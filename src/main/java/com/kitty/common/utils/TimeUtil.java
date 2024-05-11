
package com.kitty.common.utils;

import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.net.URLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class TimeUtil
{
    public static final long ONE_MILLSECOND = 1L;
    public static final long ONE_SECOND = 1000L;
    public static final int ONE_MINUTE_SEC = 60;
    public static final long ONE_MINUTE = 60000L;
    public static final long ONE_HOUR = 3600000L;
    public static final long ONE_DAY = 86400000L;
    public static final int HOURS_OF_DAY = 24;
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static SimpleDateFormat simpleDateFormat;
    
    public static int getDaysOfMonth() {
        final Calendar cl = Calendar.getInstance();
        final int dayCount = cl.getActualMaximum(5);
        return dayCount;
    }
    
    public static String formatDate(final Date date) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
    
    public static String formatDate(final Date date, final String formatPattern) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern);
        return simpleDateFormat.format(date);
    }
    
    public static String getDay(final Date date) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        return simpleDateFormat.format(date);
    }
    
    public static Date parse(final String datestr) {
        return parse(datestr, "yyyy-MM-dd");
    }
    
    public static Date parseTime(final String datestr) {
        return parse(datestr, "HH:mm:ss");
    }
    
    public static Date parse(final String datestr, final String datePattern) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        try {
            return simpleDateFormat.parse(datestr);
        }
        catch (final Exception ex) {
            return null;
        }
    }
    
    public static String getWeekOfDate(final Date dt) {
        final String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        final Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(7) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }
    
    public static int hour(final boolean flag) {
        final Calendar cal = Calendar.getInstance();
        if (flag) {
            return cal.get(11);
        }
        return cal.get(10);
    }
    
    public static int hour() {
        final Calendar cal = Calendar.getInstance();
        return cal.get(11);
    }
    
    public static int minute() {
        final Calendar cal = Calendar.getInstance();
        return cal.get(12);
    }
    
    public static String currentTime() {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String time = format.format(Calendar.getInstance().getTime());
        return time;
    }
    
    public static String year() {
        final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        final int year = c.get(1);
        return year + "";
    }
    
    public static String month() {
        final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        final int year = c.get(1);
        final int month = c.get(2) + 1;
        return year + "-" + month;
    }
    
    public static String when() {
        final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        final int year = c.get(1);
        final int month = c.get(2) + 1;
        final int day = c.get(5);
        return year + "-" + month + "-" + day;
    }
    
    public static String yesterday() {
        final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        c.add(5, -1);
        final int year = c.get(1);
        final int month = c.get(2) + 1;
        final int day = c.get(5);
        return year + "-" + month + "-" + day;
    }
    
    public static String getFirstDayOfMonth1(final int year, final int month) {
        final Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month - 1);
        final int firstDay = cal.getMinimum(5);
        cal.set(5, firstDay);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }
    
    public static String getWeekday(final int day) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar cal = new GregorianCalendar();
        cal.setFirstDayOfWeek(day);
        cal.setTime(new Date());
        cal.set(7, cal.getFirstDayOfWeek());
        final Date first = cal.getTime();
        return format.format(first);
    }
    
    public static int getWeek() {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(7) - 1;
    }
    
    public static String getLastDayOfMonth1(final int year, final int month) {
        final Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month - 1);
        final int lastDay = cal.getActualMaximum(5);
        cal.set(5, lastDay);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }
    
    public static String getFirstDayDateOfMonth(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int last = cal.getActualMinimum(5);
        cal.set(5, last);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }
    
    public static String getLastDayOfMonth(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int last = cal.getActualMaximum(5);
        cal.set(5, last);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }
    
    public static String getFirstDayDateOfYear(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int last = cal.getActualMinimum(6);
        cal.set(6, last);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }
    
    public static String getLastDayOfYear(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int last = cal.getActualMaximum(6);
        cal.set(6, last);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }
    
    public static Date StrToDate(final String type, final String str) {
        final SimpleDateFormat format = new SimpleDateFormat(type);
        Date date = null;
        try {
            date = format.parse(str);
        }
        catch (final ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    public static String subtraction(final Integer time) {
        final Calendar now = Calendar.getInstance();
        now.add(5, -time);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(now.getTime());
    }
    
    public static String addition(final Integer time) {
        final Calendar cal = Calendar.getInstance();
        cal.add(5, time);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(cal.getTime());
    }
    
    public static String temporalAdjusters(final int week) {
        final LocalDate with = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.of(week)));
        return with.toString();
    }
    
    public static String getNetworkTime() {
        try {
            final URL url = new URL("http://www.baidu.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            final long dateL = conn.getDate();
            final Date date = new Date(dateL);
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(date);
        }
        catch (final MalformedURLException e) {
            e.printStackTrace();
            return when();
        }
        catch (final IOException e2) {
            e2.printStackTrace();
            return when();
        }
    }
    
    public static Integer timeCalculate(final Date start, final Date stop) {
        final Calendar cal1 = Calendar.getInstance();
        cal1.setTime(start);
        final Calendar cal_two = Calendar.getInstance();
        cal_two.setTime(stop);
        final long t = cal_two.getTimeInMillis() - cal1.getTimeInMillis();
        final int days = new Long(t / 86400000L).intValue();
        return days;
    }
    
    public static long timeInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }
    
    public static long timeInMillis(final String when) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            final Date parse = formatter.parse(when);
            return parse.getTime();
        }
        catch (final ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }
    
    public static int dayInMillis(final String when) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            final Date parse = formatter.parse(when);
            return Math.toIntExact(parse.getTime() / 1000L);
        }
        catch (final ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public static String stampToDate(final long stamp) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.format(new Date(stamp));
        }
        catch (final Exception e) {
            e.printStackTrace();
            return when();
        }
    }
    
    public static int differentDays(final Date date1, final Date date2) {
        final Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        final Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        final int day1 = cal1.get(6);
        final int day2 = cal2.get(6);
        final int year1 = cal1.get(1);
        final int year2 = cal2.get(1);
        if (year1 != year2) {
            int timeDistance = 0;
            for (int i = year1; i < year2; ++i) {
                if ((i % 4 == 0 && i % 100 != 0) || i % 400 == 0) {
                    timeDistance += 366;
                }
                else {
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        }
        return day2 - day1;
    }
    
    public static long getTimeMills() {
        final LocalDate localDate = LocalDate.now();
        final LocalTime localTime = LocalTime.now();
        return LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), localTime.getHour(), localTime.getMinute(), 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    public static String convertTime(final int time) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(time * 1000L));
    }
    
    static {
        TimeUtil.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
