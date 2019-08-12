package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.jobs.DailyJobForSilentAction;
import com.vyas.pranav.studentcompanion.repositories.AppSettingsRepository;
import com.vyas.pranav.studentcompanion.repositories.NotificationRepository;
import com.vyas.pranav.studentcompanion.repositories.OverallAttendanceRepository;
import com.vyas.pranav.studentcompanion.utils.NavigationDrawerUtil;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.util.Date;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private int currentFragmentId;
    private FirebaseUser currUser;
    private final FirebaseAuth mAuth;
    private final Application application;
    private final NotificationRepository notificationRepository;
    private final SharedPreferencesUtils sharedPreferencesUtils;
    private final OverallAttendanceRepository overallAttendanceRepository;
    private LiveData<List<OverallAttendanceEntry>> allOverallAttendance;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        currentFragmentId = NavigationDrawerUtil.ID_TODAY_ATTENDANCE;
        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        notificationRepository = NotificationRepository.getInstance(application);
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(application);
        overallAttendanceRepository = OverallAttendanceRepository.getInstance(application);
        allOverallAttendance = overallAttendanceRepository.getAllOverallAttendance();
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

    public LiveData<Integer> getNotificationCount(Date date) {
        return notificationRepository.getCurrentNotificationCount(date);
    }

    public void removePendingJobs() {
        DailyJobForSilentAction.cancelAllJobs();
        Toast.makeText(application, "Removed all Jobs for Silent Action", Toast.LENGTH_SHORT).show();
    }

    public void restartAllPendingJobs() {
        AppSettingsRepository repository = AppSettingsRepository.getInstance(application);
        if (repository.isSmartSilentEnabled()) {
            repository.enableAutoSilentDevice();
        }
    }

    public boolean getFirstRunForFile(String file) {
        return sharedPreferencesUtils.isFileFirstOpened(file);
    }

    public void setFirstRunForFile(String file, boolean isFirstTimeOpened) {
        sharedPreferencesUtils.setFileFirstTimeOpened(file, isFirstTimeOpened);
    }

    public LiveData<List<OverallAttendanceEntry>> getAllOverallAttendance() {
        return allOverallAttendance;
    }

}
