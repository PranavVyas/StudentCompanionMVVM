package com.vyas.pranav.studentcompanion.jobs;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.services.AttendanceEditIntentService;
import com.vyas.pranav.studentcompanion.ui.activities.SignInActivity;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.MainApp;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class DailyJobForShowingReminder extends DailyJob {
    public static final String TAG = "DailyJobForShowingReminder";


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
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            sendNotification(getContext(), "Add Today's Attendance", "Please Add today's Attendance Now", getOpenAppAction(), getMarkAllPresentAction(), getContentIntent());
        } else {
            sendNotification(getContext(), "Add Today's Attendance", "Please Log in and Add today's Attendance Now", getOpenAppAction(), null, getContentIntent());
        }
        return DailyJobResult.SUCCESS;
    }

    private NotificationCompat.Action getOpenAppAction() {
        Intent openAppIntent = new Intent(getContext(), SignInActivity.class);
        PendingIntent openAppFromNotification = PendingIntent.getActivity(getContext(), Constants.SHOW_REMINDER_JOB_RC_OPEN_APP, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return (new NotificationCompat.Action.Builder(R.drawable.logo_forground, "Open App Now", openAppFromNotification).build());
    }

    private NotificationCompat.Action getMarkAllPresentAction() {
        Intent markAllAttendance = new Intent(getContext(), AttendanceEditIntentService.class);
        PendingIntent markAllAttendancePendingIntent = PendingIntent.getService(getContext(), Constants.SHOW_REMINDER_JOB_RC_MARK_ATTENDANCE, markAllAttendance, PendingIntent.FLAG_UPDATE_CURRENT);
        return (new NotificationCompat.Action.Builder(R.drawable.ic_market_place, "I am present all day", markAllAttendancePendingIntent).build());
    }

    private PendingIntent getContentIntent() {
        Intent intent = new Intent(getContext(), SignInActivity.class);
        return PendingIntent.getActivity(getContext(), Constants.SHOW_REMINDER_JOB_RC_CONTENT_INTENT, intent, 0);
    }

    private void sendNotification(Context context, String title, String desc, NotificationCompat.Action action1, NotificationCompat.Action action2, PendingIntent contentIntent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, MainApp.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_forground)
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .addAction(action1)
                .setAutoCancel(true);

        if (action2 != null) {
            notificationBuilder.addAction(action2);
        }

        NotificationManagerCompat.from(context).notify(Constants.SHOW_REMINDER_JOB_RC_SHOW_NOTIFICATION, notificationBuilder.build());
    }

}
