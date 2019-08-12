package com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase;
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

@Entity(tableName = "PlacesTable")
public class AutoAttendancePlaceEntry {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long _ID;
    private String subject;
    private double lat, lang;


    @Ignore
    public AutoAttendancePlaceEntry(String subject, double lat, double lang) {
        this.subject = subject;
        this.lat = lat;
        this.lang = lang;
    }


    @Ignore
    public AutoAttendancePlaceEntry() {
    }

    public long get_ID() {
        return _ID;
    }

    public AutoAttendancePlaceEntry(long _ID, String subject, double lat, double lang) {
        this._ID = _ID;
        this.subject = subject;
        this.lat = lat;
        this.lang = lang;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }
}
