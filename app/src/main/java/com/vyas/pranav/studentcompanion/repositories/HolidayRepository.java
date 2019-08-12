package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayDao;
import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayEntry;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

import java.util.List;

public class HolidayRepository {
    private final HolidayDao holidayDao;
    private final AppExecutors mExecutors;
    public static final Object LOCK = new Object();
    private static HolidayRepository instance;

    public static HolidayRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new HolidayRepository(context.getApplicationContext());
            }
        }
        return instance;
    }

    public HolidayRepository(Context context) {
        this.holidayDao = MainDatabase.getInstance(context).holidayDao();
        mExecutors = AppExecutors.getInstance();
    }

    public LiveData<List<HolidayEntry>> getAllHolidays() {
        return holidayDao.getAllHolidays();
    }

    public void setHoliday(HolidayEntry holidayEntry) {
        holidayDao.insertHoliday(holidayEntry);
    }

    public void setHolidays(final List<HolidayEntry> holidayEntries) {
        mExecutors.diskIO().execute(() -> holidayDao.insertAllHolidays(holidayEntries));
    }

}
