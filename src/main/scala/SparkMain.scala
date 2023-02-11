import Utils.{mapToMeanVariance}
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer
import collection.mutable.Map

object SparkMain extends App {
  val conf = new SparkConf().setAppName("Spark and SparkSql").setMaster("local")
  val sc = new SparkContext(conf)

  val spark = SparkSession.builder.config(sc.getConf).getOrCreate()
  import spark.implicits._

  sc.setLogLevel("ERROR")

  val filePath = "input/iris.csv"

  val textFile = sc.textFile(filePath)
  val titleRow = textFile.first()
  val data = textFile.filter(row => row != titleRow) // Assuming csv will always have a title row

  val population = data.map(line => line.split(",")).map(x => (x(5).replace("\"",""), x(1).toDouble))
  population.toDF("Species", "Sepal.Length").show(truncate = false)

  val meanVariance = mapToMeanVariance(population)
  meanVariance.toDF("Category", "{Mean, Variance}").show(truncate = false)

  val sampleCount = data.count().toInt / 4 // get 25% of the records in the sample
  val setosaSample = population.filter(item => item._1.equals("setosa")).takeSample(false, sampleCount)
  val versicolorSample = population.filter(item => item._1.equals("versicolor")).takeSample(false, sampleCount)
  val virginicaSample = population.filter(item => item._1.equals("virginica")).takeSample(false, sampleCount)

  // Resample 1000 times
  var setosa = (setosaSample, new ListBuffer[(String, (Double, Double))]())
  var versicolor = (versicolorSample, new ListBuffer[(String, (Double, Double))]())
  var virginica = (virginicaSample, new ListBuffer[(String, (Double, Double))]())
  val categories = List(setosa, versicolor, virginica)

  println("Resampling 1000 times, please wait......")
  for (_ <- 1 until 1000) {
    categories.foreach(c => {
      val reSampleRDD = sc.parallelize(sc.parallelize(c._1).takeSample(true, sampleCount))
      mapToMeanVariance(reSampleRDD).collect().foreach(s => c._2 += s)
    })
  }

  val allMeanVariance: Map[String, (Double, Double)] = Map()
  categories.foreach(c => {
    allMeanVariance += (c._2(0)._1 -> (c._2.map(v => v._2._1).sum / c._2.size, c._2.map(v => v._2._2).sum / c._2.size))
  })

  println("Category \t\t (Mean, Variance)")
  println("==================================")
  for((k, v) <- allMeanVariance) {
    printf("[%s\t\t(%.2f, %.2f)]\n", k, v._1, v._2)
  }

}