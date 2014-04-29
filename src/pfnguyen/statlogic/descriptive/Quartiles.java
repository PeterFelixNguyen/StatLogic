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

import pfnguyen.statlogic.algorithms.SortAlgorithms;

public class Quartiles {
    private double lowerQuartile;
    private Double median;
    private double upperQuartile;

    public Quartiles(ArrayList<Double> values) {
        calcMedian(values);
    }

    private void calcMedian(ArrayList<Double> values) {

        SortAlgorithms.BubbleSort(values);

        /* Finds median for even sized ArrayList */
        if (values.size() % 2 == 0) {
            if (values.size() % 4 != 0) {
                lowerQuartile = values.get((values.size()/4));
                median = values.get(values.size()/2-1) + values.get((values.size()/2)) / 2;
                upperQuartile = values.get((values.size()/4*3+1));
            }
            else {
                lowerQuartile = (values.get(values.size()/4-1) + values.get((values.size()/4))) / 2;
                median = (values.get(values.size()/2-1) + values.get((values.size()/2))) / 2;
                upperQuartile = (values.get(values.size()/4*3-1) + values.get((values.size()/4*3))) / 2;
            }
        }
        /* Finds median for odd sized ArrayList */
        else if (values.size() % 4 == 1) {
            lowerQuartile = (values.get(values.size()/4-1) + values.get((values.size()/4))) / 2;
            median = values.get((values.size()/2));
            upperQuartile = (values.get(values.size()/4*3) + values.get((values.size()/4*3+1))) / 2;
        }
        else {
            lowerQuartile = values.get((values.size()/4));
            median = values.get((values.size()/2));
            upperQuartile = values.get((values.size()/4*3+2));
        }
    }

    public String getStringOfMedian() {
        return median.toString();
    }

    public double getLowerQuartile () {
        return lowerQuartile;
    }

    public double getUpperQuartile() {
        return upperQuartile;
    }

    public double getMedian() {
        return median;
    }
}