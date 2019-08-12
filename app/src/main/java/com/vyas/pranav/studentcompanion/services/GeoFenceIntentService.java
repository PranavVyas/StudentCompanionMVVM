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
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableDao;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GeoFenceIntentService extends IntentService {
    private static final int RC_SHOW_NOTIFICATION = 4546;
    private static final String TAG = "GeoFenceIntentService";

    public GeoFenceIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        Toast.makeText(this, "GeoFence Handling event triggered", Toast.LENGTH_LONG).show();
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            if (GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE == geofencingEvent.getErrorCode()) {
                Logger.d("GeoFence Not Available");
                Toast.makeText(this, "Location is Off", Toast.LENGTH_SHORT).show();
            }
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();


        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            for (int i = 0; i < triggeringGeofences.size(); i++) {
                Geofence geo = triggeringGeofences.get(i);
                Logger.d("Triggering Geo fence is : " + geo.getRequestId());
            }
            Toast.makeText(this, "GeoFence Dwell Triggered", Toast.LENGTH_SHORT).show();
            insertAttendance();
        }
    }

    private void showNotification(String subjectName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MainChannel";
            String description = "Show Main Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("NOTIFICATION_MAIN", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplication(), "NOTIFICATION_MAIN")
                .setSmallIcon(R.drawable.logo_forground)
                .setContentTitle("GeoFence")
                .setContentText("Entered Geo fence For Subject " + subjectName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.getApplicationContext());

        notificationManager.notify((int) (RC_SHOW_NOTIFICATION + Math.random() * 100), mBuilder.build());
    }

    private void insertAttendance() {
        MainDatabase mDb = MainDatabase.getInstance(getApplicationContext());
        TimetableDao dao = mDb.timetableDao();
        final AttendanceDao attendanceDao = mDb.attendanceDao();
        final LiveData<List<TimetableEntry>> timetableForDay = dao.getTimetableForDay(ConverterUtils.getDayOfWeek(new Date()));
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                timetableForDay.observeForever(new Observer<List<TimetableEntry>>() {
                    @Override
                    public void onChanged(List<TimetableEntry> timetableEntries) {
                        timetableForDay.removeObserver(this);
                        long currTime = ConverterUtils.getCurrentTimeInMillis();
                        for (int i = 0; i < timetableEntries.size(); i++) {
                            long timeStart = TimeUnit.MINUTES.toMillis(timetableEntries.get(i).getTimeStart());
                            long timeEnd = TimeUnit.MINUTES.toMillis(timetableEntries.get(i).getTimeEnd());
                            if (currTime < timeEnd && currTime > timeStart) {
                                final String subName = timetableEntries.get(i).getSubName();
                                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        final LiveData<List<AttendanceEntry>> attendanceForDate = attendanceDao.getAttendanceForDate(new Date());
                                        attendanceForDate.observeForever(new Observer<List<AttendanceEntry>>() {
                                            @Override
                                            public void onChanged(List<AttendanceEntry> attendanceEntries) {
                                                attendanceForDate.removeObserver(this);
                                                for (int j = 0; j < attendanceEntries.size(); j++) {
                                                    if (attendanceEntries.get(j).getSubjectName().equals(subName)) {
                                                        attendanceEntries.get(j).setPresent(true);
                                                        attendanceDao.updateAttendance(attendanceEntries.get(j));
                                                        showNotification(subName);
                                                    }
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

    }
}
