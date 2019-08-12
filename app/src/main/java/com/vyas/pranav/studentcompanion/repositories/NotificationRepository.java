package com.vyas.pranav.studentcompanion.repositories;
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

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.Query;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.NotificationDao;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.firestore.NotificationFirestoreModel;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;

import java.util.Date;
import java.util.List;

public class NotificationRepository {
    private static final Object LOCK = new Object();
    private static NotificationRepository instance;
    private final Context context;
    private final AppExecutors mExecutors;
    private final NotificationDao notificationDao;

    public NotificationRepository(Context context) {
        this.context = context;
        notificationDao = MainDatabase.getInstance(context).notificationDao();
        mExecutors = AppExecutors.getInstance();
    }

    public static NotificationRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new NotificationRepository(context.getApplicationContext());
            }
        }
        return instance;
    }

    public void insertNotification(NotificationFirestoreModel notification) {
        mExecutors.diskIO().execute(() -> notificationDao.insertNotification(notification));
    }

    public LiveData<List<NotificationFirestoreModel>> getAllNotifications() {
        return notificationDao.getAllNotifications();
    }

    public LiveData<Integer> getCurrentNotificationCount(Date date) {
        return notificationDao.getCurrentNotificationCount(date.getTime());
    }

    public FirestoreQueryLiveData getLiveFirestoreData(Query query) {
        return new FirestoreQueryLiveData(query);
    }

    public void syncNotifications(List<String> ids, List<NotificationFirestoreModel> notificationFirestoreModels) {
        Logger.d("Ids are: ");
        Logger.d(ids);
        mExecutors.diskIO().execute(() -> {
            List<NotificationFirestoreModel> lowAttendanceNotis = notificationDao.getNotificationByTypeInForeground(Constants.NOTI_TYPE_LOW_ATTENDANCE);
            notificationDao.deleteAllNotifications();
            for (int i = 0; i < ids.size(); i++) {
                notificationFirestoreModels.get(i).set_ID(ids.get(i));
                notificationFirestoreModels.get(i).setType(Constants.NOTI_TYPE_EVENT);
            }
            notificationDao.insertAllNotifications(lowAttendanceNotis);
            notificationDao.insertAllNotifications(notificationFirestoreModels);
        });
    }

    public LiveData<NotificationFirestoreModel> getNotificationById(String s) {
        return notificationDao.getNotificationById(s);
    }

    public void updateNotification(NotificationFirestoreModel notificationFirestoreModel) {
        mExecutors.diskIO().execute(() -> notificationDao.updateNotification(notificationFirestoreModel));
    }

    public void deleteNotificationById(String s) {
        mExecutors.diskIO().execute(() -> notificationDao.deleteNotification(s));
    }
}
