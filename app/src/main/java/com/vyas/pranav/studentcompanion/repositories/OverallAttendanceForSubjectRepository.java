package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.firestore.NotificationFirestoreModel;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

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
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                LiveData<NotificationFirestoreModel> notification = notificationRepository.getNotificationById("OverallAttendance_" + subject);
                notification.observeForever(new Observer<NotificationFirestoreModel>() {
                    @Override
                    public void onChanged(NotificationFirestoreModel notificationFirestoreModel) {
                        notification.removeObserver(this);
                        int totalDays = subjectAttendance.getTotalDays();
                        int bunkedDays = subjectAttendance.getBunkedDays();
                        int attendanceCriteria = sharedPreferencesUtils.getCurrentAttendanceCriteria();
                        int daysTotalAvailableToBunk = (int) Math.ceil(totalDays * (1f - (attendanceCriteria / 100.0f)));
                        if (daysTotalAvailableToBunk - bunkedDays < Constants.FLEX_DAYS_EXTRA_TO_BUNK) {
                            //Checking if notification still exists or not. If exists than it will not be 0 type
                            if (notificationFirestoreModel == null) {
                                notificationFirestoreModel = new NotificationFirestoreModel();
                                long time = new Date().getTime() + 86400000;
                                notificationFirestoreModel.setDateInMillis(time);
                                notificationFirestoreModel.setUrl("-1");
                                notificationFirestoreModel.setVenue("-1");
                                notificationFirestoreModel.setName("Low Attendance");
                                notificationFirestoreModel.setShort_info("Attendance is low in the Subject :" + subjectAttendance.getSubName());
                                notificationFirestoreModel.setImage_url("NO_URL");
                                notificationFirestoreModel.setType(Constants.NOTI_TYPE_LOW_ATTENDANCE);
                                notificationFirestoreModel.set_ID("OverallAttendance_" + subject);
                                notificationRepository.insertNotification(notificationFirestoreModel);
                            } else {
                                long time = new Date().getTime() + 86400000;
                                notificationFirestoreModel.setDateInMillis(time);
                                notificationFirestoreModel.setUrl("-1");
                                notificationFirestoreModel.setVenue("-1");
                                notificationFirestoreModel.setName("Low Attendance");
                                notificationFirestoreModel.setShort_info("Attendance is low in the Subject :" + subjectAttendance.getSubName());
                                notificationFirestoreModel.setImage_url("NO_URL");
                                notificationFirestoreModel.setType(Constants.NOTI_TYPE_LOW_ATTENDANCE);
                                notificationRepository.updateNotification(notificationFirestoreModel);
                            }
                            Logger.d("Low Attendance");
                        } else {
                            if (notificationFirestoreModel.getType() == 0) {
                                notificationRepository.deleteNotificationById("OverallAttendance_" + subject);
                            }
                        }
                    }
                });
            }
        });
    }


}
