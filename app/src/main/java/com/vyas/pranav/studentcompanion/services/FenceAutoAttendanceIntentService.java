package com.vyas.pranav.studentcompanion.services;
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
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.awareness.fence.FenceState;
import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;
import com.vyas.pranav.studentcompanion.ui.activities.SignInActivity;
import com.vyas.pranav.studentcompanion.utils.AutoAttendanceHelper;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.MainApp;

public class FenceAutoAttendanceIntentService extends IntentService {

    public static final String SERVICE_AUTO_ATTENDANCE_FENCE_RUN = "com.pranav.vyas.FenceAutoAttendanceIntentService";

    public FenceAutoAttendanceIntentService() {
        super(SERVICE_AUTO_ATTENDANCE_FENCE_RUN);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Logger.addLogAdapter(new AndroidLogAdapter());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //TODO For now Just make jobs and this unEffective if the user is not signed in later try to remove them and enable them when user is signed in
        if (mAuth.getCurrentUser() == null) {
            Logger.d("User is not Signed in and fence is triggered");
            return;
        }
        FenceState fenceState = FenceState.extract(intent);
        SetUpProcessRepository repository = new SetUpProcessRepository(getApplicationContext());
        String subjectNameFromFence = fenceState.getFenceKey().substring(AutoAttendanceHelper.KEY_PRE_SUBJECT_FENCE.length());
        Logger.d("Subject Received in Fence Service is :" + subjectNameFromFence);
        for (String x :
                repository.getSubjectList()) {
            if (x.equals(subjectNameFromFence)) {
                Logger.d("For subject : " + x + " is same as subject received in service : " + subjectNameFromFence);
                checkFenceState(fenceState, getApplicationContext(), x);
                return;
            }
        }
    }

    void checkFenceState(FenceState fenceState, Context context, String subject) {
        switch (fenceState.getCurrentState()) {
            case FenceState.TRUE:
                Logger.d("Fence Detected : " + Constants.KEY_FENCE_LOCATION + subject);
                Toast.makeText(context, "You are in the Fence of Subject :" + subject, Toast.LENGTH_SHORT).show();
                sendNotification("Callback!", "Fence Available", "Just Received True callback from subject :" + subject);
                break;

            case FenceState.FALSE:
                Logger.d("Fence is Not Detected : " + Constants.KEY_FENCE_LOCATION + subject);
                sendNotification("Callback!", "Fence UnAvailable", "Just Received False callback from subject :" + subject);
                break;

            case FenceState.UNKNOWN:
                Logger.d("Fence Detected : " + Constants.KEY_FENCE_LOCATION + subject + " is in unknown state");
                sendNotification("Callback!", "Fence Unknown", "Just Received Unknown callback from subject :" + subject);
                break;
        }
    }

    private PendingIntent getContentIntent() {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        return PendingIntent.getActivity(getApplicationContext(), Constants.SHOW_NOTIFICATION_RC_CONTENT_INTENT, intent, 0);
    }

    private NotificationCompat.Action getOpenAppAction() {
        Intent openAppIntent = new Intent(getApplicationContext(), SignInActivity.class);
        PendingIntent openAppFromNotification = PendingIntent.getActivity(getApplicationContext(), Constants.SHOW_NOTIFICATION_RC_OPEN_APP, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return (new NotificationCompat.Action.Builder(R.drawable.logo_forground, "Open App Now", openAppFromNotification).build());
    }

    @SuppressWarnings("SameParameterValue")
    private void sendNotification(String title, String message, String bigText) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), MainApp.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_forground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(getContentIntent())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(bigText))
                .addAction(getOpenAppAction())
                .setAutoCancel(true);

        //todo debug
        NotificationManagerCompat.from(getApplicationContext()).notify((int) (Math.random() * 10000), notificationBuilder.build());
    }

}
