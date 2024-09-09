package com.kitty.common.utils;

import java.util.Random;

public class RandomUtil
{
    private static Random rand;
    
    public RandomUtil() {
    }
    
    public static int round(Integer min, Integer max) {
        if ((int)min < 0 || (int)max < 0) {
            return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(getRandomDouble(Double.valueOf((double)min), Double.valueOf((double)max))));
        }
        return RandomUtil.rand.nextInt((int)max - (int)min + 1) + (int)min;
    }
    
    public static double getRandomDouble(Double begin, Double end) {
        if (begin.equals(end)) {
            return (double)begin;
        }
        Double rtn = Double.valueOf((double)begin + (double)Double.valueOf(Math.random() * ((double)end - (double)begin)));
        if (rtn.equals(begin) || rtn.equals(end)) {
            return getRandomDouble(begin, end);
        }
        return (double)rtn;
    }
    
    static {
        RandomUtil.rand = new Random();
    }
}
