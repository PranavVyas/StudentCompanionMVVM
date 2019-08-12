package com.vyas.pranav.studentcompanion.data.holidaydatabase;
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

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Holidays")
public class HolidayEntry {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long _ID;
    private String name, day;
    private Date date;

    public HolidayEntry(long _ID, String name, String day, Date date) {
        this._ID = _ID;
        this.name = name;
        this.day = day;
        this.date = date;
    }

    @Ignore
    public HolidayEntry(String name, String day, Date date) {
        this.name = name;
        this.day = day;
        this.date = date;
    }

    @Ignore
    public HolidayEntry() {
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
