package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.firestore.NotificationFirestoreModel;
import com.vyas.pranav.studentcompanion.repositories.NotificationRepository;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;

import java.util.List;

public class NotificationsViewModel extends AndroidViewModel {

    private final NotificationRepository repository;
    private LiveData<List<NotificationFirestoreModel>> notifications;
    private int selectedPage = 0;
    private FirestoreQueryLiveData firestoreQueryLiveData;
    private LiveData<List<NotificationFirestoreModel>> allNotisFromDb;

    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        repository = new NotificationRepository(application);
        notifications = repository.getAllNotifications();
        FirebaseFirestore mDb = FirebaseFirestore.getInstance();
        CollectionReference ref = mDb.collection(Constants.PATH_EVENTS_SVNIT);
        firestoreQueryLiveData = repository.getLiveFirestoreData(ref);
        allNotisFromDb = repository.getAllNotifications();
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

    public void syncNotificationDatabase(List<String> ids, List<NotificationFirestoreModel> notificationFirestoreModels) {
        repository.syncNotifications(ids, notificationFirestoreModels);
    }

    public LiveData<List<NotificationFirestoreModel>> getAllNotisFromDb() {
        return allNotisFromDb;
    }
}
