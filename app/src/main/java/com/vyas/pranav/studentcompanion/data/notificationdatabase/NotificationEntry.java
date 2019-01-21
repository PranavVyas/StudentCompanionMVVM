package com.vyas.pranav.studentcompanion.data.notificationdatabase;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notifications")
public class NotificationEntry {

    @PrimaryKey
    @NonNull
    private long _ID;
    private String title, subtitle, imageUrl;
    private Date date;

    @Ignore
    public NotificationEntry() {
    }

    public NotificationEntry(long _ID, String title, String subtitle, String imageUrl, Date date) {
        this._ID = _ID;
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
        this.date = date;
    }

    @Ignore
    public NotificationEntry(String title, String subtitle, String imageUrl, Date date) {
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
        this.date = date;
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

