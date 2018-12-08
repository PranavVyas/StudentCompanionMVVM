package com.vyas.pranav.studentcompanion.viewmodels;

import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class OverallAttendanceForSubjectViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String subjectName;
    private OverallAttendanceDatabase mOverallDb;
    private AttendanceDatabase mAttendanceDb;

    public OverallAttendanceForSubjectViewModelFactory(String subjectName, OverallAttendanceDatabase mOverallDb, AttendanceDatabase mAttendanceDb) {
        this.subjectName = subjectName;
        this.mOverallDb = mOverallDb;
        this.mAttendanceDb = mAttendanceDb;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new OverallAttendanceForSubjectViewModel(mOverallDb, mAttendanceDb, subjectName);
    }
}
