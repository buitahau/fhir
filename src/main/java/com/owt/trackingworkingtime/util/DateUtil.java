package com.owt.trackingworkingtime.util;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

    public static String convert(Date date) {
        return formatter.format(date);
    }

    public static Date convert(String date) throws ParseException {
        return formatter.parse(date);
    }

    public static Date setZeroSecondAndMillisecond(Date date) {
        Date temp = DateUtils.setSeconds(date, 0);
        return DateUtils.setMilliseconds(temp, 0);
    }
}
