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

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAttendance(List<AttendanceEntry> attendanceEntries);

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

    @Query("SELECT date FROM AttendanceIndividual ORDER BY date ASC LIMIT 1")
    Date getFirstDate();

    @Query("SELECT date FROM AttendanceIndividual ORDER BY date DESC LIMIT 1")
    Date getLastDate();

    @Query("SELECT * FROM attendanceindividual WHERE subjectName = :subName AND date <= :endDate AND date >= :startDate")
    List<AttendanceEntry> getDaysForSubject(String subName, Date startDate, Date endDate);

    @Query("DELETE FROM AttendanceIndividual WHERE date > :date")
    void removeAttendanceAfter(Date date);

}
