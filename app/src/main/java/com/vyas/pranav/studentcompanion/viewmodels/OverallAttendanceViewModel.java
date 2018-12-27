package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;
import android.content.Context;

import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.repositories.OverallAttendanceRepository;
import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class OverallAttendanceViewModel extends AndroidViewModel {

    private LiveData<List<OverallAttendanceEntry>> allOverallAttendance;
    private OverallAttendanceRepository repository;
    private SetUpProcessRepository setUpProcessRepository;
    private Context context;

    public OverallAttendanceViewModel(@NonNull Application application) {
        super(application);
        repository = new OverallAttendanceRepository(application);
        allOverallAttendance = repository.getAllOverallAttendance();
        this.context = application;
    }

    public LiveData<List<OverallAttendanceEntry>> getAllOverallAttendance() {
        return allOverallAttendance;
    }

    public Date getStartingDate() {
        setUpProcessRepository = new SetUpProcessRepository(context);
        return ConverterUtils.convertStringToDate(setUpProcessRepository.getStartingDate());
    }
}
