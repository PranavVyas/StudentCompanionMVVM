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
