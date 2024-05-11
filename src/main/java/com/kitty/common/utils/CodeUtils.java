
package com.kitty.common.utils;

import org.apache.commons.lang3.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeUtils
{
    private static final String CHINA_TELECOM_PATTERN = "(?:^(?:\\+86)?1(?:33|49|53|7[37]|8[019]|9[19])\\d{8}$)|(?:^(?:\\+86)?1349\\d{7}$)|(?:^(?:\\+86)?1410\\d{7}$)|(?:^(?:\\+86)?170[0-2]\\d{7}$)";
    private static final String CHINA_UNICOM_PATTERN = "(?:^(?:\\+86)?1(?:3[0-2]|4[56]|5[56]|66|7[156]|8[56])\\d{8}$)|(?:^(?:\\+86)?170[47-9]\\d{7}$)";
    private static final String CHINA_MOBILE_PATTERN = "(?:^(?:\\+86)?1(?:3[4-9]|4[78]|5[0-27-9]|72|78|8[2-478]|98|95)\\d{8}$)|(?:^(?:\\+86)?1440\\d{7}$)|(?:^(?:\\+86)?170[356]\\d{7}$)";
    
    public static boolean verificationPhone(final String phoneNo) {
        final Pattern pattern = Pattern.compile("^((17[0-1])|(16[2,5,7]))\\d{8}$");
        final Matcher matcher = pattern.matcher(phoneNo);
        return matcher.matches();
    }
    
    public static boolean checkPhone(final String phone) {
        return StringUtils.isNotBlank((CharSequence)phone) && (checkChinaMobile(phone) || checkChinaUnicom(phone) || checkChinaTelecom(phone));
    }
    
    public static boolean checkChinaMobile(final String phone) {
        if (StringUtils.isNotBlank((CharSequence)phone)) {
            final Pattern regexp = Pattern.compile("(?:^(?:\\+86)?1(?:3[4-9]|4[78]|5[0-27-9]|72|78|8[2-478]|98|95)\\d{8}$)|(?:^(?:\\+86)?1440\\d{7}$)|(?:^(?:\\+86)?170[356]\\d{7}$)");
            if (regexp.matcher(phone).matches()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean checkChinaUnicom(final String phone) {
        if (StringUtils.isNotBlank((CharSequence)phone)) {
            final Pattern regexp = Pattern.compile("(?:^(?:\\+86)?1(?:3[0-2]|4[56]|5[56]|66|7[156]|8[56])\\d{8}$)|(?:^(?:\\+86)?170[47-9]\\d{7}$)");
            if (regexp.matcher(phone).matches()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean checkChinaTelecom(final String phone) {
        if (StringUtils.isNotBlank((CharSequence)phone)) {
            final Pattern regexp = Pattern.compile("(?:^(?:\\+86)?1(?:33|49|53|7[37]|8[019]|9[19])\\d{8}$)|(?:^(?:\\+86)?1349\\d{7}$)|(?:^(?:\\+86)?1410\\d{7}$)|(?:^(?:\\+86)?170[0-2]\\d{7}$)");
            if (regexp.matcher(phone).matches()) {
                return true;
            }
        }
        return false;
    }
    
    public static String nameMask(final String name) {
        String res = "";
        if (name.length() == 2) {
            res = name.replaceAll("^.", "*");
        }
        else if (name.length() > 2) {
            res = name.replaceAll("(?<=.).(?=.)", "*");
        }
        return res;
    }
    
    public static String idCardMask(final String idCardNum) {
        String res = "";
        if (!StringUtils.isEmpty((CharSequence)idCardNum)) {
            final StringBuilder stringBuilder = new StringBuilder(idCardNum);
            res = stringBuilder.replace(6, 14, "********").toString();
        }
        return res;
    }
    
    public static String phoneMask(final String phone) {
        String res = "";
        if (!StringUtils.isEmpty((CharSequence)phone)) {
            final StringBuilder stringBuilder = new StringBuilder(phone);
            res = stringBuilder.replace(3, 7, "****").toString();
        }
        return res;
    }
}
