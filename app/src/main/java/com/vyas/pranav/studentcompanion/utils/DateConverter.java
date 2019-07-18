package com.vyas.pranav.studentcompanion.utils;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    /**
     * To time stamp long.
     *
     * @param date the date
     * @return the long
     */
    @TypeConverter
    public static Long toTimeStamp(Date date) {
        return (date == null) ? null : date.getTime();
    }

    /**
     * To date date.
     *
     * @param timestamp the timestamp
     * @return the date
     */
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return (timestamp == null) ? null : new Date(timestamp);
    }
}
