package com.vyas.pranav.studentcompanion.data.lecturedatabase;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Lectures")
public class LectureEntry {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int lectureNo, startTime, stopTime;

    @Ignore
    public LectureEntry(int lectureNo, int startTime, int stopTime) {
        this.lectureNo = lectureNo;
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    @Ignore
    public LectureEntry() {
    }

    public LectureEntry(long id, int lectureNo, int startTime, int stopTime) {
        this.id = id;
        this.lectureNo = lectureNo;
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLectureNo() {
        return lectureNo;
    }

    public void setLectureNo(int lectureNo) {
        this.lectureNo = lectureNo;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getStopTime() {
        return stopTime;
    }

    public void setStopTime(int stopTime) {
        this.stopTime = stopTime;
    }
}
