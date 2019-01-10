package com.vyas.pranav.studentcompanion.jobs;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.services.AttendanceEditIntentService;
import com.vyas.pranav.studentcompanion.ui.activities.MainActivity;
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
        sendNotification(getContext(), "Add Today's Attendance", "Please Add today's Attendance Now");
        return DailyJobResult.SUCCESS;
    }

    /**
     * Gets action for notification
     *
     * @return Notification Action to open app
     */
    private NotificationCompat.Action getOpenAppAction() {
        Intent openAppIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent openAppFromNotification = PendingIntent.getActivity(getContext(), Constants.SHOW_REMINDER_JOB_RC_OPEN_APP, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return (new NotificationCompat.Action.Builder(R.drawable.ic_launcher_foreground, "Open App Now", openAppFromNotification).build());
    }

    private NotificationCompat.Action getMarkAllPresent() {
        Intent markAllAttendance = new Intent(getContext(), AttendanceEditIntentService.class);
        PendingIntent markAllAttendancePendingIntent = PendingIntent.getService(getContext(), Constants.SHOW_REMINDER_JOB_RC_MARK_ATTENDANCE, markAllAttendance, PendingIntent.FLAG_UPDATE_CURRENT);
        return (new NotificationCompat.Action.Builder(R.drawable.ic_market_place, "I am present all day", markAllAttendancePendingIntent).build());
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
                .addAction(getMarkAllPresent())
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat.from(context).notify(Constants.SHOW_REMINDER_JOB_RC_SHOW_NOTIFICATION, notification);
    }

}
