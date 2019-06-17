package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.NotificationEntry;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.Date;

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
                                updateOverallAttendance(overallAttendanceEntry);
                                checkForSmartCards(overallAttendanceEntry);
                            }
                        });
                    }
                });
            }
        });
    }

    private void checkForSmartCards(OverallAttendanceEntry subjectAttendance) {
        NotificationRepository notificationRepository = new NotificationRepository(applicationContext);
        int totalDays = subjectAttendance.getTotalDays();
        int bunkedDays = subjectAttendance.getBunkedDays();
        int daysTotalAvailableToBunk = (int) Math.ceil(totalDays * (1 - Constants.ATTENDANCE_THRESHOLD));
        if (daysTotalAvailableToBunk - bunkedDays < Constants.FLEX_DAYS_EXTRA_TO_BUNK) {
            NotificationEntry notification = new NotificationEntry();
            notification.setDate(ConverterUtils.convertDateToString(new Date()));
            notification.set_ID(subjectAttendance.get_ID());
            notification.setUrl("-1");
            notification.setVenue("-1");
            //TODO never set null to url
            notification.setName("Low Attendance");
            notification.setShort_info("Attendance is low in the Subject :" + subjectAttendance.getSubName());
            notificationRepository.insertNotification(notification);
            Logger.d("Low Attendance");
        } else {
            notificationRepository.deleteNotification(subjectAttendance.get_ID());
        }
    }


}
