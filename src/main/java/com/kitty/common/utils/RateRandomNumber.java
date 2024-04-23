package com.kitty.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.RandomUtils;

public class RateRandomNumber {
  public static double produceRandomNumber(double min, double max) {
    return RandomUtil.getRandomDouble(Double.valueOf(min), Double.valueOf(max));
  }
  
  public static double produceRateRandomNumber(double min, double max, List<Double> separates, List<Integer> percents) {
    if (min == 0.0D && max == 0.0D)
      return 0.0D; 
    if (min > max)
      throw new IllegalArgumentException("min值必须小于max值"); 
    if (separates == null || percents == null || separates.size() == 0)
      return produceRandomNumber(min, max); 
    if (separates.size() + 1 != percents.size())
      throw new IllegalArgumentException("分割数字的个数加1必须等于百分比个数"); 
    int totalPercent = 0;
    for (Integer p : percents) {
      if (p.intValue() < 0 || p.intValue() > 100)
        throw new IllegalArgumentException("百分比必须在[0,100]之间"); 
      totalPercent += p.intValue();
    } 
    if (totalPercent != 100)
      throw new IllegalArgumentException("百分比之和必须为100"); 
    for (Iterator<Double> iterator = separates.iterator(); iterator.hasNext(); ) {
      double s = ((Double)iterator.next()).doubleValue();
      if (s <= min || s >= max)
        throw new IllegalArgumentException("分割数值必须在(min,max)之间"); 
    } 
    int rangeCount = separates.size() + 1;
    List<Range> ranges = new ArrayList<>();
    int scopeMax = 0;
    for (int i = 0; i < rangeCount; i++) {
      Range range = new Range();
      range.min = (i == 0) ? min : ((Double)separates.get(i - 1)).doubleValue();
      range.max = (i == rangeCount - 1) ? max : ((Double)separates.get(i)).doubleValue();
      range.percent = ((Integer)percents.get(i)).intValue();
      range.percentScopeMin = scopeMax + 1;
      range.percentScopeMax = range.percentScopeMin + range.percent - 1;
      scopeMax = range.percentScopeMax;
      ranges.add(range);
    } 
    double r = min;
    int randomInt = RandomUtils.nextInt(1, 101);
    for (int j = 0; j < ranges.size(); j++) {
      Range range = ranges.get(j);
      if (range.percentScopeMin <= randomInt && randomInt <= range.percentScopeMax) {
        r = produceRandomNumber(range.min, range.max);
        break;
      } 
    } 
    return r;
  }
  
  public static class Range {
    public double min;
    
    public double max;
    
    public int percent;
    
    public int percentScopeMin;
    
    public int percentScopeMax;
  }
}
