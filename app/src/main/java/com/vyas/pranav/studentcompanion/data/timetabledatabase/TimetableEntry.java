package com.vyas.pranav.studentcompanion.data.timetabledatabase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "TimeTable")
public class TimetableEntry {
    @PrimaryKey
    @NonNull
    private String _ID;
    private int timeStart, timeEnd;
    private String day;
    private String subName;
    private int lectureNo;

    @Ignore
    public TimetableEntry() {

    }

    public TimetableEntry(String _ID, int timeStart, int timeEnd, String day, String subName, int lectureNo) {
        this._ID = _ID;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.day = day;
        this.subName = subName;
        this.lectureNo = lectureNo;
    }

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public int getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(int timeStart) {
        this.timeStart = timeStart;
    }

    public int getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(int timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public int getLectureNo() {
        return lectureNo;
    }

    public void setLectureNo(int lectureNo) {
        this.lectureNo = lectureNo;
    }
}
