package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.Query;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.NotificationDao;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.NotificationDatabase;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.NotificationEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;

import java.util.Date;
import java.util.List;

public class NotificationRepository {
    private Context context;
    private AppExecutors mExecutors;
    private NotificationDao notificationDao;

    public NotificationRepository(Context context) {
        this.context = context;
        notificationDao = NotificationDatabase.getInstance(context).notificationDao();
        mExecutors = AppExecutors.getInstance();
    }

    public void insertNotification(NotificationEntry notification) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                notificationDao.insertNotification(notification);
            }
        });
    }

    public LiveData<List<NotificationEntry>> getAllNotifications() {
        return notificationDao.getAllNotifications();
    }

    public void deleteNotification(long id) {
        notificationDao.deleteNotification(id);
    }

    public LiveData<List<NotificationEntry>> getNotificationsAfter(Date date) {
        return notificationDao.getNotificationAfter(date);
    }

    public LiveData<Integer> getCurentNotificationCount(Date date) {
        return notificationDao.getCurrentNotificationCount(date);
    }

    public FirestoreQueryLiveData getLiveFirestoreData(Query query) {
        return new FirestoreQueryLiveData(query);
    }

}
