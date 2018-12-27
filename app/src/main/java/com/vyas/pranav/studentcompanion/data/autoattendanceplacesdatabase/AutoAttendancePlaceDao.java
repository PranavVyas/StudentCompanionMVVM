package com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AutoAttendancePlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewPlaceId(AutoAttendancePlaceEntry newPlaceEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePlaceId(AutoAttendancePlaceEntry placeEntry);

    @Delete
    void deletePlaceId(AutoAttendancePlaceEntry placeEntry);

    @Query("SELECT * FROM PlacesTable")
    LiveData<List<AutoAttendancePlaceEntry>> getAllPlaceIds();

    @Query("DELETE FROM PlacesTable")
    void deleteAllPlaceIds();

    @Query("SELECT * FROM PlacesTable WHERE subject=:subject")
    LiveData<AutoAttendancePlaceEntry> getPlaceIdOfSubject(String subject);
}
