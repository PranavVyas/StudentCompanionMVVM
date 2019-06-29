package com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase;

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
}
