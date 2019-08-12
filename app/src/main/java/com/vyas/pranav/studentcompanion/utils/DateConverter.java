package com.vyas.pranav.studentcompanion.utils;
/*
Student Companion - An Android App that has features like attendance manager, note manager etc
Copyright (C) 2019  Pranav Vyas

This file is a part of Student Companion.

Student Companion is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Student Companion is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.
*/
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
