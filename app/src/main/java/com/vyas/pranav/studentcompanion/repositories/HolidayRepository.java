package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayDao;
import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayDatabase;
import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

import java.util.List;

import androidx.lifecycle.LiveData;

public class HolidayRepository {
    private HolidayDao holidayDao;
    private AppExecutors mExecutors;

    public HolidayRepository(Context context) {
        this.holidayDao = HolidayDatabase.getInstance(context).holidayDao();
        mExecutors = AppExecutors.getInstance();
    }

    public LiveData<List<HolidayEntry>> getAllHolidays() {
        return holidayDao.getAllHolidays();
    }

    public void setHoliday(HolidayEntry holidayEntry) {
        holidayDao.insertHoliday(holidayEntry);
    }

    public void setHolidays(final List<HolidayEntry> holidayEntries) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                holidayDao.insertAllHolidays(holidayEntries);
            }
        });
    }

}
