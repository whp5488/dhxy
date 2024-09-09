package com.kitty.common.utils;

import java.util.Iterator;
import org.apache.commons.lang3.RandomUtils;
import java.util.ArrayList;
import java.util.List;

public class RateRandomNumber
{
    public RateRandomNumber() {
    }
    
    public static double produceRandomNumber(double min, double max) {
        return RandomUtil.getRandomDouble(Double.valueOf(min), Double.valueOf(max));
    }
    
    public static double produceRateRandomNumber(double min, double max, List<Double> separates, List<Integer> percents) {
        if (min == 0.0 && max == 0.0) {
            return 0.0;
        }
        if (min > max) {
            throw new IllegalArgumentException("min值必须小于max值");
        }
        if (separates == null || percents == null || separates.size() == 0) {
            return produceRandomNumber(min, max);
        }
        if (separates.size() + 1 != percents.size()) {
            throw new IllegalArgumentException("分割数字的个数加1必须等于百分比个数");
        }
        int totalPercent = 0;
        for (Integer p : percents) {
            if ((int)p < 0 || (int)p > 100) {
                throw new IllegalArgumentException("百分比必须在[0,100]之间");
            }
            totalPercent += (int)p;
        }
        if (totalPercent != 100) {
            throw new IllegalArgumentException("百分比之和必须为100");
        }
        for (double s : separates) {
            if (s <= min || s >= max) {
                throw new IllegalArgumentException("分割数值必须在(min,max)之间");
            }
            continue;
        }
        int rangeCount = separates.size() + 1;
        List<RateRandomNumber.Range> ranges = new ArrayList<>();
        int scopeMax = 0;
        for (int i = 0; i < rangeCount; ++i) {
            RateRandomNumber.Range range = new RateRandomNumber.Range();
            range.min = ((i == 0) ? min : ((double)separates.get(i - 1)));
            range.max = ((i == rangeCount - 1) ? max : ((double)separates.get(i)));
            range.percent = (int)percents.get(i);
            range.percentScopeMin = scopeMax + 1;
            range.percentScopeMax = range.percentScopeMin + (range.percent - 1);
            scopeMax = range.percentScopeMax;
            ranges.add(range);
        }
        double r = min;
        int randomInt = RandomUtils.nextInt(1, 101);
        int j = 0;
        while (j < ranges.size()) {
            RateRandomNumber.Range range2 = (RateRandomNumber.Range)ranges.get(j);
            if (range2.percentScopeMin <= randomInt && randomInt <= range2.percentScopeMax) {
                r = produceRandomNumber(range2.min, range2.max);
                break;
            }
            ++j;
        }
        return r;
    }
    
    public static class Range
    {
        public double min;
        public double max;
        public int percent;
        public int percentScopeMin;
        public int percentScopeMax;
    }
}
