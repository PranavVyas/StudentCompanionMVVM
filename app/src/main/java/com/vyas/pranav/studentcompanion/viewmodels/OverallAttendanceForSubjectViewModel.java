package com.vyas.pranav.studentcompanion.viewmodels;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.repositories.OverallAttendanceForSubjectRepository;

public class OverallAttendanceForSubjectViewModel extends ViewModel {

    private final OverallAttendanceForSubjectRepository repository;

    public OverallAttendanceForSubjectViewModel(Context applicationContext, MainDatabase mDb, String subjectName) {
        repository = new OverallAttendanceForSubjectRepository(applicationContext, mDb, subjectName);
    }

    public void refreshOverallAttendance(String subjectName) {
        repository.refreshOverallAttendanceForSubject(subjectName);
    }
}
