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
 * Class that calculates the mode of a data set.
 *
 * @author Peter "Felix" Nguyen
 */
public class Mode {
    private ArrayList<Double> mode = new ArrayList<Double>();

    /**
     * Constructs a Mode class with the specified ArrayList of
     * BigDecimal values used to calculate the mode.
     *
     * @param  values  the values from the data set
     */
    public Mode(ArrayList<Double> values) {
        calcMode(values);
    }

    /**
     * Calculates the mode of a data set with the specified
     * ArrayList of BigDecimal values.
     *
     * @param  values  the values from the data set
     */
    private void calcMode(ArrayList<Double> values) {
        int maxCount = 0;
        /* Tallies each value to determine the max count */
        for (int i = 0; i < values.size(); i++) {
            int count = 0; // count resets to 0
            for (int j = 0; j < values.size(); j++) {
                if (values.get(j).equals(values.get(i))) {
                    count++;

                    if (count > maxCount) {
                        maxCount = count;
                    }
                }
            }
        }

        mode.clear(); // empties mode for re-use
        /* Adds a mode to the list if it qualifies */
        for (int i = 0; i < values.size(); i++) {
            int count = 0;
            for (int j = 0; j < values.size(); j++) {
                if (values.get(j).equals(values.get(i))) {
                    count++;
                    if (count == maxCount && !mode.contains(values.get(i))) {
                        mode.add(values.get(i));
                    }
                }
            }
        }
    }

    /**
     * Converts the BigDecimal value of the mode into a String
     * 
     * @return  the String representation of the mean value
     */
    @Override
    public String toString() {
        return mode.toString();
    }

    /**
     * alternative toString
     */
    public StringBuilder toString2() {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < mode.size(); i++) {
            string.append(mode.get(i).toString() + " ");
            if ((i + 1) % 5 == 0) {
                string.append("\n");
            }
        }
        return string;
    }

    /**
     * @return  the ArrayList of the modes of a data set
     */
    public ArrayList<Double> getMode() {
        return mode;
    }
}