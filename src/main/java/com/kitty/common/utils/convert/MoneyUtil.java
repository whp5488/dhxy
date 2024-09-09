package com.kitty.common.utils.convert;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.math.BigDecimal;

public class MoneyUtil
{
    private static final String[] RMB_ARR;
    private static final String[] UNIT_ARR;
    private static final String[] HIGH_UINIT_ARR;
    private static final String[] LOW_UNIT_ARR;
    private static final String RMB_UNIT = "元";
    private static final String RMB_INTEGER = "整";
    private static final String ZERO_STR = "0";
    private static final String DOUBLE_ZERO_STR = "00";
    private static final char ZERO_CHR = '0';
    private static final int SCALE = 0;
    
    public MoneyUtil() {
    }
    
    public static String fourFigure(BigDecimal money) {
        NumberFormat numberFormat = new DecimalFormat("##.####");
        return numberFormat.format(money);
    }
    
    public static String symbols(BigDecimal money) {
        NumberFormat numberFormat = new DecimalFormat("$##.####");
        return numberFormat.format(money);
    }
    
    public static String partition(BigDecimal money) {
        NumberFormat numberFormat = new DecimalFormat("#,###.####");
        return numberFormat.format(money);
    }
    
    public static String change(BigDecimal amount) {
        String greaterZeroToInt = greaterZeroToInt(amount);
        String itoRmbStr = itoRmbStr(greaterZeroToInt);
        BigDecimal lessZeroNum = amount.subtract(greaterZeroNum(amount));
        String lessZeroToInt = lessZeroToInt(lessZeroNum);
        String ftoRmbStr = ftoRmbStr(lessZeroToInt);
        return itoRmbStr + ftoRmbStr;
    }
    
    private static String greaterZeroToInt(BigDecimal amount) {
        if (amount == null) {
            return "0";
        }
        return amount.divide(new BigDecimal(1), 0, RoundingMode.DOWN).toString();
    }
    
    private static BigDecimal greaterZeroNum(BigDecimal amount) {
        return amount.divide(new BigDecimal(1), 0, RoundingMode.DOWN);
    }
    
    private static String lessZeroToInt(BigDecimal amount) {
        String intStr = amount.movePointRight(2).divide(new BigDecimal(1), 0, RoundingMode.HALF_UP).toBigInteger().toString();
        if (intStr.length() == 1) {
            return "0" + intStr;
        }
        return intStr;
    }
    
    private static String itoRmbStr(String numStr) {
        StringBuilder result = new StringBuilder();
        if ("0".equals(numStr)) {
            return result.toString();
        }
        int len = numStr.length();
        int col = (len - 1) / 4;
        int row = (len - 1) % 4;
        int index = 0;
        for (int i = 0; i <= col; ++i) {
            for (int j = 0; j <= row; ++j) {
                if (numStr.charAt(index) == '0') {
                    if ((numStr.charAt(index - 1) == '0' && j != 0) || j == row) {
                        ++index;
                        continue;
                    }
                    result.append(MoneyUtil.RMB_ARR[numStr.charAt(index) - '0']);
                }
                else {
                    result.append(MoneyUtil.RMB_ARR[numStr.charAt(index) - '0']);
                    result.append(MoneyUtil.UNIT_ARR[row - j]);
                }
                ++index;
            }
            result.append(MoneyUtil.HIGH_UINIT_ARR[col - i]);
            row = 3;
        }
        result.append("元");
        return result.toString();
    }
    
    private static String ftoRmbStr(String numStr) {
        String result = "";
        if (numStr.equals("00") || numStr.equals("0")) {
            return "整";
        }
        result += MoneyUtil.RMB_ARR[numStr.charAt(0) - '0'];
        if (numStr.charAt(0) != '0') {
            result += MoneyUtil.LOW_UNIT_ARR[0];
        }
        result += MoneyUtil.RMB_ARR[numStr.charAt(1) - '0'];
        if (numStr.charAt(1) != '0') {
            result += MoneyUtil.LOW_UNIT_ARR[1];
            return result;
        }
        return result;
    }
    
    private static String ftoPctStr(int bit, int num) {
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(bit);
        return percentFormat.format((long)num);
    }
    
    public static String colours(Long num) {
        if (num == null || (long)num <= 0L) {
            return "";
        }
        String msg = "";
        if ((long)num < 10000L) {
            msg = "#<r:255,g:255,b:255,SD:1>";
        }
        else if ((long)num < 100000L) {
            msg = "#<r:160,g:255,b:150,SD:1>";
        }
        else if ((long)num < 1000000L) {
            msg = "#<r:255,g:116,b:255>";
        }
        else if ((long)num < 10000000L) {
            msg = "#<r:255,g:250,b:75,SD:1>";
        }
        else if ((long)num < 100000000L) {
            msg = "#<r:125,g:255,b:255>";
        }
        else if ((long)num < 1000000000L) {
            msg = "#<r:94,g:255,b:94>";
        }
        else {
            msg = "#<r:244,g:183,b:87>";
        }
        return msg + partition(new BigDecimal((long)num)) + "#";
    }
    
    static {
        RMB_ARR = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        UNIT_ARR = new String[] { "", "十", "佰", "仟" };
        HIGH_UINIT_ARR = new String[] { "", "万", "亿" };
        LOW_UNIT_ARR = new String[] { "角", "分" };
    }
}
