package com.vyas.pranav.studentcompanion.data.attendancedatabase;
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

import com.vyas.pranav.studentcompanion.utils.Constants;

import java.util.Date;

@Entity(tableName = "AttendanceIndividual")
public class AttendanceEntry {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long _ID;
    private Date date;
    private int lectureNo;
    private String subjectName;
    private int present = Constants.ABSENT;

    @Ignore
    public AttendanceEntry(Date date, int lectureNo, String subjectName, int present) {
        this.date = date;
        this.lectureNo = lectureNo;
        this.subjectName = subjectName;
        this.present = present;
    }

    public AttendanceEntry(long _ID, Date date, int lectureNo, String subjectName, int present) {
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

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }
}
