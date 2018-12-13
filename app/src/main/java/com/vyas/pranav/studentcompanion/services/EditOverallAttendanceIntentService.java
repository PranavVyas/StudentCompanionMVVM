package com.vyas.pranav.studentcompanion.services;

import android.app.Application;
import android.app.IntentService;
import android.content.Intent;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.repositories.OverallAttendanceRepository;

import androidx.annotation.Nullable;

public class EditOverallAttendanceIntentService extends IntentService {

    public static final String SERVICE = "EDIT_OVERALL_ATTENDANCE";

    public EditOverallAttendanceIntentService() {
        super(SERVICE);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Application application = getApplication();
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        if (application == null) {
            Logger.d("Application Context is null in Service in applicaation");
        }
        if (getBaseContext() == null) {
            Logger.d("Application Context is null in base context");
        }
        OverallAttendanceRepository overallAttendanceRepository = new OverallAttendanceRepository(application);
        overallAttendanceRepository.refreshAllOverallAttendance();
    }
}
