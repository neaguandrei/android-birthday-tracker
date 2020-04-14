package com.aneagu.birthdaytracker.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static Date fromStringToDate(String string) {
        try {
            return simpleDateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static String fromDateToString(Date date) {
        return date == null ? null : simpleDateFormat.format(date);
    }
}
