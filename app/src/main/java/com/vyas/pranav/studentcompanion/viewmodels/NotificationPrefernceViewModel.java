package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.messaging.FirebaseMessaging;
import com.vyas.pranav.studentcompanion.data.SharedPreferencesUtils;

public class NotificationPrefernceViewModel extends AndroidViewModel {

    private SharedPreferencesUtils sharedPreferencesUtils;
    private FirebaseMessaging instance;

    public NotificationPrefernceViewModel(@NonNull Application application) {
        super(application);
        sharedPreferencesUtils = new SharedPreferencesUtils(application);
        instance = FirebaseMessaging.getInstance();
    }


    public SharedPreferencesUtils getPrefenceUtils() {
        return sharedPreferencesUtils;
    }

    public FirebaseMessaging getInstance() {
        return instance;
    }
}
