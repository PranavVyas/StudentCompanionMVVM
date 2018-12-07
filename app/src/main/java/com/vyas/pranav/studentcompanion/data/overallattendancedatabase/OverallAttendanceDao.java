package com.vyas.pranav.studentcompanion.data.overallattendancedatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface OverallAttendanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOverall(OverallAttendanceEntry overallAttendanceEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllOverall(List<OverallAttendanceEntry> overallAttendanceEntries);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateOverall(OverallAttendanceEntry overallAttendanceEntry);

    @Delete
    void deleteOverall(OverallAttendanceEntry overallAttendanceEntry);

    @Query("DELETE FROM OverallAttendance")
    void deleteAllOverall();

    @Query("SELECT * FROM OverallAttendance")
    LiveData<List<OverallAttendanceEntry>> getAllOverallAttendance();
}
