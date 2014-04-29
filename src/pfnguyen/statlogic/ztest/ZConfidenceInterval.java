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
package pfnguyen.statlogic.ztest;

import org.apache.commons.math3.distribution.NormalDistribution;

public class ZConfidenceInterval {
    private double lowerBound;
    private double upperBound;
    private double standardError;
    private NormalDistribution normal = new NormalDistribution();

    public ZConfidenceInterval(double x[], double stdDev,
            double significance) {
        calcConfidenceInterval(calcSampleMean(x), stdDev, x.length, significance);
    }

    public ZConfidenceInterval(double xBar, double stdDev, int n,
            double significance) {
        calcConfidenceInterval(xBar, stdDev, n, significance);
    }

    /** stdDev, n, confidence must not be BigDecimals */
    public void calcConfidenceInterval(double sampleMean,
            double stdDev, int n, double significance) {
        standardError = normal.inverseCumulativeProbability(
                1 - significance / 2) * stdDev / Math.sqrt(n);
        upperBound = sampleMean + standardError;
        lowerBound = sampleMean - standardError;
    }

    public String confidenceInterval() {
        return "[" + lowerBound + "," + upperBound + "]";
    }

    private double calcSampleMean(double x[]) {
        double sum = 0;
        double sampleMean;

        for (int i = 0; i < x.length; i++) {
            sum += x[i];
        }
        sampleMean = sum / x.length;
        return sampleMean;
    }
}
