package com.vyas.pranav.studentcompanion.data.attendancedatabase;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "AttendanceIndividual")
public class AttendanceEntry {
    @PrimaryKey
    @NonNull
    private String _ID;
    private Date date;
    private int lectureNo;
    private String subjectName;
    private String facultyName;
    private boolean present;

    @Ignore
    public AttendanceEntry(Date date, int lectureNo, String subjectName, String facultyName, boolean present) {
        this.date = date;
        this.lectureNo = lectureNo;
        this.subjectName = subjectName;
        this.facultyName = facultyName;
        this.present = present;
    }

    public AttendanceEntry(String _ID, Date date, int lectureNo, String subjectName, String facultyName, boolean present) {
        this._ID = _ID;
        this.date = date;
        this.lectureNo = lectureNo;
        this.subjectName = subjectName;
        this.facultyName = facultyName;
        this.present = present;
    }

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
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

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
