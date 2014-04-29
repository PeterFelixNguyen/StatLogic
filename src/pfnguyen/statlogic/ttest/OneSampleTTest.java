/**
 * Copyright 2014 Peter "Felix" Nguyen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pfnguyen.statlogic.ttest;

import java.util.ArrayList;

import org.apache.commons.math3.distribution.TDistribution;

import pfnguyen.statlogic.options.CalculatorOptions.Hypothesis;

public class OneSampleTTest {
    private TDistribution tDistribution;
    private double testStatistic;
    private Double criticalRegion;
    private Hypothesis hypothesis;
    private Double lowerRegion;
    private Double upperRegion;
    private String nullHypothesis;
    private String altHypothesis;
    private String xBar;
    private String n;
    private String sigma;
    private String alpha;
    private String conclusion = "Test statistic is within the critical region \n"
            + "Reject the null hypothesis";
    private boolean rejectNull;
    private double calculatedMean;

    /**
     * Constructs OneSampleTTest with an ArrayList of values.
     * 
     * @param  hAlternative  the inequality for alternative hypothesis
     * @param  testValue     the value for the null hypothesis
     * @param  x             the data sample
     * @param  significance  the significance level
     */
    public OneSampleTTest(Hypothesis hAlternative, double testValue,
            ArrayList<Double> x, double significance) {
        tDistribution = new TDistribution((double)(x.size()-1));
        getCriticalRegion(hAlternative, significance);
        double sampleStdDev = Math.sqrt(calcSampleVariance(x));
        calcTestStatistic(hAlternative, calcSampleMean(x),
                testValue, sampleStdDev, x);
        constructStrings(this.hypothesis = hAlternative, testValue,
                calculatedMean, sampleStdDev, x.size(), significance);
        rejectNull = testHypothesis();
    }

    /**
     * Constructs OneSampleTTest with precalculated values for
     * sample mean, sample standard deviation, and sample size.
     * 
     * @param  hAlternative  the inequality for alternative hypothesis
     * @param  testValue     the value for the null hypothesis
     * @param  xBar          the sample mean
     * @param  sampleStdDev  the sample standard deviation
     * @param  n             the sample size
     * @param  significance  the significance level
     */
    public OneSampleTTest(Hypothesis hAlternative, double testValue,
            double xBar, double sampleStdDev,
            int n, double significance) {
        tDistribution = new TDistribution((double)(n-1));
        getCriticalRegion(hAlternative, significance);
        calcTestStatistic(hAlternative, xBar, testValue, sampleStdDev, n);
        constructStrings(this.hypothesis = hAlternative, testValue, xBar,
                sampleStdDev, n, significance);
        rejectNull = testHypothesis();
    }

    /**
     * Constructs Strings for alternative hypothesis, sample mean, sample size,
     * sample standard deviation, and significance level.
     * 
     * @param  hAlternative  the inequality for alternative hypothesis
     * @param  testValue     the value for the null hypothesis
     * @param  xBar          the sample mean
     * @param  sampleStdDev  the sample standard deviation
     * @param  n             the sample size
     * @param  significance  the significance level
     */
    private void constructStrings(Hypothesis hAlternative,
            double testValue, double xBar,
            double sampleStdDev, int n, double significance) {
        String inequality = "";

        if (hAlternative == Hypothesis.LESS_THAN)
            inequality = "< ";
        else if (hAlternative == Hypothesis.GREATER_THAN)
            inequality = "> ";
        else if (hAlternative == Hypothesis.NOT_EQUAL)
            inequality = "!= ";
        nullHypothesis = "H0: mu " + "= " + testValue;
        altHypothesis = "H1: mu " + inequality + testValue;
        this.xBar = "" + xBar;
        this.n = "" + n;
        this.sigma = "" + sampleStdDev;
        this.alpha = "" + significance;
    }

    /**
     * Calculates the critical region.
     * 
     * @param hAlternative  the inequality for alternative hypothesis
     * @param significance  the significance level
     */
    private void getCriticalRegion(Hypothesis hAlternative,
            double significance) {
        if (hAlternative == Hypothesis.NOT_EQUAL) {
            lowerRegion = tDistribution.inverseCumulativeProbability(significance / 2);
            upperRegion = tDistribution.inverseCumulativeProbability(1 - (significance / 2));
        }
        else if (hAlternative == Hypothesis.LESS_THAN) {
            criticalRegion = tDistribution.inverseCumulativeProbability(significance);
        }
        else if (hAlternative == Hypothesis.GREATER_THAN) {
            criticalRegion = Math.abs(tDistribution.inverseCumulativeProbability(significance));
        }
        else {
            System.out.println("No hypothesis/inequality selected");
        }
    }

    /**
     * Check if test statistic is within the critical region
     * to determine whether or not to reject the null hypothesis.
     * 
     * @return  true if null hypothesis is rejected, otherwise false
     */
    private boolean testHypothesis() {
        if (hypothesis == Hypothesis.LESS_THAN
                && testStatistic < criticalRegion) {
            return true;
        }
        else if (hypothesis == Hypothesis.GREATER_THAN
                && testStatistic > criticalRegion) {
            return true;
        }
        else if (hypothesis == Hypothesis.NOT_EQUAL
                && (testStatistic < lowerRegion ||
                        testStatistic > upperRegion)) {
            return true;
        }
        else
            conclusion = "Test statistic is NOT within the critical region \n"
                    + "Do NOT reject the null hypothesis";
        return false;
    }

    /**
     * Calculates the sample mean.
     *
     * @param  x  the data sample
     * @return    the sample mean
     */
    private double calcSampleMean(ArrayList<Double> x) {
        double sum = 0;

        for (int i = 0; i < x.size(); i++) {
            sum += x.get(i);
        }

        calculatedMean = sum / x.size();

        return calculatedMean;
    }

    /**
     * Calculates the sample variance.
     * 
     * @param  x  the data sample
     * @return    the sample variance
     */
    private double calcSampleVariance(ArrayList<Double> x) {
        double oneOverNAdjusted = 1 / (x.size() - 1);
        double sumOfDifference;
        double squaredDifference;
        double sumOfSquaredDifference = 1; // This is bad
        double sampleMean = calcSampleMean(x);

        for (int i = 0; i < x.size(); i++) {
            sumOfDifference = x.get(i) - sampleMean;
            squaredDifference = sumOfDifference * sumOfDifference;
            sumOfSquaredDifference = sumOfSquaredDifference + squaredDifference;
        }

        return sumOfSquaredDifference * oneOverNAdjusted;
    }

    /**
     * Calculates the test statistic if x is calculated from an ArrayList.
     * 
     * @param  hAlternative    the inequality for alternative hypothesis
     * @param  sampleMean      the sample mean
     * @param  populationMean  the population mean
     * @param  stdDev          the standard deviation
     * @param  x               the data sample
     */
    private void calcTestStatistic(Hypothesis hAlternative,
            double sampleMean, double populationMean,
            double stdDev, ArrayList<Double> x) {

        testStatistic = (sampleMean - populationMean) /
                (stdDev / Math.sqrt(x.size()));
    }

    /**
     * Calculates the test statistic if x is provided by the user.
     * 
     * @param  hAlternative    the inequality for alternative hypothesis
     * @param  sampleMean      the sample mean
     * @param  populationMean  the population mean
     * @param  stdDev          the standard deviation
     * @param  x               the data sample
     */
    private void calcTestStatistic(Hypothesis hAlternative,
            double sampleMean, double populationMean,
            double sampleStdDev, int n) {

        testStatistic = (sampleMean - populationMean) / (sampleStdDev / Math.sqrt(n));
    }

    /**
     * @return  the null hypothesis
     */
    public String getNullHypothesis() {
        return nullHypothesis;
    }

    /**
     * @return  the alt hypothesis
     */
    public String getAltHypothesis() {
        return altHypothesis;
    }

    /**
     * @return  the sample mean
     */
    public String getXBar() {
        return xBar;
    }

    /**
     * @return  the sample size
     */
    public String getN() {
        return n;
    }

    /**
     * @return  the standard deviation
     */
    public String getSigma() {
        return sigma;
    }

    /**
     * @return  the significance level
     */
    public String getAlpha() {
        return alpha;
    }

    /**
     * @return  the test statistic
     */
    public double getTestStatistics() {
        return testStatistic;
    }

    /**
     * @return  the String representation of the critical region(s)
     */
    public String getCriticalRegionAsString() {
        if (hypothesis == Hypothesis.NOT_EQUAL)
            return lowerRegion.toString() + " and " + upperRegion.toString();
        else
            return criticalRegion.toString();
    }

    /**
     * @return  the String representation of the test's conclusion
     */
    public String getConclusionAsString() {
        return conclusion;
    }

    /**
     * @return  true if null hypothesis is rejected, false otherwise
     */
    public boolean getConclusion() {
        return rejectNull;
    }
}
