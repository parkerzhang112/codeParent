package com.code.payapi.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String YYYY_MM_DD = "yyyyMMDD 00:00:00";

    public String format(Date date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateString = sdf.format(new Date());
        return  dateString;
    }
}
