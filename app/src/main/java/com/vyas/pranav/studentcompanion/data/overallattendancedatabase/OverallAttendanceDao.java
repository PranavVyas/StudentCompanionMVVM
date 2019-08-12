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

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

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

    @Query("SELECT * FROM OverallAttendance WHERE subName = :subName")
    LiveData<OverallAttendanceEntry> getOverallAttendanceForSubject(String subName);
}
