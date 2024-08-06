package com.code.baseservice.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static String YYYY_MM_DD_HH_MM_SS1 = "yyyy/MM/dd HH:mm:ss";
    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateString = sdf.format(new Date());
        return dateString;
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return new SimpleDateFormat(format).format(new Date());
    }


    public static String format1(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateString = sdf.format(date);
        return dateString;
    }

    public static Date addHour(Date date, int hour){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.HOUR,hour);//日期加10天
        Date dt1=rightNow.getTime();
        return  dt1;
    }

    public static Date addMinute(Date date, int minute){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.MINUTE,minute);//日期加10天
        Date dt1=rightNow.getTime();
        return  dt1;
    }
}
