package com.kitty.common.utils.text;

import java.util.ArrayList;
import java.util.List;

public enum Chinese {
  YI('一', 1),
  ER('二', 2),
  SAN('三', 3),
  SI('四', 4),
  WU('五', 5),
  LIU('六', 6),
  QI('七', 7),
  BA('八', 8),
  JIU('九', 9),
  LING('零', 0),
  SHI('十', 10),
  BAI('百', 100),
  QIAN('千', 1000),
  WAN('万', 10000);
  
  private char cValue;
  
  private int aValue;
  
  Chinese(char cValue, int aValue) {
    this.cValue = cValue;
    this.aValue = aValue;
  }
  
  public static Integer getAFromC(String cValue) {
    int length = cValue.length() - 1;
    int total = 0;
    while (length >= 0) {
      char chinese = cValue.charAt(length);
      int arabic = c2a(chinese);
      if (arabic >= 10 && length - 1 >= 0) {
        length--;
        int arabicValue = c2a(cValue.charAt(length));
        total += arabic * arabicValue;
        length--;
        continue;
      } 
      total += arabic;
      length--;
    } 
    return Integer.valueOf(total);
  }
  
  public static String getCFromA(int aValue) {
    if (aValue == 0)
      return String.valueOf(LING.cValue); 
    StringBuilder stringBuilder = new StringBuilder();
    int count = 0;
    List<Integer> arrayList = new ArrayList<>(10);
    while (aValue > 0) {
      int remainder = aValue % 10;
      arrayList.add(Integer.valueOf(remainder));
      aValue /= 10;
    } 
    int index = 0;
    boolean start = true;
    while (index < arrayList.size()) {
      int currentValue = ((Integer)arrayList.get(index)).intValue();
      if (count > 0 && currentValue != 0)
        stringBuilder.insert(0, a2c((int)Math.pow(10.0D, count))); 
      char c = a2c(currentValue);
      if (currentValue != 0) {
        if (start)
          start = false; 
        stringBuilder.insert(0, c);
      } 
      if (currentValue == 0 && !start) {
        stringBuilder.insert(0, c);
        start = true;
      } 
      index++;
      count++;
    } 
    if (stringBuilder.length() > 1 && stringBuilder.charAt(0) == YI.cValue && stringBuilder.charAt(1) == SHI.cValue)
      stringBuilder.deleteCharAt(0); 
    return stringBuilder.toString();
  }
  
  static int c2a(char cValue) {
    for (Chinese c : values()) {
      if (c.cValue == cValue)
        return c.aValue; 
    } 
    return 0;
  }
  
  static char a2c(int aValue) {
    for (Chinese c : values()) {
      if (c.aValue == aValue)
        return c.cValue; 
    } 
    return ' ';
  }
}
