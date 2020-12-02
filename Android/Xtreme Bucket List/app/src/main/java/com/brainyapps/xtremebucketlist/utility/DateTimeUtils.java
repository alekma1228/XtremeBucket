package com.brainyapps.xtremebucketlist.utility;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateTimeUtils {
    /*
     * Time Format for saving
     */
    public static String DATE_TIME_STRING_FORMAT = "MM/dd/yyyy HH:mm:ss";
    public static String DATE_STRING_FORMAT = "MMM dd, yyyy";

    /*
     * Date -> yyyyMMdd
     * Date -> MMdd
     * Date -> yyyy/MM/dd
     * Date -> dd/MM/yyyy
     */
    public static String dateToString(Date date, String strformat) {
        SimpleDateFormat format = new SimpleDateFormat(strformat);
        return format.format(date);
    }

    /*
     * yyyyMMdd -> Date
     * MMdd -> Date
     * yyyy/MM/dd -> Date
     * dd/MM/yyyy -> Date
     */
    public static Date stringToDate(String strDate, String strformat) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat(strformat);
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return date;
    }

    // next week date
    public static Date getWeekDate(int week) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        Date date = new Date(calendar.getTime().getTime() + ((week - day) * 24 * 3600 * 1000));
        return date;
    }

    public static Date getNextMonthDate(Date fromDate, int day) {
        Calendar calendar = Calendar.getInstance();
        int year = fromDate.getYear();
        int month = fromDate.getMonth() + 1;
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static boolean checkValidDate(Date date) {
        Calendar precalendar = Calendar.getInstance();
        precalendar.set(Calendar.YEAR, precalendar.get(Calendar.YEAR) - 1);
        Date preDate = precalendar.getTime();
        Calendar nextcalendar = Calendar.getInstance();
        nextcalendar.set(Calendar.YEAR, nextcalendar.get(Calendar.YEAR) + 1);
        Date nextDate = nextcalendar.getTime();
        if ((date.getTime() >= preDate.getTime()) && (date.getTime() <= nextDate.getTime()))
            return true;
        return false;
    }

    // get age
    public static int getAge(Date birthday) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.setTime(birthday);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        return age;
    }
}

