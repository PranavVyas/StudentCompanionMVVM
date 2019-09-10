package com.vyas.pranav.studentcompanion.viewmodels;
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
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.repositories.AppSettingsRepository;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.util.List;

public class AppSettingsViewModel extends AndroidViewModel {

    private LiveData<List<AutoAttendancePlaceEntry>> autoAttendancePlaceEntryLiveData;
    private final AppSettingsRepository appSettingsRepository;
    private final SharedPreferencesUtils sharedPreferencesUtils;
    private boolean isDeveloperEnabled;

    public AppSettingsViewModel(@NonNull Application application) {
        super(application);
        appSettingsRepository = new AppSettingsRepository(application);
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(application);
        autoAttendancePlaceEntryLiveData = appSettingsRepository.getAutoAttendanceLiveData();
    }

    public boolean isReminderEnabled() {
        return sharedPreferencesUtils.isReminderEnabled();
    }

    public int getReminderTime() {
        return sharedPreferencesUtils.getReminderTime();
    }

    public boolean setReminderJobTime(int timeInMinutes) {
        return appSettingsRepository.setReminderJob(timeInMinutes);
    }

    public boolean cancelReminderJob() {
        return appSettingsRepository.cancelReminderJob();
    }

    public boolean isAutoAttendanceEnabled() {
        return sharedPreferencesUtils.isAutoAttendanceEnabled();
    }

    public boolean isSmartSilentEnabled() {
        return sharedPreferencesUtils.isSmartSilentEnabled();
    }

    public void deleteUserAccount() {
        appSettingsRepository.deleteUserAccount();
    }

    public void toggleNightMode() {
        sharedPreferencesUtils.toggleNightMode();
    }

//    public void setRefreshGeoFence(boolean isScheduled) {
//        appSettingsRepository.setGeoFenceRefreshing(isScheduled);
//    }

    public boolean toggleSmartSilent() {
        return appSettingsRepository.toggleSmartSilent();
    }

    public LiveData<List<AutoAttendancePlaceEntry>> getAutoAttendanceLiveData() {
        return autoAttendancePlaceEntryLiveData;
    }

    public boolean isDeveloperEnabled() {
        return sharedPreferencesUtils.isDeveloperEnabled();
    }

    public void setDeveloperEnabled(boolean developerEnabled) {
        sharedPreferencesUtils.setDeveloperEnabled(developerEnabled);
    }

    //    public boolean isNightModeEnabled() {
//        return appSettingsRepository.isNightModeEnabled();
//    }
}
