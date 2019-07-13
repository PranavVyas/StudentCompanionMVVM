package com.vyas.pranav.studentcompanion.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.GoogleApiClient;

public class AutoAttendanceSubjectDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Context context;
    private final GoogleApiClient googleApiClient;

    public AutoAttendanceSubjectDetailViewModelFactory(Context context, GoogleApiClient googleApiClient) {
        this.context = context;
        this.googleApiClient = googleApiClient;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AutoAttendanceSubjectDetailViewModel(context, googleApiClient);
    }
}
