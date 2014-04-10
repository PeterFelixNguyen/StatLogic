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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.apache.commons.math3.distribution.TDistribution;

import pfnguyen.statlogic.math.BigDecimalMath;
import pfnguyen.statlogic.options.CalculatorOptions.Hypothesis;

public class OneSampleTTest {
    private TDistribution tDistribution;
    private BigDecimal testStatistic;
    private BigDecimal criticalRegion; // move to an if else in constructor???
    private Hypothesis hypothesis;
    private BigDecimal lowerRegion; // move to an if else in constructor???
    private BigDecimal upperRegion; // move to an if else in constructor???
    private String nullHypothesis;
    private String altHypothesis;
    private String xBar;
    private String n;
    private String sigma;
    private String alpha;
    private String conclusion = "Test statistic is within the critical region \n" + "Reject the null hypothesis";
    private BigDecimal calculatedMean;

    public OneSampleTTest(Hypothesis hAlternative, BigDecimal testValue,
            ArrayList<BigDecimal> x, double significance) {
        tDistribution = new TDistribution((double)(x.size()-1));
        getCriticalRegion(hAlternative, significance);
        BigDecimal sampleStdDev = BigDecimalMath.sqrt(calcSampleVariance(x));
        calcTestStatistic(hAlternative, calcSampleMean(x), testValue, sampleStdDev, x);
        constructStrings(this.hypothesis = hAlternative, testValue, calculatedMean, sampleStdDev, x.size(), significance);
    }

    public OneSampleTTest(Hypothesis hAlternative, BigDecimal testValue,
            BigDecimal xBar, BigDecimal sampleStdDev, int n, double significance) {
        tDistribution = new TDistribution((double)(n-1));
        getCriticalRegion(hAlternative, significance);
        calcTestStatistic(hAlternative, xBar, testValue, sampleStdDev, n);
        constructStrings(this.hypothesis = hAlternative, testValue, xBar, sampleStdDev, n, significance);
    }

    private void constructStrings(Hypothesis hypothesis, BigDecimal testValue, BigDecimal xBar, BigDecimal sampleStdDev, int n, double significance) {
        String inequality = "";

        if (hypothesis == Hypothesis.LESS_THAN)
            inequality = "< ";
        else if (hypothesis == Hypothesis.GREATER_THAN)
            inequality = "> ";
        else if (hypothesis == Hypothesis.NOT_EQUAL)
            inequality = "!= ";
        nullHypothesis = "H0: mu " + "= " + testValue;
        altHypothesis = "H1: mu " + inequality + testValue;
        this.xBar = "" + xBar;
        this.n = "" + n;
        this.sigma = "" + sampleStdDev;
        this.alpha = "" + significance;
    }

    /* Change to double, BigDecimal precision not necessary */
    private void getCriticalRegion(Hypothesis hypothesis,
            double significance) {
        if (hypothesis == Hypothesis.NOT_EQUAL) {
            lowerRegion = new BigDecimal(
                    tDistribution.inverseCumulativeProbability(significance / 2));
            lowerRegion = lowerRegion.setScale(3, BigDecimal.ROUND_HALF_UP);
            upperRegion = new BigDecimal(
                    tDistribution.inverseCumulativeProbability(1 - (significance / 2)));
            upperRegion = upperRegion.setScale(3, BigDecimal.ROUND_HALF_UP);
        }
        else if (hypothesis == Hypothesis.LESS_THAN) {
            criticalRegion = new BigDecimal(
                    tDistribution.inverseCumulativeProbability(significance));
            criticalRegion = criticalRegion.setScale(3, BigDecimal.ROUND_HALF_UP);
        }
        else if (hypothesis == Hypothesis.GREATER_THAN) {
            criticalRegion = new BigDecimal(
                    tDistribution.inverseCumulativeProbability(significance));
            criticalRegion = criticalRegion.setScale(3, BigDecimal.ROUND_HALF_UP).abs();
        }
        else {
            System.out.println("No hypothesis/inequality selected");
        }
    }

    public boolean testHypothesis() {
        if (hypothesis == Hypothesis.LESS_THAN
                && testStatistic.compareTo(criticalRegion) < 0) {
            return true;
        }
        else if (hypothesis == Hypothesis.GREATER_THAN
                && testStatistic.compareTo(criticalRegion) > 0) {
            return true;
        }
        else if (hypothesis == Hypothesis.NOT_EQUAL
                && (testStatistic.compareTo(lowerRegion) < 0 || testStatistic
                        .compareTo(upperRegion) > 0)) {
            return true;
        }
        else
            conclusion = "Test statistic is NOT within the critical region \n" + "Do NOT reject the null hypothesis";
        return false;
    }

    /**
     * @return  the String representation of the test's conclusion
     */
    public String getConclusionAsString() {
        return conclusion;
    }

    /**
     * Calculates the sample mean
     *
     * @param  x  the data sample
     * @return    the sample mean
     */
    private BigDecimal calcSampleMean(ArrayList<BigDecimal> x) {
        BigDecimal sum = BigDecimal.ZERO;

        for (int i = 0; i < x.size(); i++) {
            sum = sum.add(x.get(i));
        }

        calculatedMean = sum.divide(new BigDecimal(x.size()), 4,
                RoundingMode.HALF_UP);

        return calculatedMean;
    }

    /**
     * Calculates the sample variance
     * 
     * @param  x  the data sample
     * @return    the sample variance
     */
    private BigDecimal calcSampleVariance(ArrayList<BigDecimal> x) {
        BigDecimal oneOverNAdjusted = BigDecimal.ONE.divide(new BigDecimal(x.size()).subtract(BigDecimal.ONE), 4, RoundingMode.HALF_UP);
        BigDecimal sumOfDifference;
        BigDecimal squaredDifference;
        BigDecimal sumOfSquaredDifference = BigDecimal.ONE;
        BigDecimal sampleMean = calcSampleMean(x);

        for (int i = 0; i < x.size(); i++) {
            sumOfDifference = x.get(i).subtract(sampleMean);
            squaredDifference = sumOfDifference.multiply(sumOfDifference);
            sumOfSquaredDifference = sumOfSquaredDifference.add(squaredDifference);
        }

        return sumOfSquaredDifference.multiply(oneOverNAdjusted);
    }

    private void calcTestStatistic(Hypothesis hAlternative,
            BigDecimal sampleMean, BigDecimal populationMean,
            BigDecimal stdDev, ArrayList<BigDecimal> x) {

        testStatistic = sampleMean.subtract(populationMean).
                divide(stdDev.divide(new BigDecimal(Math.sqrt(x.size())),
                        4, RoundingMode.HALF_UP), 4, RoundingMode.HALF_UP);
    }

    private void calcTestStatistic(Hypothesis hAlternative,
            BigDecimal sampleMean, BigDecimal populationMean,
            BigDecimal sampleStdDev, int n) {

        testStatistic = sampleMean.subtract(populationMean)
                .divide(sampleStdDev.divide(new BigDecimal(Math.sqrt(n)),
                        4, RoundingMode.HALF_UP), 4, RoundingMode.HALF_UP);
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
    public BigDecimal getTestStatistics() {
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
}
