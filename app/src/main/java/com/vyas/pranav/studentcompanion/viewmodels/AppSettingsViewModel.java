package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.repositories.AppSettingsRepository;

import java.util.List;

public class AppSettingsViewModel extends AndroidViewModel {

    private boolean isReminderEnabled;
    private boolean isAutoAttendanceEnabled;
    private int reminderTime;
    private LiveData<List<AutoAttendancePlaceEntry>> autoAttendancePlaceEntryLiveData;

    private final AppSettingsRepository appSettingsRepository;

    public AppSettingsViewModel(@NonNull Application application) {
        super(application);
        appSettingsRepository = new AppSettingsRepository(application);
        autoAttendancePlaceEntryLiveData = appSettingsRepository.getAutoAtttendanceLiveData();
    }

    public boolean isReminderEnabled() {
        return appSettingsRepository.isReminderEnabled();
    }

    public int getReminderTime() {
        return appSettingsRepository.getReminderTime();
    }

    public void setReminderJobTime(int timeInMinutes) {
        appSettingsRepository.setReminderJob(timeInMinutes);
    }

    public void cancelReminderJob() {
        appSettingsRepository.cancelReminderJob();
    }

    public boolean isAutoAttendanceEnabled() {
        return appSettingsRepository.isAutoAttendanceEnabled();
    }

    public boolean isSmartSilentEnabled() {
        return appSettingsRepository.isSmartSilentEnabled();
    }

    public void deleteUserAccount() {
        appSettingsRepository.deleteUserAccount();
    }

    public void toggleNightMode() {
        appSettingsRepository.toggleNightMode();
    }

//    public void setRefreshGeoFence(boolean isScheduled) {
//        appSettingsRepository.setGeoFenceRefreshing(isScheduled);
//    }

    public void toggleSmartSilent() {
        appSettingsRepository.toggleSmartSilent();
    }

    public LiveData<List<AutoAttendancePlaceEntry>> getAutoAttendanceLiveData() {
        return autoAttendancePlaceEntryLiveData;
    }

//    public boolean isNightModeEnabled() {
//        return appSettingsRepository.isNightModeEnabled();
//    }
}
