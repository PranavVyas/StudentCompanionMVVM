package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.repositories.OverallAttendanceRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class OverallAttendanceViewModel extends AndroidViewModel {

    private LiveData<List<OverallAttendanceEntry>> allOverallAttendance;
    private OverallAttendanceRepository repository;

    public OverallAttendanceViewModel(@NonNull Application application) {
        super(application);
        repository = new OverallAttendanceRepository(application);
        allOverallAttendance = repository.getAllOverallAttendance();
    }

    public LiveData<List<OverallAttendanceEntry>> getAllOverallAttendance() {
        return allOverallAttendance;
    }

    public void setAllOverallAttendance(LiveData<List<OverallAttendanceEntry>> allOverallAttendance) {
        this.allOverallAttendance = allOverallAttendance;
    }

    public void updateOverallAttendance(OverallAttendanceEntry overallAttendanceEntry) {
        repository.updateOverallAttendance(overallAttendanceEntry);
    }
}
