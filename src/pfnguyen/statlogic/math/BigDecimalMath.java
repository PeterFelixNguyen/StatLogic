/**
 * This solution is borrowed from Jesper Udby.
 * His blog regarding this solution is found here:
 * http://blog.udby.com/archives/17
 */
package pfnguyen.statlogic.math;

import java.math.BigDecimal;

/**
 * Class that calculates the square root of a BigDecimal value.
 *
 * @author  Peter "Felix" Nguyen
 */
public class BigDecimalMath {

    /**
     * Calculates the square root of a BigDecimal value.
     *
     * @param  x  value to calculate square root
     * @return    square root value of a BigDecimal
     */
    public static BigDecimal sqrt(BigDecimal x) {
        return BigDecimal.valueOf(Math.sqrt(x.doubleValue()));
    }
}
