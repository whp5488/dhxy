
package com.kitty.common.utils;

import java.util.Calendar;
import java.lang.management.ManagementFactory;
import org.apache.commons.lang3.time.DateFormatUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY;
    public static String YYYY_MM;
    public static String YYYY_MM_DD;
    public static String YYYYMMDDHHMMSS;
    public static String YYYY_MM_DD_HH_MM_SS;
    private static String[] parsePatterns;
    
    public static Date getNowDate() {
        return new Date();
    }
    
    public static String getDate() {
        return dateTimeNow(DateUtils.YYYY_MM_DD);
    }
    
    public static final String getTime() {
        return dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
    }
    
    public static final String dateTimeNow() {
        return dateTimeNow(DateUtils.YYYYMMDDHHMMSS);
    }
    
    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }
    
    public static final String dateTime(final Date date) {
        return parseDateToStr(DateUtils.YYYY_MM_DD, date);
    }
    
    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }
    
    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (final ParseException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static final String datePath() {
        final Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }
    
    public static final String dateTime() {
        final Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }
    
    public static Date parseDate(final Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), DateUtils.parsePatterns);
        }
        catch (final ParseException e) {
            return null;
        }
    }
    
    public static Date getServerStartDate() {
        final long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }
    
    public static int differentDaysByMillisecond(final Date date1, final Date date2) {
        return Math.abs((int)((date2.getTime() - date1.getTime()) / 86400000L));
    }
    
    public static String getDatePoor(final Date endDate, final Date nowDate) {
        final long nd = 86400000L;
        final long nh = 3600000L;
        final long nm = 60000L;
        final long diff = endDate.getTime() - nowDate.getTime();
        final long day = diff / nd;
        final long hour = diff % nd / nh;
        final long min = diff % nd % nh / nm;
        return day + "天" + hour + "小时" + min + "分钟";
    }
    
    public static String dayForWeek(final Date tmpDate) {
        final Calendar cal = Calendar.getInstance();
        final String[] weekDays = { "7", "1", "2", "3", "4", "5", "6" };
        try {
            cal.setTime(tmpDate);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        int w = cal.get(7) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }
    
    public static String formatDate(final Date date) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);
    }
    
    static {
        DateUtils.YYYY = "yyyy";
        DateUtils.YYYY_MM = "yyyy-MM";
        DateUtils.YYYY_MM_DD = "yyyy-MM-dd";
        DateUtils.YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
        DateUtils.YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        DateUtils.parsePatterns = new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM" };
    }
}
