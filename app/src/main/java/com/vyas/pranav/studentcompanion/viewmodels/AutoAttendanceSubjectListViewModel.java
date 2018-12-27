package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AutoAttendanceSubjectListViewModel extends AndroidViewModel {

    private SetUpProcessRepository setUpProcessRepository;

    public AutoAttendanceSubjectListViewModel(@NonNull Application application) {
        super(application);
        setUpProcessRepository = new SetUpProcessRepository(application);

    }

    public List<String> getSubjectList() {
        return setUpProcessRepository.getSubjectsListOnly();
    }
}
