package com.vyas.pranav.studentcompanion.jobs;
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
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.services.ToggleSilentDeviceIntentService;
import com.vyas.pranav.studentcompanion.ui.activities.MainActivity;
import com.vyas.pranav.studentcompanion.ui.activities.SignInActivity;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.MainApp;

import java.util.concurrent.TimeUnit;

import static com.vyas.pranav.studentcompanion.utils.ConverterUtils.getCurrentTimeInMillis;

public class DailyJobForUnsilentAction extends Job {

    public static final String TAG = "DailyJobForUnsilentAction";

    public static void scheduleDeviceUnsilentAtTime(long time) {
        long currentTimeInMillis = getCurrentTimeInMillis();
        long timeInMillis = time - currentTimeInMillis;
        Logger.d("Time - current time = " + time + " - " + currentTimeInMillis + " = " + timeInMillis);
        if (timeInMillis < 0) {
            timeInMillis = (TimeUnit.HOURS.toMillis(24) + time) - currentTimeInMillis;
            Logger.d("New ()()() Time - current time = " + time + " - " + currentTimeInMillis + " + " + TimeUnit.HOURS.toMillis(24) + "= " + timeInMillis);
        }
        new JobRequest.Builder(TAG).setUpdateCurrent(false).setExact(timeInMillis).build().schedule();
    }

    public static void cancelAllJobs() {
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            JobManager.instance().cancelAllForTag(TAG);
        }
    }

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        sendNotification(getContext(), "Notification", "Device is in Normal mode now");
        Intent toggleSilentIntent = new Intent(getContext(), ToggleSilentDeviceIntentService.class);
        toggleSilentIntent.setAction(Constants.INTENT_ACTION_UNSILENT_DEVICE);
        getContext().startService(toggleSilentIntent);
        return Result.SUCCESS;
    }

    private NotificationCompat.Action getOpenAppAction() {
        Intent openAppIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent openAppFromNotification = PendingIntent.getActivity(getContext(), Constants.UNSILENT_JOB_RC_OPEN_APP, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return (new NotificationCompat.Action.Builder(R.drawable.logo_forground, "Open App Now", openAppFromNotification).build());
    }

    private NotificationCompat.Action getSilentAction() {
        Intent silentDevice = new Intent(getContext(), ToggleSilentDeviceIntentService.class);
        silentDevice.setAction(Constants.INTENT_ACTION_SILENT_DEVICE);
        PendingIntent silentDevicePendingIntent = PendingIntent.getService(getContext(), Constants.SILENT_JOB_RC_UNSILENT_ACTION, silentDevice, PendingIntent.FLAG_UPDATE_CURRENT);
        return (new NotificationCompat.Action.Builder(R.drawable.ic_market_place, "Silent Device", silentDevicePendingIntent).build());
    }

    private PendingIntent getContentIntent() {
        Intent intent = new Intent(getContext(), SignInActivity.class);
        return PendingIntent.getActivity(getContext(), Constants.SHOW_REMINDER_JOB_RC_CONTENT_INTENT, intent, 0);
    }

    @SuppressWarnings("SameParameterValue")
    private void sendNotification(Context context, @SuppressWarnings("SameParameterValue") String title, String desc) {
        Notification notification = new NotificationCompat.Builder(context, MainApp.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_forground)
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(getContentIntent())
                .addAction(getOpenAppAction())
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat.from(context).notify(Constants.UNSILENT_JOB_RC_SHOW_NOTIFICATION, notification);
    }
}
