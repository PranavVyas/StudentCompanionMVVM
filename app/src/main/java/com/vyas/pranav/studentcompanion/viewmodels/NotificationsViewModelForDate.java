package com.vyas.pranav.studentcompanion.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vyas.pranav.studentcompanion.data.notificationdatabase.NotificationEntry;
import com.vyas.pranav.studentcompanion.repositories.NotificationRepository;

import java.util.Date;
import java.util.List;

public class NotificationsViewModelForDate extends ViewModel {

    private Date date;
    private final Context context;
    private final NotificationRepository notificationRepository;
    private LiveData<List<NotificationEntry>> notificationFromDate;

    public NotificationsViewModelForDate(Date date, Context context) {
        this.date = date;
        this.context = context;
        notificationRepository = NotificationRepository.getInstance(context);
    }
}
