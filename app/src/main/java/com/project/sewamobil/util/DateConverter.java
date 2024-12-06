package com.project.sewamobil.util;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @TypeConverter
    public static Date fromTimestamp(String value) {
        try {
            return value == null ? null : sdf.parse(value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String dateToTimestamp(Date date) {
        return date == null ? null : sdf.format(date);
    }
}
