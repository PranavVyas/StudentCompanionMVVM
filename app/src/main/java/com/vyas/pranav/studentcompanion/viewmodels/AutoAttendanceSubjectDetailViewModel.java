package com.vyas.pranav.studentcompanion.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.common.api.GoogleApiClient;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.repositories.AutoAttendanceRepository;

public class AutoAttendanceSubjectDetailViewModel extends ViewModel {

    private AutoAttendanceRepository repo;
    private Context context;
    private String currName;

    public AutoAttendanceSubjectDetailViewModel(Context context, GoogleApiClient googleApiClient) {
        this.context = context;
        repo = new AutoAttendanceRepository(context);
        this.currName = "No Place Selected! You will not be able to use Auto Attendance Feature if no place is selected! Select place now!";
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

    public LiveData<AutoAttendancePlaceEntry> getPlaceEntryOfSubject(String subject) {
        return repo.getPlaceEntryOfSubject(subject);
    }

    public void refreshFenceInDb(AutoAttendancePlaceEntry placeEntry) {
        repo.updatePlaceEntry(placeEntry);
    }
}
