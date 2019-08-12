package com.vyas.pranav.studentcompanion.data.notificationdatabase;
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
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

public class NotificationEntry {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long _ID;
    private String name, date, short_info;
    private String url = "";
    private String venue = "";

    @Ignore
    public NotificationEntry() {
    }

    public NotificationEntry(long _ID, String name, String url, String date, String short_info, String venue) {
        this._ID = _ID;
        this.name = name;
        this.url = url;
        this.date = date;
        this.short_info = short_info;
        this.venue = venue;
    }

    @Ignore
    public NotificationEntry(String name, String url, String date, String short_info, String venue) {
        this.name = name;
        this.url = url;
        this.date = date;
        this.short_info = short_info;
        this.venue = venue;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShort_info() {
        return short_info;
    }

    public void setShort_info(String short_info) {
        this.short_info = short_info;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}

