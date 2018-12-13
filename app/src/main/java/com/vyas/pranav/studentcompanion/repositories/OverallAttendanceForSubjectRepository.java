package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class OverallAttendanceForSubjectRepository {
    private OverallAttendanceDao overallAttendanceDao;
    private AttendanceDao attendanceDao;
    private String subject;
    private AppExecutors mExecutors;
    private Context applicationContext;

    public OverallAttendanceForSubjectRepository(Context applicationContext, OverallAttendanceDatabase mOverallDb, AttendanceDatabase mAttendanceDb, String subject) {
        overallAttendanceDao = mOverallDb.overallAttendanceDao();
        attendanceDao = mAttendanceDb.attendanceDao();
        this.subject = subject;
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        if (applicationContext == null) {
            Logger.d("Application Context is null in OverallAttendanceForSubjectRepository");
        }
        this.applicationContext = applicationContext;
        mExecutors = AppExecutors.getInstance();
    }

    public OverallAttendanceForSubjectRepository(Context application, String subject) {
        overallAttendanceDao = OverallAttendanceDatabase.getInstance(application).overallAttendanceDao();
        attendanceDao = AttendanceDatabase.getInstance(application).attendanceDao();
        this.subject = subject;
        this.applicationContext = application;
        mExecutors = AppExecutors.getInstance();
    }

    public void updateOverallAttendance(final OverallAttendanceEntry overallAttendanceEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                overallAttendanceDao.updateOverall(overallAttendanceEntry);
            }
        });
    }

    public LiveData<OverallAttendanceEntry> getOverallAttendanceEntryForSubject() {
        return overallAttendanceDao.getOverallAttendanceForSubject(subject);
    }

    public void refreshOverallAttendanceForSubject(final String subjectName) {
        if (subjectName.equals("No Lecture")) {
            return;
        }
        mExecutors.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                final LiveData<OverallAttendanceEntry> overallAttendance = overallAttendanceDao.getOverallAttendanceForSubject(subjectName);
                overallAttendance.observeForever(new Observer<OverallAttendanceEntry>() {
                    @Override
                    public void onChanged(final OverallAttendanceEntry overallAttendanceEntry) {
                        overallAttendance.removeObserver(this);
                        SetUpProcessRepository setUpProcessRepository = new SetUpProcessRepository(applicationContext);
                        final Date startDate = ConverterUtils.convertStringToDate(setUpProcessRepository.getStartingDate());
                        mExecutors.diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                Date todayDate = new Date();
                                int presentDays = attendanceDao.getAttendedDaysForSubject(subjectName, startDate, todayDate);
                                int bunkedDays = attendanceDao.getBunkedDaysForSubject(subjectName, startDate, todayDate);
                                int totalDays = attendanceDao.getTotalDaysForSubject(subjectName);
                                overallAttendanceEntry.setTotalDays(totalDays);
                                overallAttendanceEntry.setBunkedDays(bunkedDays);
                                overallAttendanceEntry.setPresentDays(presentDays);
                                overallAttendanceDao.updateOverall(overallAttendanceEntry);
                            }
                        });
                    }
                });
            }
        });
    }


}
