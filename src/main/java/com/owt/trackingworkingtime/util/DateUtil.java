package com.owt.trackingworkingtime.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");

    public static String convert(Date date) {
        return formatter.format(date);
    }

    public static Date convert(String date) throws ParseException {
        return formatter.parse(date);
    }
}
