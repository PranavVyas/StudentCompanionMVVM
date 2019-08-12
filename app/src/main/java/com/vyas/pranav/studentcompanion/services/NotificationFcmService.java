package com.vyas.pranav.studentcompanion.services;
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
import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.activities.SignInActivity;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.MainApp;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class NotificationFcmService extends FirebaseMessagingService {

    private static final String TYPE_NEW_DOCUMENT = "1";
    private static final String TYPE_SELL_ITEM = "3";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> receivedData = remoteMessage.getData();
        String typeOfEvent = receivedData.get("typeOfEvent");
        if (typeOfEvent.equals(TYPE_NEW_DOCUMENT)) {
            String name = receivedData.get("nameOfEvent");
            String date = receivedData.get("dateOfEvent");
            String link = receivedData.get("linkOfEvent");
            String venue = receivedData.get("venueOfEvent");
            String shortInfo = receivedData.get("shortInfoOfEvent");
            Logger.addLogAdapter(new AndroidLogAdapter());
            Logger.d("name: " + name + "\ndate: " + date + "\ntype :" + typeOfEvent + "\nlink: " + link + "\nvenue: " + venue + "\nshort_info: " + shortInfo);
//        addToDatabase(name,typeOfEvent,link,venue,date,shortInfo);
            sendNotification(typeOfEvent, name, date, venue);
        } else if (typeOfEvent.equals(TYPE_SELL_ITEM)) {
            String category = receivedData.get("categoryOfThing");
            String name = receivedData.get("nameOfThing");
            String sellerName = receivedData.get("nameOfSeller");
            sendNotification(name, sellerName, category);
        }

    }

//    private void addToDatabase(String name, String typeOfEvent, String link, String venue, String date, String shortInfo) {
//        NotificationDao notificationDao = MainDatabase.getInstance(getApplicationContext()).notificationDao();
//        NotificationEntry notification = new NotificationEntry();
//        notification.setShort_info(shortInfo);
//        notification.setName(name);
//        notification.setUrl(link);
//        notification.setDate(date);
//        notification.setVenue(venue);
//        if (typeOfEvent.equals("1")) {
//            //Insert
//            notificationDao.insertNotification(notification);
//        } else if (typeOfEvent.equals("2")) {
//            deleteNotification(notificationDao.getUniqueNotification(date, name, shortInfo, link, venue), notificationDao);
//        }
//    }

//    private void deleteNotification(LiveData<NotificationEntry> uniqueNotification, NotificationDao notificationDao) {
//        AppExecutors.getInstance().mainThread().execute(new Runnable() {
//            @Override
//            public void run() {
//                uniqueNotification.observeForever(new Observer<NotificationEntry>() {
//                    @Override
//                    public void onChanged(NotificationEntry notificationEntry) {
//                        uniqueNotification.removeObserver(this);
//                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (notificationEntry != null) {
//                                    notificationDao.deleteNotification(notificationEntry);
//                                }
//                            }
//                        });
//                    }
//                });
//            }
//        });
//    }

    private PendingIntent getContentIntent() {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        return PendingIntent.getActivity(getApplicationContext(), Constants.SHOW_NOTIFICATION_RC_CONTENT_INTENT, intent, 0);
    }

    private NotificationCompat.Action getOpenAppAction() {
        Intent openAppIntent = new Intent(getApplicationContext(), SignInActivity.class);
        PendingIntent openAppFromNotification = PendingIntent.getActivity(getApplicationContext(), Constants.SHOW_NOTIFICATION_RC_OPEN_APP, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return (new NotificationCompat.Action.Builder(R.drawable.logo_forground, "Open App Now", openAppFromNotification).build());
    }

    private void sendNotification(String type, String name, String date, String venue) {
        Calendar calNow = Calendar.getInstance();
        calNow.setTime(new Date());
        Calendar calInNoti = Calendar.getInstance();
        calInNoti.setTime(ConverterUtils.convertStringToDate(date));
        if (calInNoti.after(calNow)) {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), MainApp.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo_forground)
                    .setContentTitle("New Event Added")
                    .setContentText("Recently Event : " + name + "has been added!\n Click to know more")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(getContentIntent())
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("New Event Added: " + name + "\nDate: " + date + "\nVenue: " + venue))
                    .addAction(getOpenAppAction())
                    .setAutoCancel(true);

            NotificationManagerCompat.from(getApplicationContext()).notify(Constants.EVENTS_SHOW_NOTIFICATION, notificationBuilder.build());
        }
    }

    private void sendNotification(String name, String sellerName, String category) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), MainApp.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_forground)
                .setContentTitle("New Item Added")
                .setContentText("New Item Added in Shop\n Click to know more")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(getContentIntent())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("New Item Added: " + name + "\nSeller Name : " + sellerName + "\nCategory: " + category))
                .addAction(getOpenAppAction())
                .setAutoCancel(true);

        NotificationManagerCompat.from(getApplicationContext()).notify(Constants.EVENTS_SHOW_NOTIFICATION, notificationBuilder.build());
    }

}
