package com.vyas.pranav.studentcompanion.data.notificationdatabase.firestore;
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

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.vyas.pranav.studentcompanion.utils.Constants;

@IgnoreExtraProperties
@Entity(tableName = "Notifications")
public class NotificationFirestoreModel {

    private String name, short_info, image_url;
    @PrimaryKey
    @NonNull
    private String _ID = "DEFAULT_ID";
    private String url = "";
    private String venue = "";
    private String dateInMillis;
    private int type = Constants.NOTI_TYPE_EVENT;

    @Ignore
    public NotificationFirestoreModel() {
    }

    @Ignore
    public NotificationFirestoreModel(String name, String short_info, String image_url, String url, String venue, String dateInMillis, int type) {
        this.name = name;
        this.short_info = short_info;
        this.image_url = image_url;
        this.url = url;
        this.venue = venue;
        this.dateInMillis = dateInMillis;
        this.type = type;
    }

    public NotificationFirestoreModel(String name, String short_info, String image_url, String _ID, String url, String venue, String dateInMillis, int type) {
        this.name = name;
        this.short_info = short_info;
        this.image_url = image_url;
        this._ID = _ID;
        this.url = url;
        this.venue = venue;
        this.dateInMillis = dateInMillis;
        this.type = type;
    }

    @Exclude
    public String get_ID() {
        return _ID;
    }

    @Exclude
    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateInMillis() {
        return dateInMillis;
    }

    public void setDateInMillis(String dateInMillis) {
        this.dateInMillis = dateInMillis;
    }

    public String getShort_info() {
        return short_info;
    }

    public void setShort_info(String short_info) {
        this.short_info = short_info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
