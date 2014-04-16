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

import org.apache.commons.math3.distribution.TDistribution;

public class TConfidenceInterval {
    private BigDecimal lowerBound;
    private BigDecimal upperBound;
    private BigDecimal standardError;
    private TDistribution tDistribution;

    public TConfidenceInterval(BigDecimal x[], BigDecimal stdDev,
            double confidence) {
        tDistribution = new TDistribution((double)(x.length-1));
        calcConfidenceInterval(calcSampleMean(x), stdDev, x.length, confidence);
    }

    public TConfidenceInterval(BigDecimal xBar, BigDecimal stdDev, int n,
            double confidence) {
        tDistribution = new TDistribution((double)(n-1));
        calcConfidenceInterval(xBar, stdDev, n, confidence);
    }

    /** stdDev, n, confidence must not be BigDecimals */
    public void calcConfidenceInterval(BigDecimal sampleMean,
            BigDecimal stdDev, int n, double significance) {
        standardError = new BigDecimal(
                tDistribution.inverseCumulativeProbability(1 - significance / 2))
        .multiply(stdDev).divide(new BigDecimal(Math.sqrt(n)), 4,
                RoundingMode.HALF_UP);
        upperBound = sampleMean.add(standardError);
        lowerBound = sampleMean.subtract(standardError);
    }

    public String confidenceInterval() {
        return "[" + lowerBound + "," + upperBound + "]";
    }

    private BigDecimal calcSampleMean(BigDecimal x[]) {
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal sampleMean;

        for (int i = 0; i < x.length; i++) {
            sum = sum.add(x[i]);
        }
        sampleMean = sum.divide(new BigDecimal(x.length), 4,
                RoundingMode.HALF_UP);
        return sampleMean;
    }
}
