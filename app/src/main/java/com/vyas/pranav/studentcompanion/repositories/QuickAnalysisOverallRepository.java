package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;

import java.util.List;

public class QuickAnalysisOverallRepository {

    private Context context;
    private AttendanceDao attendanceDao;

    public QuickAnalysisOverallRepository(Context context) {
        this.context = context;
        attendanceDao = MainDatabase.getInstance(context).attendanceDao();
    }

    public LiveData<List<AttendanceEntry>> getAttendanceForSubject(String subject) {
        return attendanceDao.getAttendanceForSubject(subject);
    }
}
