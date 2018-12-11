package com.vyas.pranav.studentcompanion.data.holidaydatabase;

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
public interface HolidayDao {
    @Query("SELECT * FROM Holidays ORDER BY date")
    LiveData<List<HolidayEntry>> getAllHolidays();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllHolidays(List<HolidayEntry> holidays);

    @Query("SELECT date FROM holidays")
    List<Date> getAllDates();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHoliday(HolidayEntry newHoliday);

    @Delete
    void deleteHoliday(HolidayEntry holidayToDelete);

    @Query("DELETE FROM Holidays")
    void deleteAllHolidays();

    @Query("SELECT * FROM Holidays WHERE day = :holidayDay")
    List<HolidayEntry> getHolidaysByDay(String holidayDay);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateHoliday(HolidayEntry newHoliday);
}
