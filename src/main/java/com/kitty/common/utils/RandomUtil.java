package com.kitty.common.utils;

import java.util.Random;

public class RandomUtil {
  private static Random rand = new Random();
  
  public static int round(Integer min, Integer max) {
    if (min.intValue() < 0 || max.intValue() < 0)
      return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(getRandomDouble(Double.valueOf(min.doubleValue()), Double.valueOf(max.doubleValue())))); 
    return rand.nextInt(max.intValue() - min.intValue() + 1) + min.intValue();
  }
  
  public static double getRandomDouble(Double begin, Double end) {
    if (begin.equals(end))
      return begin.doubleValue(); 
    Double rtn = Double.valueOf(begin.doubleValue() + Double.valueOf(Math.random() * (end.doubleValue() - begin.doubleValue())).doubleValue());
    if (rtn.equals(begin) || rtn.equals(end))
      return getRandomDouble(begin, end); 
    return rtn.doubleValue();
  }
}
