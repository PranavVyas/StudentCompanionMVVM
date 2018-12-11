package com.vyas.pranav.studentcompanion.data.attendancedatabase;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "AttendanceIndividual")
public class AttendanceEntry {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long _ID;
    private Date date;
    private int lectureNo;
    private String subjectName;
    private boolean present;

    @Ignore
    public AttendanceEntry(Date date, int lectureNo, String subjectName, boolean present) {
        this.date = date;
        this.lectureNo = lectureNo;
        this.subjectName = subjectName;
        this.present = present;
    }

    public AttendanceEntry(long _ID, Date date, int lectureNo, String subjectName, boolean present) {
        this._ID = _ID;
        this.date = date;
        this.lectureNo = lectureNo;
        this.subjectName = subjectName;
        this.present = present;
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLectureNo() {
        return lectureNo;
    }

    public void setLectureNo(int lectureNo) {
        this.lectureNo = lectureNo;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
