package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

import java.util.List;

import androidx.lifecycle.LiveData;

public class OverallAttendanceRepository {

    private OverallAttendanceDao overallAttendanceDao;
    private AttendanceDao attendanceDao;
    private AppExecutors mExecutors;
    private Context application;

    public OverallAttendanceRepository(Context application) {
        OverallAttendanceDatabase mOverallDb = OverallAttendanceDatabase.getInstance(application);
        this.overallAttendanceDao = mOverallDb.overallAttendanceDao();
        AttendanceDatabase mAttendanceDb = AttendanceDatabase.getInstance(application);
        this.attendanceDao = mAttendanceDb.attendanceDao();
        this.mExecutors = AppExecutors.getInstance();
        this.application = application;
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
        if (application == null) {
            Logger.d("Context empty in refreshOverallAttendance");
        }
        SetUpProcessRepository setUpProcessRepository = new SetUpProcessRepository(application);
        List<String> subList = setUpProcessRepository.getSubjectList();
        for (int i = 0; i < subList.size(); i++) {
            String subject = subList.get(i);
            OverallAttendanceForSubjectRepository repository = new OverallAttendanceForSubjectRepository(application, subject);
            repository.refreshOverallAttendanceForSubject(subject);
        }
    }
}
