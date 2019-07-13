package com.vyas.pranav.studentcompanion.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;

public class OverallAttendanceForSubjectViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MainDatabase mDb;
    private final Context applicationContext;
    private String subjectName;

    public OverallAttendanceForSubjectViewModelFactory(String subjectName, MainDatabase mDb, Context applicationContext) {
        this.subjectName = subjectName;
        this.mDb = mDb;
        this.applicationContext = applicationContext.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new OverallAttendanceForSubjectViewModel(applicationContext, mDb, subjectName);
    }
}
