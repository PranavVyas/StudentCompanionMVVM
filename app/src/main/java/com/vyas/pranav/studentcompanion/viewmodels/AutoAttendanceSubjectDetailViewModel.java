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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.repositories.AutoAttendanceRepository;

public class AutoAttendanceSubjectDetailViewModel extends ViewModel {

    private final AutoAttendanceRepository repo;
    private String currName;
    private LiveData<AutoAttendancePlaceEntry> mLiveData;

    public AutoAttendanceSubjectDetailViewModel(AutoAttendanceRepository repo, String subName) {
        this.repo = repo;
        this.currName = "No Place Selected! You will not be able to use Auto Attendance Feature if no place is selected! Select place now!";
        mLiveData = repo.getPlaceEntryOfSubject(subName);
    }

    public String getCurrName() {
        return currName;
    }

    public void setCurrName(String currName) {
        this.currName = currName;
    }

    public void insertNewPlace(AutoAttendancePlaceEntry newPlace) {
        repo.insertPlaceEntry(newPlace);
    }

    public LiveData<AutoAttendancePlaceEntry> getPlaceEntryOfSubject() {
        return mLiveData;
    }

    public void refreshFenceInDb(AutoAttendancePlaceEntry placeEntry) {
        repo.updatePlaceEntry(placeEntry);
    }
}
