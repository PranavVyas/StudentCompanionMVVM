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

import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;
import com.vyas.pranav.studentcompanion.repositories.TimetableRepository;
import com.vyas.pranav.studentcompanion.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimetableViewModel extends AndroidViewModel {

    private LiveData<List<TimetableEntry>> timetableEntries;
    private TimetableRepository timetableRepository;
    private SetUpProcessRepository setUpProcessRepository;
    private int lecturesPerDay;
    private boolean isProductiveViewOn = false;
    private int currentPage = 0;
    private List<List<String>> daysLectures;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isProductiveViewOn() {
        return isProductiveViewOn;
    }

    public void setProductiveViewOn(boolean productiveViewOn) {
        isProductiveViewOn = productiveViewOn;
    }

    public TimetableViewModel(@NonNull Application application) {
        super(application);
        timetableRepository = new TimetableRepository(application);
        timetableEntries = timetableRepository.getFullTimetable();
        setUpProcessRepository = new SetUpProcessRepository(application);
        lecturesPerDay = setUpProcessRepository.getNoOfLecturesPerDay();
        generateListForFirstTime();
    }

    public List<List<String>> getDaysLectures() {
        return daysLectures;
    }

    public void setDaysLectures(int raw, int column, String newSub) {
        this.daysLectures.get(raw).set(column, newSub);
    }

    private void generateListForFirstTime() {
        List<String> Monday = new ArrayList<>();
        List<String> Tuesday = new ArrayList<>();
        List<String> Wednesday = new ArrayList<>();
        List<String> Thursday = new ArrayList<>();
        List<String> Friday = new ArrayList<>();
        for (int i = 0; i < getLecturesPerDay() * Constants.NO_OF_DAYS; i++) {
            int day = i / lecturesPerDay;
            int lecture = i % lecturesPerDay;

            switch (day) {
                case 0:
                    //Monday
                    Monday.add(lecture, Constants.DEFAULT_LECTURE);
                    break;

                case 1:
                    //Tuesday
                    Tuesday.add(lecture, Constants.DEFAULT_LECTURE);
                    break;

                case 2:
                    //Wednesday
                    Wednesday.add(lecture, Constants.DEFAULT_LECTURE);
                    break;

                case 3:
                    //Thursday
                    Thursday.add(lecture, Constants.DEFAULT_LECTURE);
                    break;

                case 4:
                    //Friday
                    Friday.add(lecture, Constants.DEFAULT_LECTURE);
                    break;
            }
        }
        daysLectures = new ArrayList<>(Arrays.asList(
                Monday, Tuesday, Wednesday, Thursday, Friday
        ));
    }

    public LiveData<List<TimetableEntry>> getTimetableEntries() {
        return timetableEntries;
    }

    public int getStartingTimeOfLecture(int lectureNo) {
        return setUpProcessRepository.getStartTimeForLecture(lectureNo);
    }

    public int getEndingTimeOfLecture(int lectureNo) {
        return setUpProcessRepository.getEndTimeForLecture(lectureNo);
    }

    public int getLecturesPerDay() {
        return lecturesPerDay;
    }

    public int getSemInfo() {
        return setUpProcessRepository.getSemester();
    }

    public String getStartDate() {
        return setUpProcessRepository.getStartingDate();
    }

    public String getEndDate() {
        return setUpProcessRepository.getEndingDate();
    }

    public List<String> getSubjectList() {
        return setUpProcessRepository.getSubjectList();
    }


}
