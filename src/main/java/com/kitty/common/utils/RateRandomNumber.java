
package com.kitty.common.utils;

import java.util.Iterator;
import org.apache.commons.lang3.RandomUtils;
import java.util.ArrayList;
import java.util.List;

public class RateRandomNumber
{
    public static double produceRandomNumber(final double min, final double max) {
        return RandomUtil.getRandomDouble(min, max);
    }
    
    public static double produceRateRandomNumber(final double min, final double max, final List<Double> separates, final List<Integer> percents) {
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
        for (final Integer p : percents) {
            if (p < 0 || p > 100) {
                throw new IllegalArgumentException("百分比必须在[0,100]之间");
            }
            totalPercent += p;
        }
        if (totalPercent != 100) {
            throw new IllegalArgumentException("百分比之和必须为100");
        }
        for (final double s : separates) {
            if (s <= min || s >= max) {
                throw new IllegalArgumentException("分割数值必须在(min,max)之间");
            }
        }
        final int rangeCount = separates.size() + 1;
        final List<Range> ranges = new ArrayList<Range>();
        int scopeMax = 0;
        for (int i = 0; i < rangeCount; ++i) {
            final Range range = new Range();
            range.min = ((i == 0) ? min : separates.get(i - 1));
            range.max = ((i == rangeCount - 1) ? max : separates.get(i));
            range.percent = percents.get(i);
            range.percentScopeMin = scopeMax + 1;
            range.percentScopeMax = range.percentScopeMin + (range.percent - 1);
            scopeMax = range.percentScopeMax;
            ranges.add(range);
        }
        double r = min;
        final int randomInt = RandomUtils.nextInt(1, 101);
        for (int j = 0; j < ranges.size(); ++j) {
            final Range range2 = ranges.get(j);
            if (range2.percentScopeMin <= randomInt && randomInt <= range2.percentScopeMax) {
                r = produceRandomNumber(range2.min, range2.max);
                break;
            }
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
