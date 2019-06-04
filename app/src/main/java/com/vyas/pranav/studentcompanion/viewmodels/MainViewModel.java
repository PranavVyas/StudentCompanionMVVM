package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vyas.pranav.studentcompanion.jobs.DailyJobForSilentAction;
import com.vyas.pranav.studentcompanion.repositories.AppSettingsRepository;
import com.vyas.pranav.studentcompanion.repositories.NotificationRepository;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.NavigationDrawerUtil;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    private int currentFragmentId;
    private FirebaseUser currUser;
    private FirebaseAuth mAuth;
    private Application application;
    private NotificationRepository notificationRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        currentFragmentId = NavigationDrawerUtil.ID_TODAY_ATTENDANCE;
        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        notificationRepository = new NotificationRepository(application);
    }

    public int getCurrentFragmentId() {
        return currentFragmentId;
    }

    public void setCurrentFragmentId(int currentFragmentId) {
        this.currentFragmentId = currentFragmentId;
    }

    public FirebaseUser getCurrUser() {
        return currUser;
    }

    public void setCurrUser(FirebaseUser currUser) {
        this.currUser = currUser;
    }

    public LiveData<Integer> getNotificationCount(String dateStr) {
        return notificationRepository.getCurentNotificationCount(ConverterUtils.convertStringToDate(dateStr));
    }

    public void removePendingJobs() {
        DailyJobForSilentAction.cancelAllJobs();
        Toast.makeText(application, "Removed all Jobs for Silent Action", Toast.LENGTH_SHORT).show();
    }

    public void restartAllPendingJobs() {
        AppSettingsRepository repository = new AppSettingsRepository(application);
        if (repository.isSmartSilentEnabled()) {
            repository.enableAutoSilentDevice();
        }
    }
}
