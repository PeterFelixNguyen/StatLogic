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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import pfnguyen.statlogic.math.BigDecimalMath;

/**
 * Class that calculates the statistical dispersion of a data set.
 *
 * @author Peter "Felix" Nguyen
 */
public class Dispersion {
    private BigDecimal range;
    private BigDecimal sampleStdDev;
    private BigDecimal sampleVariance;
    private BigDecimal stdDev;
    private BigDecimal variance;

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
    public Dispersion(ArrayList<BigDecimal> values, BigDecimal mean) {
        calcDispersion(values, mean);
    }

    /**
     * Calculates the statistical dispersion of a data set with the specified
     * ArrayList of BigDecimal values and BigDecimal mean.
     *
     * @param  values  the values from the data set
     * @param  mean    the mean of all the values
     */
    private void calcDispersion(ArrayList<BigDecimal> values, BigDecimal mean) {
        BigDecimal sum;
        ArrayList<BigDecimal> squaredValues = new ArrayList<BigDecimal>();
        ArrayList<BigDecimal> difference = new ArrayList<BigDecimal>();
        range = values.get(values.size()-1).subtract(values.get(0));

        sum = BigDecimal.ZERO;
        squaredValues.clear();
        difference.clear();

        for (int i = 0; i < values.size(); i++) {
            difference.add(values.get(i).subtract(mean));
        }

        for (int i = 0; i < values.size(); i++) {
            squaredValues.add(difference.get(i).multiply(difference.get(i)));
            sum = sum.add(squaredValues.get(i));
        }

        variance = sum.divide(new BigDecimal(Integer.toString(difference.size())), 4, RoundingMode.HALF_UP);
        sampleVariance = sum.divide(new BigDecimal(Integer.toString(difference.size() - 1)), 4, RoundingMode.HALF_UP);

        stdDev = BigDecimalMath.sqrt(variance);
        sampleStdDev = BigDecimalMath.sqrt(sampleVariance);
    }

    /**
     * @return  the range of a data set
     */
    public BigDecimal getRange() {
        return range;
    }

    /**
     * @return  the sample variance
     */
    public BigDecimal getSampleVariance() {
        return sampleVariance;
    }

    /**
     * @return  the sample standard deviation
     */
    public BigDecimal getSampleStdDev() {
        return sampleStdDev;
    }

    /**
     * @return  the population variance
     */
    public BigDecimal getVariance() {
        return variance;
    }

    /**
     * @return  the population standard deviation
     */
    public BigDecimal getStdDev() {
        return stdDev;
    }
}
