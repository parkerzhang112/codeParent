package com.code.baseservice.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYY_MM_DD_HH_MM_SS2 = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static String YYYY_MM_DD_HH_MM_SS1 = "yyyy/MM/dd HH:mm:ss";


    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateString = sdf.format(new Date());
        return dateString;
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
