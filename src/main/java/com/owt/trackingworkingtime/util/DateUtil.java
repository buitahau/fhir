package com.owt.trackingworkingtime.util;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final SimpleDateFormat DATE_TIME_ZONE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

    private static final SimpleDateFormat ONLY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat ONLY_ZONE_FORMAT = new SimpleDateFormat("z");

    public static String convert(Date date) {
        return DATE_TIME_ZONE_FORMAT.format(date);
    }

    public static Date convert(String date) throws ParseException {
        return DATE_TIME_ZONE_FORMAT.parse(date);
    }

    public static Date setZeroSecondAndMillisecond(Date date) {
        Date temp = DateUtils.setSeconds(date, 0);
        return DateUtils.setMilliseconds(temp, 0);
    }

    public static String getOnlyDate(Date date) {
        return ONLY_DATE_FORMAT.format(date);
    }

    public static String getOnlyZone(Date date) {
        return ONLY_ZONE_FORMAT.format(date);
    }
}
