package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.repositories.OverallAttendanceRepository;
import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.util.Date;
import java.util.List;

public class OverallAttendanceViewModel extends AndroidViewModel {

    private LiveData<List<OverallAttendanceEntry>> allOverallAttendance;
    private OverallAttendanceRepository repository;
    private SetUpProcessRepository setUpProcessRepository;
    private Context context;
    private SharedPreferencesUtils utils;
    private boolean isTutorialShownOnStarting = false;

    public OverallAttendanceViewModel(@NonNull Application application) {
        super(application);
        repository = OverallAttendanceRepository.getInstance(application);
        allOverallAttendance = repository.getAllOverallAttendance();
        this.context = application;
        utils = SharedPreferencesUtils.getInstance(application);
    }

    public LiveData<List<OverallAttendanceEntry>> getAllOverallAttendance() {
        return allOverallAttendance;
    }

    public Date getStartingDate() {
        setUpProcessRepository = SetUpProcessRepository.getInstance(context);
        return ConverterUtils.convertStringToDate(setUpProcessRepository.getStartingDate());
    }

    public boolean getFirstRunForFile(String file) {
        return utils.isFileFirstOpened(file);
    }

    public void setFirstRunForFile(String file, boolean isFirstTimeOpened) {
        utils.setFileFirstTimeOpened(file, isFirstTimeOpened);
    }

    public boolean isTutorialShownOnStarting() {
        return isTutorialShownOnStarting;
    }

    public void setTutorialShownOnStarting(boolean tutorialShownOnStarting) {
        isTutorialShownOnStarting = tutorialShownOnStarting;
    }
}
