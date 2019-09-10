package com.vyas.pranav.studentcompanion.services;
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
import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.repositories.AttendanceDatabaseRepository;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.Date;
import java.util.List;

public class AttendanceEditIntentService extends IntentService {

    private static final String TAG = "AttendanceEditIntentSer";

    public AttendanceEditIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.cancel(Constants.SHOW_REMINDER_JOB_RC_SHOW_NOTIFICATION);
        AttendanceDatabaseRepository repository = new AttendanceDatabaseRepository(getApplicationContext());
        Date date = new Date();
        ConverterUtils.CustomDate customDate = ConverterUtils.extractElementsFromDate(date);
        Date date1 = ConverterUtils.formatDateFromCalender(customDate.getDate(), customDate.getMonth(), customDate.getYear());
        LiveData<List<AttendanceEntry>> attendanceForDate = repository.getAttendanceForDate(date1);
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                attendanceForDate.observeForever(new Observer<List<AttendanceEntry>>() {
                    @Override
                    public void onChanged(List<AttendanceEntry> attendanceEntries) {
                        attendanceForDate.removeObserver(this);
                        Logger.d("Marking all present now");
                        for (int i = 0; i < attendanceEntries.size(); i++) {
                            AttendanceEntry attendanceEntry = attendanceEntries.get(i);
                            if (attendanceEntry.getPresent() != Constants.PRESENT) {
                                attendanceEntry.setPresent(Constants.PRESENT);
                            }
                            repository.updateAttendance(attendanceEntry);
                        }
                    }
                });
            }
        });
    }
}
