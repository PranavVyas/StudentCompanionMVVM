package com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "PlacesTable")
public class AutoAttendancePlaceEntry {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long _ID;
    private String placeId, subject;

    @Ignore
    public AutoAttendancePlaceEntry(String placeId, String subject) {
        this.placeId = placeId;
        this.subject = subject;
    }

    @Ignore
    public AutoAttendancePlaceEntry() {
    }

    public AutoAttendancePlaceEntry(@NonNull long _ID, String placeId, String subject) {
        this._ID = _ID;
        this.placeId = placeId;
        this.subject = subject;
    }

    @NonNull
    public long get_ID() {
        return _ID;
    }

    public void set_ID(@NonNull long _ID) {
        this._ID = _ID;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
