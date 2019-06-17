package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.NotificationEntry;
import com.vyas.pranav.studentcompanion.repositories.NotificationRepository;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;

import java.util.List;

public class NotificationsViewModel extends AndroidViewModel {

    private LiveData<List<NotificationEntry>> notifications;
    private NotificationRepository repository;
    private int selectedPage = 0;
    private FirestoreQueryLiveData firestoreQueryLiveData;

    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        repository = new NotificationRepository(application);
        notifications = repository.getAllNotifications();
        FirebaseFirestore mDb = FirebaseFirestore.getInstance();
        CollectionReference ref = mDb.collection("events");
        firestoreQueryLiveData = repository.getLiveFirestoreData(ref);
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

    public FirestoreQueryLiveData getFirestoreLiveData() {
        return firestoreQueryLiveData;
    }
}
