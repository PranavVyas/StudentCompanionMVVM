package com.vyas.pranav.studentcompanion.data.overallattendancedatabase;

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
    private int totalDays, presentDays, bunkedDays, credits;

    @Ignore
    public OverallAttendanceEntry() {
    }

    public OverallAttendanceEntry(long _ID, String subName, int totalDays, int presentDays, int bunkedDays, int credits) {
        this._ID = _ID;
        this.subName = subName;
        this.totalDays = totalDays;
        this.presentDays = presentDays;
        this.bunkedDays = bunkedDays;
        this.credits = credits;
    }

    @Ignore
    public OverallAttendanceEntry(String subName, int totalDays, int presentDays, int bunkedDays, int credits) {
        this.subName = subName;
        this.totalDays = totalDays;
        this.presentDays = presentDays;
        this.bunkedDays = bunkedDays;
        this.credits = credits;
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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
