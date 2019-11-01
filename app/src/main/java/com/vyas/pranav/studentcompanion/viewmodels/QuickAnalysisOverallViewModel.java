package com.vyas.pranav.studentcompanion.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.repositories.QuickAnalysisOverallRepository;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.util.List;

public class QuickAnalysisOverallViewModel extends ViewModel {

    private String subject, startingDate, endingDate;
    private Context context;
    private int semester;
    private LiveData<List<AttendanceEntry>> attendanceForSubject;

    public QuickAnalysisOverallViewModel(String subject, Context context) {
        QuickAnalysisOverallRepository repository = new QuickAnalysisOverallRepository(context);
        attendanceForSubject = repository.getAttendanceForSubject(subject);
        SharedPreferencesUtils utils = new SharedPreferencesUtils(context);
        semester = utils.getSemester();
        startingDate = utils.getStartingDate();
        endingDate = utils.getEndingDate();
    }

    public LiveData<List<AttendanceEntry>> getAttendanceForSubject() {
        return attendanceForSubject;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public int getSemester() {
        return semester;
    }
}
