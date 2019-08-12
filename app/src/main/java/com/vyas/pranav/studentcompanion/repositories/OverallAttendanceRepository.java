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
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.ui.activities.SignInActivity;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.AttendanceUtils;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.MainApp;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.util.Date;
import java.util.List;

public class OverallAttendanceRepository {

    private final OverallAttendanceDao overallAttendanceDao;
    private final AttendanceDao attendanceDao;
    private final AppExecutors mExecutors;
    private static final Object LOCK = new Object();
    private static OverallAttendanceRepository instance;
    private final Context applicationContext;

    public OverallAttendanceRepository(Context context) {
        this.overallAttendanceDao = MainDatabase.getInstance(context).overallAttendanceDao();
        this.attendanceDao = MainDatabase.getInstance(context).attendanceDao();
        this.mExecutors = AppExecutors.getInstance();
        this.applicationContext = context;
    }

    public static OverallAttendanceRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new OverallAttendanceRepository(context);
            }
        }
        return instance;
    }

    public LiveData<List<OverallAttendanceEntry>> getAllOverallAttendance() {
        return overallAttendanceDao.getAllOverallAttendance();
    }

    public void insertOverallAttendance(final OverallAttendanceEntry overallAttendanceEntry) {
        mExecutors.diskIO().execute(() -> overallAttendanceDao.insertOverall(overallAttendanceEntry));
    }

    public void updateOverallAttendance(final OverallAttendanceEntry overallAttendanceEntry) {
        mExecutors.diskIO().execute(() -> overallAttendanceDao.updateOverall(overallAttendanceEntry));
    }

    public void deleteOverallAttendance(final OverallAttendanceEntry overallAttendanceEntry) {
        mExecutors.diskIO().execute(() -> overallAttendanceDao.deleteOverall(overallAttendanceEntry));
    }

    public void deleteAllOverallAttendance() {
        mExecutors.diskIO().execute(() -> overallAttendanceDao.deleteAllOverall());
    }

    public void refreshAllOverallAttendance() {
        if (applicationContext == null) {
            Logger.d("Context empty in refreshOverallAttendance");
        }
        SharedPreferencesUtils sharedPreferencesUtils = SharedPreferencesUtils.getInstance(applicationContext.getApplicationContext());
        List<String> subList = sharedPreferencesUtils.getSubjectList();
        for (int i = 0; i < subList.size(); i++) {
            String subject = subList.get(i);
            refreshOverallAttendanceForSubject(subject);
        }
    }

    public void refreshOverallAttendanceForSubject(final String subjectName) {
        if (subjectName.equals("No Lecture")) {
            return;
        }
        mExecutors.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                sendNotification(applicationContext, "Refreshing", "Attendance is Refreshing,\nPlease wait...", getContentIntent());
                final LiveData<OverallAttendanceEntry> overallAttendance = overallAttendanceDao.getOverallAttendanceForSubject(subjectName);
                overallAttendance.observeForever(new Observer<OverallAttendanceEntry>() {
                    @Override
                    public void onChanged(final OverallAttendanceEntry overallAttendanceEntry) {
                        overallAttendance.removeObserver(this);
                        SetUpProcessRepository setUpProcessRepository = SetUpProcessRepository.getInstance(applicationContext);
                        final Date startDate = ConverterUtils.convertStringToDate(setUpProcessRepository.getStartingDate());
                        mExecutors.diskIO().execute(() -> {
                            Date todayDate = new Date();
                            int presentDays = attendanceDao.getAttendedDaysForSubject(subjectName, startDate, todayDate);
                            int bunkedDays = attendanceDao.getBunkedDaysForSubject(subjectName, startDate, todayDate);
                            int totalDays = attendanceDao.getTotalDaysForSubject(subjectName);
                            overallAttendanceEntry.setTotalDays(totalDays);
                            overallAttendanceEntry.setBunkedDays(bunkedDays);
                            overallAttendanceEntry.setPresentDays(presentDays);
                            updateOverallAttendance(overallAttendanceEntry);
                            AttendanceUtils.checkForSmartCards(overallAttendanceEntry, applicationContext);
                            NotificationManagerCompat.from(applicationContext).cancel(Constants.SHOW_NOTIFICATION_RC_OVERALL_REFRESH_ATTENDANCE);
                        });
                    }
                });
            }
        });
    }


    private void sendNotification(Context context, @SuppressWarnings("SameParameterValue") String title, String desc, PendingIntent contentIntent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, MainApp.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_forground)
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(Constants.SHOW_NOTIFICATION_RC_OVERALL_REFRESH_ATTENDANCE, notificationBuilder.build());
    }

    private PendingIntent getContentIntent() {
        Intent intent = new Intent(applicationContext, SignInActivity.class);
        return PendingIntent.getActivity(applicationContext, Constants.SHOW_REMINDER_JOB_RC_CONTENT_INTENT, intent, 0);
    }

}
