package com.vyas.pranav.studentcompanion.data.notedatabase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Notes")
public class NotesEntry {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    private String title, desc;
    private Date date;

    public NotesEntry(long id, String title, Date date, String desc) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.desc = desc;
    }

    @Ignore
    public NotesEntry(String title, Date date, String desc) {
        this.title = title;
        this.date = date;
        this.desc = desc;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
