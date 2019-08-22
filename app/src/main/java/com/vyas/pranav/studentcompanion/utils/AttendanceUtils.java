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
import com.vyas.pranav.studentcompanion.data.notificationdatabase.firestore.NotificationFirestoreModel;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.repositories.NotificationRepository;

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
        NotificationRepository notificationRepository = NotificationRepository.getInstance(applicationContext);
        SharedPreferencesUtils sharedPreferencesUtils = SharedPreferencesUtils.getInstance(applicationContext);
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                LiveData<NotificationFirestoreModel> notification = notificationRepository.getNotificationById("OverallAttendance_" + subjectAttendance.getSubName());
                notification.observeForever(new Observer<NotificationFirestoreModel>() {
                    @Override
                    public void onChanged(NotificationFirestoreModel notificationFirestoreModel) {
                        notification.removeObserver(this);
                        int totalDays = subjectAttendance.getTotalDays();
                        double presentPresent = (subjectAttendance.getPresentDays() * 100.0) / totalDays;
                        double warningPercent = 100 - ((100.0 - sharedPreferencesUtils.getCurrentAttendanceCriteria()) * 2 / 3);
                        if (presentPresent < warningPercent) {
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
                                notificationFirestoreModel.set_ID("OverallAttendance_" + subjectAttendance.getSubName());
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
                            Logger.d("Low Attendance");
                        } else {
                            if (notificationFirestoreModel.getType() == 0) {
                                notificationRepository.deleteNotificationById("OverallAttendance_" + subjectAttendance.getSubName());
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
}
