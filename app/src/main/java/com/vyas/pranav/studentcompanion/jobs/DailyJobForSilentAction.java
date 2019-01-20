package com.vyas.pranav.studentcompanion.jobs;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

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

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.vyas.pranav.studentcompanion.utils.ConverterUtils.getCurrentTimeInMillis;

public class DailyJobForSilentAction extends Job {

    public static final String TAG = "DailyJobForSilentAction";


    public static void scheduleDeviceSilentAtTime(long time) {
        long currentTimeInMillis = getCurrentTimeInMillis();
        long timeInMillis = time - currentTimeInMillis;
        Logger.d("Time - current time = " + time + " - " + currentTimeInMillis + " = " + timeInMillis);
        if (timeInMillis < 0) {
            timeInMillis = (TimeUnit.HOURS.toMillis(24) + time) - currentTimeInMillis;
            Logger.d("New ()()() Time - current time = " + time + " - " + currentTimeInMillis + " + " + TimeUnit.HOURS.toMillis(24) + "= " + timeInMillis);
        }
        new JobRequest.Builder(TAG).setUpdateCurrent(false).setExact(timeInMillis).build().schedule();
    }
    //TODO Remove this job when user signs out

    public static void cancelAllJobs() {
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            JobManager.instance().cancelAllForTag(TAG);
        }
    }

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        sendNotification(getContext(), "Notification", "Device is Silent Now...");
        Intent toggleSilentIntent = new Intent(getContext(), ToggleSilentDeviceIntentService.class);
        toggleSilentIntent.setAction(Constants.INTENT_ACTION_SILENT_DEVICE);
        getContext().startService(toggleSilentIntent);
        return Result.SUCCESS;
    }

    private NotificationCompat.Action getOpenAppAction() {
        Intent openAppIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent openAppFromNotification = PendingIntent.getActivity(getContext(), Constants.SILENT_JOB_RC_OPEN_APP, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return (new NotificationCompat.Action.Builder(R.drawable.ic_launcher_foreground, "Open App Now", openAppFromNotification).build());
    }

    private NotificationCompat.Action getUnSilentAction() {
        Intent unSilentDevice = new Intent(getContext(), ToggleSilentDeviceIntentService.class);
        unSilentDevice.setAction(Constants.INTENT_ACTION_UNSILENT_DEVICE);
        PendingIntent unSilentDevicePendingIntent = PendingIntent.getService(getContext(), Constants.SILENT_JOB_RC_UNSILENT_ACTION, unSilentDevice, PendingIntent.FLAG_UPDATE_CURRENT);
        return (new NotificationCompat.Action.Builder(R.drawable.ic_market_place, "Unsilent Device", unSilentDevicePendingIntent).build());
    }

    private PendingIntent getContentIntent() {
        Intent intent = new Intent(getContext(), SignInActivity.class);
        return PendingIntent.getActivity(getContext(), Constants.SHOW_REMINDER_JOB_RC_CONTENT_INTENT, intent, 0);
    }

    private void sendNotification(Context context, String title, String desc) {
        Notification notification = new NotificationCompat.Builder(context, MainApp.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(getContentIntent())
                .addAction(getOpenAppAction())
                .addAction(getUnSilentAction())
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat.from(context).notify(Constants.SILENT_JOB_RC_SHOW_NOTIFICATION, notification);
    }

}
