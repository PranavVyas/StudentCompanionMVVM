package com.vyas.pranav.studentcompanion.data.timetabledatabase;
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
public interface TimetableDao {
    @Query("SELECT * FROM TimeTable WHERE day = :day")
    LiveData<List<TimetableEntry>> getTimetableForDay(String day);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllTimeTableEntry(List<TimetableEntry> newTimetableEntries);

    @Query("DELETE FROM TimeTable")
    void deleteWholeTimetable();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTimetableEntry(TimetableEntry newTimetableEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTimeTableEntry(TimetableEntry newTimetableEntry);

    @Query("SELECT * FROM TimeTable ORDER BY _ID")
    LiveData<List<TimetableEntry>> getFullTimetable();

    @Delete
    void deleteTimetableEntry(TimetableEntry timetableEntry);

    @Query("SELECT * FROM TimeTable WHERE subName = :subject")
    LiveData<List<TimetableEntry>> getTimetableForSubject(String subject);
}
