package com.vyas.pranav.studentcompanion.viewmodels;

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
