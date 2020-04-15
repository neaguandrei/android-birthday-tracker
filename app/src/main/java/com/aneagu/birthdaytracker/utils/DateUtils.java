package com.aneagu.birthdaytracker.utils;

import android.annotation.SuppressLint;

import com.aneagu.birthdaytracker.data.repository.local.Birthday;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

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

    public static String findDaysLeft(Birthday birthday) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date birthDate = DateUtils.fromStringToDate(birthday.getDate());

        LocalDate fromDate = LocalDate.now();
        LocalDate untilDate = birthDate.toInstant().atZone(defaultZoneId).toLocalDate().withYear(fromDate.getYear());

        long daysNumber = Duration.between(fromDate.atStartOfDay(), untilDate.atStartOfDay()).toDays();
        if (untilDate.isBefore(fromDate)) {
            daysNumber = fromDate.lengthOfYear() + daysNumber;
        }

        if (daysNumber == 0) {
            return "Today";
        } else if (daysNumber == 1) {
            return "Tomorrow";
        }

        return "In " + daysNumber + " days";
    }
}
