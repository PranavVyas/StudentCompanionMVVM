package com.vyas.pranav.studentcompanion.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AutoAttendanceSubjectDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Context context;
    private FragmentActivity fragmentActivity;

    public AutoAttendanceSubjectDetailViewModelFactory(Context context, FragmentActivity fragmentActivity) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AutoAttendanceSubjectDetailViewModel(context, fragmentActivity);
    }
}
