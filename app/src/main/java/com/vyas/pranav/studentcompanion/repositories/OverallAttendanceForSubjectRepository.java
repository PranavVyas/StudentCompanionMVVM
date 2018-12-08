package com.vyas.pranav.studentcompanion.repositories;

import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class OverallAttendanceForSubjectRepository {
    private OverallAttendanceDao overallAttendanceDao;
    private AttendanceDao attendanceDao;
    private String subject;
    private AppExecutors mExecutors;

    public OverallAttendanceForSubjectRepository(OverallAttendanceDatabase mOverallDb, AttendanceDatabase mAttendanceDb, String subject) {
        overallAttendanceDao = mOverallDb.overallAttendanceDao();
        attendanceDao = mAttendanceDb.attendanceDao();
        this.subject = subject;
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
        final LiveData<OverallAttendanceEntry> overallAttendance = overallAttendanceDao.getOverallAttendanceForSubject(subjectName);
        overallAttendance.observeForever(new Observer<OverallAttendanceEntry>() {
            @Override
            public void onChanged(final OverallAttendanceEntry overallAttendanceEntry) {
                overallAttendance.removeObserver(this);
                mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        Date todayDate = new Date();
                        int presentDays = attendanceDao.getAttendedDaysForSubject(subjectName, ConverterUtils.convertStringToDate(Constants.SEM_START_DATE_STR), todayDate);
                        int bunkedDays = attendanceDao.getBunkedDaysForSubject(subjectName, ConverterUtils.convertStringToDate(Constants.SEM_START_DATE_STR), todayDate);
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


}
