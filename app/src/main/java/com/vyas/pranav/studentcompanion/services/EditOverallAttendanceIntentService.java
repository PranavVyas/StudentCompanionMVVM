package com.vyas.pranav.studentcompanion.services;

import android.app.Application;
import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class EditOverallAttendanceIntentService extends IntentService {

    public static final String SERVICE = "EDIT_OVERALL_ATTENDANCE";
    private static final int RC_SHOW_NOTIFICATION = 1000;
    private static final int RC_OPEN_APP = 2000;

    public EditOverallAttendanceIntentService() {
        super(SERVICE);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Application application = getApplication();

    }


}
