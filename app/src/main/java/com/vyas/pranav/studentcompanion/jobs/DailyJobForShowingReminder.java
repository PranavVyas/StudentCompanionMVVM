package com.vyas.pranav.studentcompanion.jobs;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.activities.MainActivity;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class DailyJobForShowingReminder extends DailyJob {
    public static final String TAG = "DailyJobForShowingRemin";
    private static final int RC_SHOW_NOTIFICATION = 1000;
    private static final int RC_OPEN_APP = 100;

    public static void scheduleReminderJob(int time) {
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            return;
        }
        JobRequest.Builder jobBuilder = new JobRequest.Builder(TAG).setUpdateCurrent(true);
        long startTime = TimeUnit.MINUTES.toMillis(time);
        long endTime = startTime + TimeUnit.MINUTES.toMillis(1);
        DailyJob.schedule(jobBuilder, startTime, endTime);
    }

    public static void cancelReminderJob() {
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            JobManager.instance().cancelAllForTag(TAG);
        }
    }

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {
        showNotification();
        return DailyJobResult.SUCCESS;
    }

    private void showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MainChannel";
            String description = "Show Main Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("NOTIFICATION_MAIN", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(), "NOTIFICATION_MAIN")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Added")
                .setContentText("Please fill today's attendance")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(getOpenAppAction())
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext().getApplicationContext());

        notificationManager.notify(RC_SHOW_NOTIFICATION, mBuilder.build());
    }

    /**
     * Gets action for notification
     *
     * @return Notification Action to open app
     */
    private NotificationCompat.Action getOpenAppAction() {
        Intent openAppIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent openAppFromNotification = PendingIntent.getActivity(getContext(), RC_OPEN_APP, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return (new NotificationCompat.Action.Builder(R.drawable.ic_launcher_foreground, "Title", openAppFromNotification).build());
    }

}
