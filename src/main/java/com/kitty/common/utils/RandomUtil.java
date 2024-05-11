
package com.kitty.common.utils;

import java.util.Random;

public class RandomUtil
{
    private static Random rand;
    
    public static int round(final Integer min, final Integer max) {
        if (min < 0 || max < 0) {
            return BigDecimalUtil.decimalScaleIntegerDown(0, getRandomDouble((double)min, (double)max));
        }
        return RandomUtil.rand.nextInt(max - min + 1) + min;
    }
    
    public static double getRandomDouble(final Double begin, final Double end) {
        if (begin.equals(end)) {
            return begin;
        }
        final Double rtn = begin + Math.random() * (end - begin);
        if (rtn.equals(begin) || rtn.equals(end)) {
            return getRandomDouble(begin, end);
        }
        return rtn;
    }
    
    static {
        RandomUtil.rand = new Random();
    }
}
