package com.vyas.pranav.studentcompanion.repositories;

import android.app.Application;

import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class OverallAttendanceRepository {

    private OverallAttendanceDao overallAttendanceDao;
    private AttendanceDao attendanceDao;
    private AppExecutors mExecutors;

    public OverallAttendanceRepository(Application application) {
        OverallAttendanceDatabase mOverallDb = OverallAttendanceDatabase.getInstance(application);
        this.overallAttendanceDao = mOverallDb.overallAttendanceDao();
        AttendanceDatabase mAttendanceDb = AttendanceDatabase.getInstance(application);
        this.attendanceDao = mAttendanceDb.attendanceDao();
        this.mExecutors = AppExecutors.getInstance();
    }

    public OverallAttendanceRepository(OverallAttendanceDatabase mOverallDb, AttendanceDatabase mAttendanceDb) {
        this.attendanceDao = mAttendanceDb.attendanceDao();
        this.overallAttendanceDao = mOverallDb.overallAttendanceDao();
        this.mExecutors = AppExecutors.getInstance();
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
        overallAttendanceDao.getAllOverallAttendance().observeForever(new Observer<List<OverallAttendanceEntry>>() {
            @Override
            public void onChanged(List<OverallAttendanceEntry> overallAttendanceEntries) {
                overallAttendanceDao.getAllOverallAttendance().removeObserver(this);
                for (final OverallAttendanceEntry x :
                        overallAttendanceEntries) {
                    final String subName = x.getSubName();
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            Date todayDate = new Date();
                            int presentDays = attendanceDao.getAttendedDaysForSubject(subName, ConverterUtils.convertStringToDate(Constants.SEM_START_DATE_STR), todayDate);
                            int bunkedDays = attendanceDao.getBunkedDaysForSubject(subName, ConverterUtils.convertStringToDate("01/12/2018"), new Date());
                            int totalDays = attendanceDao.getTotalDaysForSubject(subName);
                            x.setTotalDays(totalDays);
                            x.setBunkedDays(bunkedDays);
                            x.setPresentDays(presentDays);
                            overallAttendanceDao.updateOverall(x);
                        }
                    });
                }
            }
        });
    }

}
