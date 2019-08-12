package com.vyas.pranav.studentcompanion.data.holidaydatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HolidayDao {
    @Query("SELECT * FROM Holidays ORDER BY date")
    LiveData<List<HolidayEntry>> getAllHolidays();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllHolidays(List<HolidayEntry> holidays);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHoliday(HolidayEntry newHoliday);

}
