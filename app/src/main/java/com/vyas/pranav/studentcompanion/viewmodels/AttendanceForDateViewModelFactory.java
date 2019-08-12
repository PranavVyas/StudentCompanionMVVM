package com.vyas.pranav.studentcompanion.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;

public class AttendanceForDateViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Context context;
    private final Date date;

    public AttendanceForDateViewModelFactory(Context context, Date date) {
        this.context = context;
        this.date = date;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AttendanceForDateViewModel(context, date);
    }

}
