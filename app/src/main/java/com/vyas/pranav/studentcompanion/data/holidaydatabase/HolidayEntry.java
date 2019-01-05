package com.vyas.pranav.studentcompanion.data.holidaydatabase;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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
