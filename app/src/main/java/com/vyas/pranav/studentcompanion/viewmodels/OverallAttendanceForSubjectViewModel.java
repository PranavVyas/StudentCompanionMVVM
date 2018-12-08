package com.vyas.pranav.studentcompanion.viewmodels;

import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDatabase;
import com.vyas.pranav.studentcompanion.repositories.OverallAttendanceForSubjectRepository;

import androidx.lifecycle.ViewModel;

public class OverallAttendanceForSubjectViewModel extends ViewModel {

    private OverallAttendanceForSubjectRepository repository;

    public OverallAttendanceForSubjectViewModel(OverallAttendanceDatabase mOverallDb, AttendanceDatabase mAttendanceDb, String subjectName) {
        repository = new OverallAttendanceForSubjectRepository(mOverallDb, mAttendanceDb, subjectName);
    }

    public void refreshOverallAttendance(String subjectName) {
        repository.refreshOverallAttendanceForSubject(subjectName);
    }

}
