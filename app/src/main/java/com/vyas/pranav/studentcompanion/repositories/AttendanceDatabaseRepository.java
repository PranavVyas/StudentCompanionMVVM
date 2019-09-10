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

import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

import java.util.Date;
import java.util.List;

public class AttendanceDatabaseRepository {

    private final AttendanceDao attendanceDao;
    private final AppExecutors mExecutors;
//    private static final Object LOCK = new Object();
//    private static AttendanceDatabaseRepository instance;

    //    public static AttendanceDatabaseRepository getInstance(Context context) {
//        if (instance == null) {
//            synchronized (LOCK) {
//                instance = new AttendanceDatabaseRepository(context.getApplicationContext());
//            }
//        }
//        return instance;
//    }
    public AttendanceDatabaseRepository(Context context) {
        attendanceDao = MainDatabase.getInstance(context).attendanceDao();
        mExecutors = AppExecutors.getInstance();
    }

    public AttendanceDatabaseRepository(AttendanceDao attendanceDao) {
        this.attendanceDao = attendanceDao;
        mExecutors = AppExecutors.getInstance();
    }

    public AttendanceDao getAttendanceDao() {
        return attendanceDao;
    }

    public LiveData<List<AttendanceEntry>> getAttendanceForDate(Date date) {
        return attendanceDao.getAttendanceForDate(date);
    }

    public void deleteAttendance(final AttendanceEntry attendanceEntry) {
        mExecutors.diskIO().execute(() -> {
            attendanceDao.deleteAttendance(attendanceEntry);
        });
    }

    public void deleteAllAttendance() {
        mExecutors.diskIO().execute(() -> attendanceDao.deleteAll());
    }

    public void updateAttendance(final AttendanceEntry attendanceEntry) {
        mExecutors.diskIO().execute(() -> attendanceDao.updateAttendance(attendanceEntry));
    }

    public void insertAllAttendanceAndOverallAttendance(final List<AttendanceEntry> attendanceEntries, final Context context) {
        mExecutors.diskIO().execute(() -> {
            for (int i = 0; i < attendanceEntries.size(); i++) {
                attendanceDao.insertAttendance(attendanceEntries.get(i));
            }
            SetUpProcessRepository repo = new SetUpProcessRepository(context);
            repo.initializeOverallAttendance();
        });
    }
}
