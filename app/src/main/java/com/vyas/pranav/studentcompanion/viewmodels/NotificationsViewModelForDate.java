package com.vyas.pranav.studentcompanion.viewmodels;

import android.content.Context;

import com.vyas.pranav.studentcompanion.data.notificationdatabase.NotificationEntry;
import com.vyas.pranav.studentcompanion.repositories.NotificationRepository;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModelForDate extends ViewModel {

    private Date date;
    private Context context;
    private NotificationRepository notificationRepository;
    private LiveData<List<NotificationEntry>> notificationFromDate;

    public NotificationsViewModelForDate(Date date, Context context) {
        this.date = date;
        this.context = context;
        notificationRepository = new NotificationRepository(context);
        notificationFromDate = notificationRepository.getNotificationsAfter(date);
    }

    public LiveData<List<NotificationEntry>> getNotificationFromDate() {
        return notificationFromDate;
    }
}
