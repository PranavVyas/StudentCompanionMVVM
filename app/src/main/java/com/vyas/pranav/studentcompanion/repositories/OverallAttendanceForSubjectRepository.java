package com.vyas.pranav.studentcompanion.repositories;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.ui.activities.SignInActivity;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.AttendanceUtils;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.MainApp;

import java.util.Date;

public class OverallAttendanceForSubjectRepository {
    private final OverallAttendanceDao overallAttendanceDao;
    private final AttendanceDao attendanceDao;
    private final AppExecutors mExecutors;
    private final Context applicationContext;
    private String subject;

    public OverallAttendanceForSubjectRepository(Context applicationContext, MainDatabase mDb, String subject) {
        overallAttendanceDao = mDb.overallAttendanceDao();
        attendanceDao = mDb.attendanceDao();
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
        mExecutors.diskIO().execute(() -> overallAttendanceDao.updateOverall(overallAttendanceEntry));
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
                        mExecutors.diskIO().execute(() -> {
                            Date todayDate = new Date();
                            int presentDays = attendanceDao.getAttendedDaysForSubject(subjectName, startDate, todayDate);
                            int bunkedDays = attendanceDao.getBunkedDaysForSubject(subjectName, startDate, todayDate);
                            int totalDays = attendanceDao.getTotalDaysForSubject(subjectName);
                            overallAttendanceEntry.setTotalDays(totalDays);
                            overallAttendanceEntry.setBunkedDays(bunkedDays);
                            overallAttendanceEntry.setPresentDays(presentDays);
                            updateOverallAttendance(overallAttendanceEntry);
                            AttendanceUtils.checkForSmartCards(overallAttendanceEntry, applicationContext);
                        });
                    }
                });
                sendNotification(applicationContext, "Refreshed", "Subject Overall Attendance is refreshed for subject : " + subjectName, getContentIntent());
            }
        });
    }

    private void sendNotification(Context context, @SuppressWarnings("SameParameterValue") String title, String desc, PendingIntent contentIntent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, MainApp.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_forground)
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify((int) (Math.random() * 1000), notificationBuilder.build());
        //todo debug
    }

    private PendingIntent getContentIntent() {
        Intent intent = new Intent(applicationContext, SignInActivity.class);
        return PendingIntent.getActivity(applicationContext, Constants.SHOW_REMINDER_JOB_RC_CONTENT_INTENT, intent, 0);
    }

}
