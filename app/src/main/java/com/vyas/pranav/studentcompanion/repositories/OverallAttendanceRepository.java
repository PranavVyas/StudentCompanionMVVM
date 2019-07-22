package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.util.List;

public class OverallAttendanceRepository {

    private final OverallAttendanceDao overallAttendanceDao;
    private final AttendanceDao attendanceDao;
    private final AppExecutors mExecutors;
    private final Context context;

    public OverallAttendanceRepository(Context context) {
        this.overallAttendanceDao = MainDatabase.getInstance(context).overallAttendanceDao();
        this.attendanceDao = MainDatabase.getInstance(context).attendanceDao();
        this.mExecutors = AppExecutors.getInstance();
        this.context = context;
    }

    public LiveData<List<OverallAttendanceEntry>> getAllOverallAttendance() {
        return overallAttendanceDao.getAllOverallAttendance();
    }

    public void insertOverallAttendance(final OverallAttendanceEntry overallAttendanceEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                overallAttendanceDao.insertOverall(overallAttendanceEntry);
            }
        });
    }

    public void updateOverallAttendance(final OverallAttendanceEntry overallAttendanceEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                overallAttendanceDao.updateOverall(overallAttendanceEntry);
            }
        });
    }

    public void deleteOverallAttendance(final OverallAttendanceEntry overallAttendanceEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                overallAttendanceDao.deleteOverall(overallAttendanceEntry);
            }
        });
    }

    public void deleteAllOverallAttendance() {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                overallAttendanceDao.deleteAllOverall();
            }
        });
    }

    public void refreshAllOverallAttendance() {
        if (context == null) {
            Logger.d("Context empty in refreshOverallAttendance");
        }
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context);
        List<String> subList = sharedPreferencesUtils.getSubjectList();
        for (int i = 0; i < subList.size(); i++) {
            String subject = subList.get(i);
            OverallAttendanceForSubjectRepository repository = new OverallAttendanceForSubjectRepository(context, subject);
            repository.refreshOverallAttendanceForSubject(subject);
        }
    }
}
