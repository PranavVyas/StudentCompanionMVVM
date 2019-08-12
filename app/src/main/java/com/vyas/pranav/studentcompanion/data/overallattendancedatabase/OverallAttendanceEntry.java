package com.vyas.pranav.studentcompanion.data.overallattendancedatabase;
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

@Entity(tableName = "OverallAttendance")
public class OverallAttendanceEntry {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long _ID;
    private String subName;
    private int totalDays, presentDays, bunkedDays;

    @Ignore
    public OverallAttendanceEntry() {
    }

    public OverallAttendanceEntry(long _ID, String subName, int totalDays, int presentDays, int bunkedDays) {
        this._ID = _ID;
        this.subName = subName;
        this.totalDays = totalDays;
        this.presentDays = presentDays;
        this.bunkedDays = bunkedDays;
    }

    @Ignore
    public OverallAttendanceEntry(String subName, int totalDays, int presentDays, int bunkedDays, int credits) {
        this.subName = subName;
        this.totalDays = totalDays;
        this.presentDays = presentDays;
        this.bunkedDays = bunkedDays;
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public int getPresentDays() {
        return presentDays;
    }

    public void setPresentDays(int presentDays) {
        this.presentDays = presentDays;
    }

    public int getBunkedDays() {
        return bunkedDays;
    }

    public void setBunkedDays(int bunkedDays) {
        this.bunkedDays = bunkedDays;
    }
}
