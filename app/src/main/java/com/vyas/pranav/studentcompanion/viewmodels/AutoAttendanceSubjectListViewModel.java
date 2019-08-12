package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;

import java.util.List;

public class AutoAttendanceSubjectListViewModel extends AndroidViewModel {

    private final SetUpProcessRepository setUpProcessRepository;

    public AutoAttendanceSubjectListViewModel(@NonNull Application application) {
        super(application);
        setUpProcessRepository = SetUpProcessRepository.getInstance(application);
    }

    public List<String> getSubjectList() {
        return setUpProcessRepository.getSubjectList();
    }
}
