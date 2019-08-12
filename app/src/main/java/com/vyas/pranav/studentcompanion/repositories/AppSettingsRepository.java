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
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceDao;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.jobs.DailyJobForRefreshGeoFence;
import com.vyas.pranav.studentcompanion.jobs.DailyJobForShowingReminder;
import com.vyas.pranav.studentcompanion.jobs.DailyJobForSilentAction;
import com.vyas.pranav.studentcompanion.jobs.DailyJobForUnsilentAction;
import com.vyas.pranav.studentcompanion.ui.activities.SignInActivity;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AppSettingsRepository {

    private final Context context;
    private final SharedPreferences mPreference;
    private final SharedPreferences.Editor mEditor;
    private static final Object LOCK = new Object();
    private static AppSettingsRepository instance;

    public static AppSettingsRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new AppSettingsRepository(context.getApplicationContext());
            }
        }
        return instance;
    }

    public AppSettingsRepository(Context context) {
        this.context = context;
        mPreference = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPreference.edit();
        mEditor.apply();
    }

    public boolean isReminderEnabled() {
        return mPreference.getBoolean(context.getString(R.string.pref_key_switch_enable_reminder), context.getResources().getBoolean(R.bool.pref_def_value_switch_enable_reminder));
    }

    public int getReminderTime() {
        return mPreference.getInt(context.getString(R.string.pref_key_time_reminder_time), context.getResources().getInteger(R.integer.pref_def_value_time_reminder_time));
    }

    public void cancelReminderJob() {
        DailyJobForShowingReminder.cancelReminderJob();
        Toast.makeText(context, "Reminder Canceled!", Toast.LENGTH_SHORT).show();
    }

    public void setReminderJob(int timeInMinutes) {
        DailyJobForShowingReminder.scheduleReminderJob(timeInMinutes);
        Toast.makeText(context, "Reminder set for time : " + ConverterUtils.convertTimeIntInString(timeInMinutes), Toast.LENGTH_SHORT).show();
    }

    public boolean isAutoAttendanceEnabled() {
        return mPreference.getBoolean(context.getString(R.string.pref_key_switch_enable_auto_attendance), context.getResources().getBoolean(R.bool.pref_def_value_switch_enable_auto_attendance));
    }

    public void enableAutoSilentDevice() {
        DailyJobForSilentAction.cancelAllJobs();
        DailyJobForUnsilentAction.cancelAllJobs();
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                final LiveData<List<TimetableEntry>> timetableForDay = TimetableRepository.getInstance(context).getTimetableForDay(ConverterUtils.getDayOfWeek(new Date()));
                timetableForDay.observeForever(new Observer<List<TimetableEntry>>() {
                    @Override
                    public void onChanged(List<TimetableEntry> timetableEntries) {
                        timetableForDay.removeObserver(this);
                        for (int i = 0; i < timetableEntries.size(); i++) {
                            if (!timetableEntries.get(i).getSubName().equals("No Lecture")) {
                                int startTime = timetableEntries.get(i).getTimeStart();
                                int endTime = timetableEntries.get(i).getTimeEnd();
                                Toast.makeText(context, "Enabled Smart Silent!", Toast.LENGTH_SHORT).show();
                                Logger.d("Smart Silent : Enabled");

                                DailyJobForSilentAction.scheduleDeviceSilentAtTime(TimeUnit.MINUTES.toMillis(startTime + Constants.TIME_WINDOW_SILENT_DEVICE));
                                DailyJobForUnsilentAction.scheduleDeviceSilentAtTime(TimeUnit.MINUTES.toMillis(endTime - Constants.TIME_WINDOW_UNSILENT_DEVICE));
                            }
                        }
                    }
                });
            }
        });
    }

    public void deleteUserAccount() {
        AuthUI.getInstance().delete(context).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Account Successfully Removed\nRedirecting to Login screen...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, SignInActivity.class);
                    context.startActivity(intent);
                } else if (task.isCanceled()) {
                    Toast.makeText(context, "Error Occurred Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void toggleNightMode() {
        if (mPreference.getBoolean(context.getString(R.string.pref_key_switch_enable_night_mode), false)) {
            context.setTheme(R.style.AppTheme_Night);
        } else {
            context.setTheme(R.style.AppTheme);
        }
    }

    public void setGeoFenceRefreshing(boolean isScheduled) {
        if (isScheduled) {
            DailyJobForRefreshGeoFence.cancelJob();
            DailyJobForRefreshGeoFence.scheduleJob();
        } else {
            DailyJobForRefreshGeoFence.cancelJob();
        }
    }

    public boolean isSmartSilentEnabled() {
        return mPreference.getBoolean(context.getString(R.string.pref_key_switch_enable_smart_silent), false);
    }

    public void toggleSmartSilent() {
        if (isSmartSilentEnabled()) {
            enableAutoSilentDevice();
        } else {
            DailyJobForSilentAction.cancelAllJobs();
            DailyJobForUnsilentAction.cancelAllJobs();
            Toast.makeText(context, "Disabled Smart Silent!", Toast.LENGTH_SHORT).show();
            Logger.d("Smart Silent : Disabled");
        }
    }

    public LiveData<List<AutoAttendancePlaceEntry>> getAutoAttendanceLiveData() {
        AutoAttendancePlaceDao attendancePlaceDao = MainDatabase.getInstance(context).autoAttendancePlaceDao();
        return attendancePlaceDao.getAllPlaceEntries();
    }
}
