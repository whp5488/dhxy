
package com.kitty.common.utils.text;

import java.util.List;
import java.util.ArrayList;

public enum Chinese
{
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
    
    private Chinese(final char cValue, final int aValue) {
        this.cValue = cValue;
        this.aValue = aValue;
    }
    
    public static Integer getAFromC(final String cValue) {
        int length = cValue.length() - 1;
        int total = 0;
        while (length >= 0) {
            final char chinese = cValue.charAt(length);
            final int arabic = c2a(chinese);
            if (arabic >= 10 && length - 1 >= 0) {
                --length;
                final int arabicValue = c2a(cValue.charAt(length));
                total += arabic * arabicValue;
                --length;
            }
            else {
                total += arabic;
                --length;
            }
        }
        return total;
    }
    
    public static String getCFromA(int aValue) {
        if (aValue == 0) {
            return String.valueOf(Chinese.LING.cValue);
        }
        final StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        final List<Integer> arrayList = new ArrayList<Integer>(10);
        while (aValue > 0) {
            final int remainder = aValue % 10;
            arrayList.add(remainder);
            aValue /= 10;
        }
        int index = 0;
        boolean start = true;
        while (index < arrayList.size()) {
            final int currentValue = arrayList.get(index);
            if (count > 0 && currentValue != 0) {
                stringBuilder.insert(0, a2c((int)Math.pow(10.0, count)));
            }
            final char c = a2c(currentValue);
            if (currentValue != 0) {
                if (start) {
                    start = false;
                }
                stringBuilder.insert(0, c);
            }
            if (currentValue == 0 && !start) {
                stringBuilder.insert(0, c);
                start = true;
            }
            ++index;
            ++count;
        }
        if (stringBuilder.length() > 1 && stringBuilder.charAt(0) == Chinese.YI.cValue && stringBuilder.charAt(1) == Chinese.SHI.cValue) {
            stringBuilder.deleteCharAt(0);
        }
        return stringBuilder.toString();
    }
    
    static int c2a(final char cValue) {
        for (final Chinese c : values()) {
            if (c.cValue == cValue) {
                return c.aValue;
            }
        }
        return 0;
    }
    
    static char a2c(final int aValue) {
        for (final Chinese c : values()) {
            if (c.aValue == aValue) {
                return c.cValue;
            }
        }
        return ' ';
    }
}
