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

import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableDao;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

import java.util.List;

public class TimetableRepository {

    private TimetableDao timetableDao;
    private AppExecutors mExecutors;
    private Context context;
    private static final Object LOCK = new Object();

    private static TimetableRepository instance;

    public static TimetableRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new TimetableRepository(context.getApplicationContext());
            }
        }
        return instance;
    }

    public TimetableRepository(Context context) {
        this.context = context;
        this.timetableDao = MainDatabase.getInstance(context).timetableDao();
        mExecutors = AppExecutors.getInstance();
    }

    public void insertTimetable(final TimetableEntry timetableEntry) {
        mExecutors.diskIO().execute(() -> timetableDao.insertTimeTableEntry(timetableEntry));
    }

    public void insertTimetable(final List<TimetableEntry> timetableEntries) {
        mExecutors.diskIO().execute(() -> timetableDao.insertAllTimeTableEntry(timetableEntries));
    }

    public LiveData<List<TimetableEntry>> getFullTimetable() {
        return timetableDao.getFullTimetable();
    }

    public LiveData<List<TimetableEntry>> getTimetableForDay(String day) {
        return timetableDao.getTimetableForDay(day);
    }

    public List<TimetableEntry> getFullTimetableMainThread() {
        return timetableDao.getFullTimetableMainThread();
    }
}
