package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.messaging.FirebaseMessaging;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

public class NotificationPrefernceViewModel extends AndroidViewModel {

    private final SharedPreferencesUtils sharedPreferencesUtils;
    private final FirebaseMessaging instance;

    public NotificationPrefernceViewModel(@NonNull Application application) {
        super(application);
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(application);
        instance = FirebaseMessaging.getInstance();
    }


    public SharedPreferencesUtils getPrefenceUtils() {
        return sharedPreferencesUtils;
    }

    public FirebaseMessaging getInstance() {
        return instance;
    }
}
