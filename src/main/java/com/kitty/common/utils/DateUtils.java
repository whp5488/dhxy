package com.kitty.common.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
  public static String YYYY = "yyyy";
  
  public static String YYYY_MM = "yyyy-MM";
  
  public static String YYYY_MM_DD = "yyyy-MM-dd";
  
  public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
  
  public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
  
  private static String[] parsePatterns = new String[] { 
      "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", 
      "yyyy.MM.dd HH:mm", "yyyy.MM" };
  
  public static Date getNowDate() {
    return new Date();
  }
  
  public static String getDate() {
    return dateTimeNow(YYYY_MM_DD);
  }
  
  public static final String getTime() {
    return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
  }
  
  public static final String dateTimeNow() {
    return dateTimeNow(YYYYMMDDHHMMSS);
  }
  
  public static final String dateTimeNow(String format) {
    return parseDateToStr(format, new Date());
  }
  
  public static final String dateTime(Date date) {
    return parseDateToStr(YYYY_MM_DD, date);
  }
  
  public static final String parseDateToStr(String format, Date date) {
    return (new SimpleDateFormat(format)).format(date);
  }
  
  public static final Date dateTime(String format, String ts) {
    try {
      return (new SimpleDateFormat(format)).parse(ts);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    } 
  }
  
  public static final String datePath() {
    Date now = new Date();
    return DateFormatUtils.format(now, "yyyy/MM/dd");
  }
  
  public static final String dateTime() {
    Date now = new Date();
    return DateFormatUtils.format(now, "yyyyMMdd");
  }
  
  public static Date parseDate(Object str) {
    if (str == null)
      return null; 
    try {
      return parseDate(str.toString(), parsePatterns);
    } catch (ParseException e) {
      return null;
    } 
  }
  
  public static Date getServerStartDate() {
    long time = ManagementFactory.getRuntimeMXBean().getStartTime();
    return new Date(time);
  }
  
  public static int differentDaysByMillisecond(Date date1, Date date2) {
    return Math.abs((int)((date2.getTime() - date1.getTime()) / 86400000L));
  }
  
  public static String getDatePoor(Date endDate, Date nowDate) {
    long nd = 86400000L;
    long nh = 3600000L;
    long nm = 60000L;
    long diff = endDate.getTime() - nowDate.getTime();
    long day = diff / nd;
    long hour = diff % nd / nh;
    long min = diff % nd % nh / nm;
    return day + "天" + hour + "小时" + min + "分钟";
  }
  
  public static String dayForWeek(Date tmpDate) {
    Calendar cal = Calendar.getInstance();
    String[] weekDays = { "7", "1", "2", "3", "4", "5", "6" };
    try {
      cal.setTime(tmpDate);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    int w = cal.get(7) - 1;
    if (w < 0)
      w = 0; 
    return weekDays[w];
  }
  
  public static String formatDate(Date date) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    return simpleDateFormat.format(date);
  }
}
