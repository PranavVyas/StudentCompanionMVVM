package com.vyas.pranav.studentcompanion.data;

import java.util.Date;

import androidx.room.TypeConverter;

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
