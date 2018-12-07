package com.vyas.pranav.studentcompanion.data.overallattendancedatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface OverallAttendanceDao {

    @Insert
    void insertOverall(OverallAttendanceEntry overallAttendanceEntry);

    @Insert
    void insertAllOverall(List<OverallAttendanceEntry> overallAttendanceEntries);

    @Update
    void updateOverall(OverallAttendanceEntry overallAttendanceEntry);

    @Delete
    void deleteOverall(OverallAttendanceEntry overallAttendanceEntry);

    @Query("DELETE FROM OverallAttendance")
    void deleteAllOverall();

    @Query("SELECT * FROM OverallAttendance")
    LiveData<List<OverallAttendanceEntry>> getAllOverallAttendance();
}
