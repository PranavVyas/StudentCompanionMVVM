package com.vyas.pranav.studentcompanion.utils;
/*
Student Companion - An Android App that has features like attendance manager, note manager etc
Copyright (C) 2019  Pranav Vyas

This file is a part of Student Companion.

Student Companion is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Student Companion is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.
*/
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.firestore.NotificationFirestoreModel;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.repositories.NotificationRepository;
import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AttendanceUtils {

    public static final int FILE_NOT_FOUND_SRC = -1;
    private static final String TAG = "AttendanceUtils";
    private static final int FILE_NOT_CREATED_DEST = -2;
    private static final int UNKNOWN_ERROR = -3;

    public static List<Date> getDates(Date date1, Date date2) {
        ArrayList<Date> dates = new ArrayList<>();

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static void checkForSmartCards(OverallAttendanceEntry subjectAttendance, Context applicationContext) {
        String id = "OverallAttendance_" + subjectAttendance.getSubName();
        NotificationRepository notificationRepository = new NotificationRepository(applicationContext);
        SharedPreferencesUtils sharedPreferencesUtils = SharedPreferencesUtils.getInstance(applicationContext);
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                LiveData<NotificationFirestoreModel> notification = notificationRepository.getNotificationById(id);
                notification.observeForever(new Observer<NotificationFirestoreModel>() {
                    @Override
                    public void onChanged(NotificationFirestoreModel notificationFirestoreModel) {
                        notification.removeObserver(this);
                        int totalDays = subjectAttendance.getTotalDays();
                        int bunkedDays = subjectAttendance.getBunkedDays();
                        double presentPresent = (subjectAttendance.getPresentDays() * 100.0) / totalDays;
                        double warningPercent = 100 - ((100.0 - sharedPreferencesUtils.getCurrentAttendanceCriteria()) * 2 / 3);
                        int maxAttendance = (int) Math.ceil(((totalDays - bunkedDays) * 100.0) / totalDays);
                        Logger.d("Present percent : " + maxAttendance + "\nWarning Percent : " + warningPercent);
                        if (maxAttendance < warningPercent) {
                            //Checking if notification still exists or not. If exists than it will not be 0 type
                            if (notificationFirestoreModel == null) {
                                notificationFirestoreModel = new NotificationFirestoreModel();
                                long time = new Date().getTime() + TimeUnit.DAYS.toMillis(1);
                                notificationFirestoreModel.setDateInMillis(String.valueOf(time));
                                notificationFirestoreModel.setUrl("-1");
                                notificationFirestoreModel.setVenue("-1");
                                notificationFirestoreModel.setName("Low Attendance");
                                notificationFirestoreModel.setShort_info("Attendance is low in the Subject :" + subjectAttendance.getSubName());
                                notificationFirestoreModel.setImage_url("NO_URL");
                                notificationFirestoreModel.setType(Constants.NOTI_TYPE_LOW_ATTENDANCE);
                                notificationFirestoreModel.set_ID(id);
                                notificationRepository.insertNotification(notificationFirestoreModel);
                            } else {
                                long time = new Date().getTime() + TimeUnit.DAYS.toMillis(1);
                                notificationFirestoreModel.setDateInMillis(String.valueOf(time));
                                notificationFirestoreModel.setUrl("-1");
                                notificationFirestoreModel.setVenue("-1");
                                notificationFirestoreModel.setName("Low Attendance");
                                notificationFirestoreModel.setShort_info("Attendance is low in the Subject :" + subjectAttendance.getSubName());
                                notificationFirestoreModel.setImage_url("NO_URL");
                                notificationFirestoreModel.setType(Constants.NOTI_TYPE_LOW_ATTENDANCE);
                                notificationRepository.updateNotification(notificationFirestoreModel);
                            }
                            Logger.d("Low Attendance for id " + id);
                        } else {
//                            if (notificationFirestoreModel.getType() == Constants.NOTI_TYPE_LOW_ATTENDANCE) {
                            if (notificationFirestoreModel != null) {
                                notificationRepository.deleteNotificationById(id);
//                            }
                            }
                        }
                    }
                });
            }
        });
    }

    public static boolean hasInternetAccess(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean res = activeNetworkInfo != null;
        if (res) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("https://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
            } catch (IOException e) {
                Log.e(TAG, "Error checking internet connection", e);
            }
        } else {
            Log.d(TAG, "No network available!");
        }
        return false;
    }

    public static void refreshNewTimetable(Context context, int semester, List<List<String>> subjects, List<String> days, List<String> columnTitles, String startDate) {
        List<TimetableEntry> timetableEntries = new ArrayList<>();
        MainDatabase mDb = MainDatabase.getInstance(context);
        for (int i = 0; i < days.size(); i++) {
            for (int j = 0; j < columnTitles.size(); j++) {
                String subject = subjects.get(i).get(j);
                int lectureNo = j + 1;
                String day = days.get(i);
                String columnTitle = columnTitles.get(j);
                int timeStart = ConverterUtils.convertTimeInInt(columnTitle.substring(columnTitle.lastIndexOf('e') + 3, columnTitle.lastIndexOf('T') - 1));
                int timeEnd = ConverterUtils.convertTimeInInt(columnTitle.substring(columnTitle.lastIndexOf('o') + 2));
                String id = Generators.generateIdForTimetableEntry(lectureNo, semester, i);
                TimetableEntry entry = new TimetableEntry(id, timeStart, timeEnd, day, subject, lectureNo);
                timetableEntries.add(entry);
//                showSnackBar("For Subject: " + subject + " at day: " + day + " of lecture no: " + lectureNo + "time End is : " + timeEnd + "times tart is: " + timeStart);
            }
        }
        AppExecutors.getInstance().diskIO().execute(() -> {
            mDb.timetableDao().deleteWholeTimetable();
            mDb.timetableDao().insertAllTimeTableEntry(timetableEntries);
            mDb.attendanceDao().removeAttendanceAfter(new Date());
            //to remove creation of duplicate overall attendances
            mDb.overallAttendanceDao().deleteAllOverall();
            SetUpProcessRepository setUpProcessRepository = new SetUpProcessRepository(context);
            setUpProcessRepository.holidayInitialized(mDb.holidayDao().getHolidayDates(),
                    ConverterUtils.convertStringToDate(startDate),
                    ConverterUtils.convertStringToDate(setUpProcessRepository.getEndingDate()));
        });
    }

}
