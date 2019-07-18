package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.AttendanceUtils;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.util.Date;

public class OverallAttendanceForSubjectRepository {
    private final OverallAttendanceDao overallAttendanceDao;
    private final AttendanceDao attendanceDao;
    private final AppExecutors mExecutors;
    private final Context applicationContext;
    private String subject;
    private SharedPreferencesUtils sharedPreferencesUtils;

    public OverallAttendanceForSubjectRepository(Context applicationContext, MainDatabase mDb, String subject) {
        overallAttendanceDao = mDb.overallAttendanceDao();
        attendanceDao = mDb.attendanceDao();
        sharedPreferencesUtils = new SharedPreferencesUtils(applicationContext);
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
        overallAttendanceDao = MainDatabase.getInstance(application).overallAttendanceDao();
        attendanceDao = MainDatabase.getInstance(application).attendanceDao();
        this.subject = subject;
        this.applicationContext = application;
        mExecutors = AppExecutors.getInstance();
    }

    private void updateOverallAttendance(final OverallAttendanceEntry overallAttendanceEntry) {
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
                                updateOverallAttendance(overallAttendanceEntry);
                                AttendanceUtils.checkForSmartCards(overallAttendanceEntry, applicationContext);
                            }
                        });
                    }
                });
            }
        });
    }


}
