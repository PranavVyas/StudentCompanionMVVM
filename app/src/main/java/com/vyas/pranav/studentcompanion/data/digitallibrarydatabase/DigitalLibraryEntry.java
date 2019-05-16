package com.vyas.pranav.studentcompanion.data.digitallibrarydatabase;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Digital Library")
public class DigitalLibraryEntry {

    private String bookName, subject, authorName, bookUrl, extraInfo;
    @PrimaryKey(autoGenerate = true)
    private long _ID;

    @Ignore
    public DigitalLibraryEntry() {
    }

    @Ignore
    public DigitalLibraryEntry(String bookName, String subject, String authorName, String bookUrl, String extraInfo) {
        this.bookName = bookName;
        this.subject = subject;
        this.authorName = authorName;
        this.bookUrl = bookUrl;
        this.extraInfo = extraInfo;
    }

    public DigitalLibraryEntry(String bookName, String subject, String authorName, String bookUrl, String extraInfo, long _ID) {
        this.bookName = bookName;
        this.subject = subject;
        this.authorName = authorName;
        this.bookUrl = bookUrl;
        this.extraInfo = extraInfo;
        this._ID = _ID;
    }


    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }
}
