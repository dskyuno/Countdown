package com.project.timemanagerment.base.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
    private DateFormatUtil() {
    }

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat formatYMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat formatYMDNoZero = new SimpleDateFormat("yyyy-M-d");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat f = new SimpleDateFormat();

    public static String getFormatYMD(Long dateTime) {
        Date date = new Date(dateTime);
        return formatYMD.format(date);
    }

    public static String getFormatYMDNoZero(Long dateTime) {
        Date date = new Date(dateTime);
        return formatYMDNoZero.format(date);
    }

    public static String getFormatYMDHMS(Long dateTime) {
        Date date = new Date(dateTime);
        return formatYMDHMS.format(date);
    }

    public static long getLongByStringYMD(String formatString) {
        long i = 0;
        try {
            Date parse = formatYMD.parse(formatString);
            i = parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static long getLongByStringYMDHMS(String formatString) {
        long i = 0;
        try {
            Date parse = formatYMDHMS.parse(formatString);
            i = parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return i;
    }


}
