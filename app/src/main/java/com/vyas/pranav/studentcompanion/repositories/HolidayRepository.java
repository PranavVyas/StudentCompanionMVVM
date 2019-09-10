package com.vyas.pranav.studentcompanion.repositories;
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
//    public static final Object LOCK = new Object();
//    private static HolidayRepository instance;

    //    public static HolidayRepository getInstance(Context context) {
//        if (instance == null) {
//            synchronized (LOCK) {
//                instance = new HolidayRepository(context.getApplicationContext());
//            }
//        }
//        return instance;
//    }
//
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
