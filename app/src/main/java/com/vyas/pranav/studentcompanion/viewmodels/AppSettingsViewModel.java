package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import com.vyas.pranav.studentcompanion.repositories.AppSettingsRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AppSettingsViewModel extends AndroidViewModel {

    private boolean isReminderEnabled;
    private boolean isAutoAttendanceEnabled;
    private int reminderTime;

    private AppSettingsRepository appSettingsRepository;

    public AppSettingsViewModel(@NonNull Application application) {
        super(application);
        appSettingsRepository = new AppSettingsRepository(application);
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

    public void setRefreshGeoFence(boolean isScheduled) {
        appSettingsRepository.setGeoFenceRefreshing(isScheduled);
    }

    public void toggleSmartSilent() {
        appSettingsRepository.toggleSmartSilent();
    }
}
