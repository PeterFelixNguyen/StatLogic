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
 * Class that calculates the mean of a data set.
 *
 * @author Peter "Felix" Nguyen
 */
public class Mean {
    private Double mean;

    @SuppressWarnings("unused")
    private Mean() {
    }

    /**
     * Constructs a Mean class with the specified ArrayList of
     * BigDecimal values used to calculate the mean.
     *
     * @param  values  the values from the data set
     */
    public Mean(ArrayList<Double> values) {
        calcMean(values);
    }

    /**
     * Calculates the mean of a data set with the specified
     * ArrayList of BigDecimal values.
     *
     * @param  values  the values from the data set
     */
    private void calcMean(ArrayList<Double> values) {
        double sum = 0;

        for (int i = 0; i < values.size(); i++) {
            sum += values.get(i);
        }

        mean = sum / values.size();
    }

    /**
     * Converts the BigDecimal value of the mean into a String.
     * 
     * @return  the String representation of the mean value
     */
    @Override
    public String toString() {
        return mean.toString();
    }

    /**
     * @return  the mean of the data set
     */
    public double getMean() {
        return mean;
    }
}