package com.vyas.pranav.studentcompanion.viewmodels;

import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AttendanceForDateViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AttendanceDatabase mAttendanceDb;
    private final Date date;

    public AttendanceForDateViewModelFactory(AttendanceDatabase mAttendanceDb, Date date) {
        this.mAttendanceDb = mAttendanceDb;
        this.date = date;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AttendanceForDateViewModel(mAttendanceDb, date);
    }

}
