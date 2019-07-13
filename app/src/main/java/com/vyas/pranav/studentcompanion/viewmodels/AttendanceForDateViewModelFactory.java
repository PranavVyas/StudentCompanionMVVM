package com.vyas.pranav.studentcompanion.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;

import java.util.Date;

public class AttendanceForDateViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MainDatabase mDb;
    private final Date date;

    public AttendanceForDateViewModelFactory(MainDatabase mDb, Date date) {
        this.mDb = mDb;
        this.date = date;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AttendanceForDateViewModel(mDb, date);
    }

}
