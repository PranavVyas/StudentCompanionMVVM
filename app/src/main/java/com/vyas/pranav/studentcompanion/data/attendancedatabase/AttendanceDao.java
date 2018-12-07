package com.vyas.pranav.studentcompanion.data.attendancedatabase;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllAttendance(List<AttendanceEntry> attendanceEntries);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAttendance(AttendanceEntry attendanceEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAttendance(AttendanceEntry attendanceEntry);

    @Delete
    void deleteAttendance(AttendanceEntry attendanceEntry);

    @Query("SELECT * FROM AttendanceIndividual")
    LiveData<List<AttendanceEntry>> getAllAttendance();

    @Query("SELECT * FROM AttendanceIndividual WHERE date = :date ORDER BY _ID")
    LiveData<List<AttendanceEntry>> getAttendanceForDate(Date date);

    @Query("DELETE FROM AttendanceIndividual")
    void deleteAll();
}
