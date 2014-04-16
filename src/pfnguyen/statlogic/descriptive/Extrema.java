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
import java.util.ArrayList;

import pfnguyen.statlogic.algorithms.SortAlgorithms;

/**
 * Class that calculates the extrema of a data set.
 * 
 * @author Peter "Felix" Nguyen
 */
public class Extrema {
    private BigDecimal maxima;
    private BigDecimal minima;

    /**
     * Constructs an Extrema class with the specified ArrayList of BigDecimal
     * values used to calculate the extrema of a data set.
     * 
     * @param  values  the values used for calculation of extrema
     */
    public Extrema(ArrayList<BigDecimal> values) {
        calcExtrema(values);
    }

    /**
     * Calculates the minima and maxima of a data set.
     * 
     * @param  values  the values used for calculation of extrema
     */
    private void calcExtrema(ArrayList<BigDecimal> values) {
        SortAlgorithms.BubbleSort(values);
        minima = values.get(0);
        maxima = values.get(values.size() - 1);
    }

    /**
     * @return  the minima of a data set
     */
    public BigDecimal getMinima() {
        return minima;
    }

    /**
     * @return  the maxima of a data set
     */
    public BigDecimal getMaxima() {
        return maxima;
    }
}
