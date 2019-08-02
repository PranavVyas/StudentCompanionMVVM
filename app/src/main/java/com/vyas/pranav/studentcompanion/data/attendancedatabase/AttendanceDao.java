package com.vyas.pranav.studentcompanion.data.attendancedatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

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

    @Query("SELECT * FROM AttendanceIndividual WHERE date = :date AND NOT subjectName = 'No Lecture' ORDER BY _ID")
    LiveData<List<AttendanceEntry>> getAttendanceForDate(Date date);

    @Query("DELETE FROM AttendanceIndividual")
    void deleteAll();

    @Query("SELECT COUNT(_ID) FROM attendanceindividual WHERE subjectName = :subName")
    int getTotalDaysForSubject(String subName);

    @Query("SELECT COUNT(_ID) FROM attendanceindividual WHERE subjectName = :subName AND present = 1 AND date <= :endDate AND date >= :startDate")
    int getAttendedDaysForSubject(String subName, Date startDate, Date endDate);

    @Query("SELECT COUNT(_ID) FROM AttendanceIndividual WHERE subjectName = :subName AND present = 0 AND date <= :endDate AND date >= :startDate")
    int getBunkedDaysForSubject(String subName, Date startDate, Date endDate);

}
