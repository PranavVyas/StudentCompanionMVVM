package com.vyas.pranav.studentcompanion.data.timetabledatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TimetableDao {
    @Query("SELECT * FROM TimeTable WHERE day = :day")
    TimetableEntry getTimetableForDay(String day);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllTimeTableEntry(List<TimetableEntry> newTimetableEntries);

    @Query("DELETE FROM TimeTable")
    void deleteWholeTimetable();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTimetableEntry(TimetableEntry newTimetableEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTimeTableEntry(TimetableEntry newTimetableEntry);

    @Query("SELECT * FROM  TimeTable ORDER BY _ID")
    LiveData<List<TimetableEntry>> getFullTimetable();

    @Delete
    void deleteTimetableEntry(TimetableEntry timetableEntry);
}
