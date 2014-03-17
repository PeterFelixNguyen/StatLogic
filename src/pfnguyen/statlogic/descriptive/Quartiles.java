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

public class Quartiles {
    private BigDecimal lowerQuartile;
    private BigDecimal median;
    private BigDecimal upperQuartile;

    public Quartiles(ArrayList<BigDecimal> values) {
        calcMedian(values);
    }

    private void calcMedian(ArrayList<BigDecimal> values) {

        /* Sorting algorithm using for loops */
        for (int i = 0; i < values.size(); i++) {
            for (int j = 0; j < values.size(); j++) {

                /* Swaps elements */
                if (j != values.size() - 1)
                    if (values.get(j).compareTo(values.get(j + 1)) > 0) {
                        BigDecimal temp = values.get(j);
                        values.set(j, values.get(j + 1));
                        values.set(j + 1, temp);
                    }
            }
        }

        /* Finds median for even sized ArrayList */
        if (values.size() % 2 == 0) {
            if (values.size() % 4 != 0) {
                lowerQuartile = values.get((values.size()/4)); System.out.println(lowerQuartile);
                median = values.get(values.size()/2-1).add(values.get((values.size()/2)));
                median = median.divide(new BigDecimal(Integer.toString(2)), 4, RoundingMode.HALF_UP);
                upperQuartile = values.get((values.size()/4*3+1)); System.out.println(upperQuartile);
            }
            else {
                lowerQuartile = values.get(values.size()/4-1).add(values.get((values.size()/4)));
                lowerQuartile = lowerQuartile.divide(new BigDecimal(Integer.toString(2)), 4, RoundingMode.HALF_UP); System.out.println(lowerQuartile);
                median = values.get(values.size()/2-1).add(values.get((values.size()/2)));
                median = median.divide(new BigDecimal(Integer.toString(2)), 4, RoundingMode.HALF_UP);
                upperQuartile = values.get(values.size()/4*3-1).add(values.get((values.size()/4*3)));
                upperQuartile = upperQuartile.divide(new BigDecimal(Integer.toString(2)), 4, RoundingMode.HALF_UP); System.out.println(upperQuartile);
            }
        }
        /* Finds median for odd sized ArrayList */
        else if (values.size() % 4 == 1) {
            lowerQuartile = values.get(values.size()/4-1).add(values.get((values.size()/4)));
            lowerQuartile = lowerQuartile.divide(new BigDecimal(Integer.toString(2)), 4, RoundingMode.HALF_UP); System.out.println(lowerQuartile);
            median = values.get((values.size()/2));
            upperQuartile = values.get(values.size()/4*3).add(values.get((values.size()/4*3+1)));
            upperQuartile = upperQuartile.divide(new BigDecimal(Integer.toString(2)), 4, RoundingMode.HALF_UP); System.out.println(upperQuartile);
        }
        else {
            lowerQuartile = values.get((values.size()/4)); System.out.println(lowerQuartile);
            median = values.get((values.size()/2));
            upperQuartile = values.get((values.size()/4*3+2)); System.out.println(upperQuartile);
        }
        median.setScale(4, RoundingMode.HALF_UP); // Not working properly
    }

    @Override
    public String toString() {
        return median.toString();
    }

    public BigDecimal getMedian() {
        return median;
    }
}