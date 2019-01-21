package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import com.vyas.pranav.studentcompanion.data.notificationdatabase.NotificationEntry;
import com.vyas.pranav.studentcompanion.repositories.NotificationRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class NotificationsViewModel extends AndroidViewModel {

    private LiveData<List<NotificationEntry>> notifications;
    private NotificationRepository repository;
    private int selectedPage = 0;

    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        repository = new NotificationRepository(application);
        notifications = repository.getAllNotifications();
    }

    public LiveData<List<NotificationEntry>> getAllNotifications() {
        return notifications;
    }

    public void deleteNotification(long id) {
        repository.deleteNotification(id);
    }

    public int getSelectedPage() {
        return selectedPage;
    }

    public void setSelectedPage(int position) {
        this.selectedPage = position;
    }
}
