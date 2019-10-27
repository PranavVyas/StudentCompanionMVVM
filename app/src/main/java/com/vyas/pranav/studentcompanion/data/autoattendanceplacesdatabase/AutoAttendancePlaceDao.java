package com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase;
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
public interface AutoAttendancePlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewPlaceEntry(AutoAttendancePlaceEntry newPlaceEntry);

    @Delete
    void deletePlaceEntry(AutoAttendancePlaceEntry placeEntry);

    @Query("SELECT * FROM PlacesTable")
    LiveData<List<AutoAttendancePlaceEntry>> getAllPlaceEntries();

    @Query("DELETE FROM PlacesTable")
    void deleteAllPlaceEntries();

    @Query("SELECT * FROM PlacesTable WHERE subject=:subject")
    LiveData<AutoAttendancePlaceEntry> getPlaceEntryOfSubject(String subject);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePlaceEntry(AutoAttendancePlaceEntry placeEntry);

    @Query("DELETE FROM PlacesTable WHERE subject =:deleteSub")
    void deletePlaceEntry(String deleteSub);
}
