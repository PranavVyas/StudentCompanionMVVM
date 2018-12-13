package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import com.vyas.pranav.studentcompanion.repositories.AppSettingsRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AppSettingsViewModel extends AndroidViewModel {

    private boolean isReminderEnabled;
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

}
