package com.vyas.pranav.studentcompanion.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vyas.pranav.studentcompanion.repositories.AutoAttendanceRepository;

public class AutoAttendanceSubjectDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    private final AutoAttendanceRepository repository;
    private final String subName;

    public AutoAttendanceSubjectDetailViewModelFactory(AutoAttendanceRepository repository, String subName) {
        this.repository = repository;
        this.subName = subName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AutoAttendanceSubjectDetailViewModel(repository, subName);
    }
}
