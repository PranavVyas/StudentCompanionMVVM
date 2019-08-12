package com.vyas.pranav.studentcompanion.viewmodels;
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
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.util.List;

public class NotificationsViewModel extends AndroidViewModel {

    private final NotificationRepository repository;
    private LiveData<List<NotificationFirestoreModel>> notifications;
    private int selectedPage = 0;
    private FirestoreQueryLiveData firestoreQueryLiveData;
    private LiveData<List<NotificationFirestoreModel>> allNotisFromDb;

    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        repository = NotificationRepository.getInstance(application);
        notifications = repository.getAllNotifications();
        FirebaseFirestore mDb = FirebaseFirestore.getInstance();
        CollectionReference ref = mDb.collection(SharedPreferencesUtils.getInstance(application).getCurrentPath() + Constants.PATH_EVENTS_SVNIT);
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
