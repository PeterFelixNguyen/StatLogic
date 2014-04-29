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
package pfnguyen.statlogic.descriptive;

import java.util.ArrayList;

/**
 * Class that calculates the statistical dispersion of a data set.
 *
 * @author Peter "Felix" Nguyen
 */
public class Dispersion {
    private double range;
    private double sampleStdDev;
    private double sampleVariance;
    private double stdDev;
    private double variance;

    @SuppressWarnings("unused")
    private Dispersion() {
    }

    /**
     * Constructs a Dispersion class with the specified ArrayList of
     * BigDecimal values and BigDecimal mean used to calculate statistical
     * dispersion.
     *
     * @param  values  the values from the data set
     * @param  mean    the mean of all the values
     */
    public Dispersion(ArrayList<Double> values, double mean) {
        calcDispersion(values, mean);
    }

    /**
     * Calculates the statistical dispersion of a data set with the specified
     * ArrayList of BigDecimal values and BigDecimal mean.
     *
     * @param  values  the values from the data set
     * @param  mean    the mean of all the values
     */
    private void calcDispersion(ArrayList<Double> values, double mean) {
        double sum;
        ArrayList<Double> squaredValues = new ArrayList<Double>();
        ArrayList<Double> difference = new ArrayList<Double>();
        range = values.get(values.size()-1) - values.get(0);

        sum = 0;
        squaredValues.clear(); // I think i can delete this
        difference.clear(); // This too

        for (int i = 0; i < values.size(); i++) {
            difference.add(values.get(i) - mean);
        }

        for (int i = 0; i < values.size(); i++) {
            squaredValues.add(difference.get(i) * difference.get(i));
            sum += squaredValues.get(i);
        }

        variance = sum / difference.size();
        sampleVariance = sum / (difference.size() - 1);

        stdDev = Math.sqrt(variance);
        sampleStdDev = Math.sqrt(sampleVariance);
    }

    /**
     * @return  the range of a data set
     */
    public double getRange() {
        return range;
    }

    /**
     * @return  the sample variance
     */
    public double getSampleVariance() {
        return sampleVariance;
    }

    /**
     * @return  the sample standard deviation
     */
    public double getSampleStdDev() {
        return sampleStdDev;
    }

    /**
     * @return  the population variance
     */
    public double getVariance() {
        return variance;
    }

    /**
     * @return  the population standard deviation
     */
    public double getStdDev() {
        return stdDev;
    }
}
