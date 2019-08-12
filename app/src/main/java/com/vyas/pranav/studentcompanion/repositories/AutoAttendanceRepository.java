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

import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceDao;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

public class AutoAttendanceRepository {

    private final Context context;
    private final AutoAttendancePlaceDao autoAttendancePlaceDao;
    private final AppExecutors mExecutors;
    private static final Object LOCK = new Object();
    private static AutoAttendanceRepository instance;

    public static AutoAttendanceRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new AutoAttendanceRepository(context.getApplicationContext());
            }
        }
        return instance;
    }
    public AutoAttendanceRepository(Context context) {
        this.context = context.getApplicationContext();
        autoAttendancePlaceDao = MainDatabase.getInstance(context).autoAttendancePlaceDao();
        mExecutors = AppExecutors.getInstance();
    }

    public void updatePlaceEntry(AutoAttendancePlaceEntry placeEntry) {
        mExecutors.diskIO().execute(() -> autoAttendancePlaceDao.updatePlaceEntry(placeEntry));
    }

    public void insertPlaceEntry(AutoAttendancePlaceEntry placeEntry) {
        mExecutors.diskIO().execute(() -> autoAttendancePlaceDao.insertNewPlaceEntry(placeEntry));
    }

    public LiveData<AutoAttendancePlaceEntry> getPlaceEntryOfSubject(String subject) {
        return autoAttendancePlaceDao.getPlaceEntryOfSubject(subject);
    }

}
