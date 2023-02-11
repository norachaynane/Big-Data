# Spark Project

Created by Nora Chaynane


Project steps
Step 1. Download a dataset (.csv file) from the site
https://vincentarelbundock.github.io/Rdatasets/datasets.html
For example mtcars.csv
Step 2. Select a categorical variable and a numeric variable and form the key-value pair and
create a pairRDD called “population”.
For example cyl (short for cylinder) can be the categorical variable since there are only
three distinct values: 4, 6 and 8. The mpg (miles per gallon) can be the numerical variable.
So in this step you will create a pairRDD with key cyl and value mpg.
Step 3. Compute the mean mpg and variance for each category and display as shown below:
Category Mean Variance
4 17.29 1.27
6 19.58 1.75
8 22.44 2.30
Step 4. Create the sample for bootstrapping. All you need to do is take 25% of the
population without replacement.
Step 5. Do 1000 times
 5a. Create a “resampledData”. All you need to do is take 100% of the sample with
replacement.
 5b. Compute the mean mpg and variance for each category (similar to Step 3).
 5c. Keep adding the values in some running sum.
Step 6. Divide each quantity by 1000 to get the average and display the result.
Category Mean Variance
4 17.42 1.27
6 19.31 1.67
8 22.46 2.22
OPTIONAL
These are fun steps for those who want to explore and learn more.
Step 6 Determine the absolute error percentage for each of the values being estimated.
abs(actual – estimate)*100/actual.
In this example, there are six values. For each one of those six you need to compute
abs(actual – estimate)*100/actual. For instance, for mean of 4 cyl.
= abs(17.29-1742)*100/17.29 = 0.75. 
