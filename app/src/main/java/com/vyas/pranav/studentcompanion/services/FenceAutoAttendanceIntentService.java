package com.vyas.pranav.studentcompanion.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.awareness.fence.FenceState;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;
import com.vyas.pranav.studentcompanion.ui.activities.SignInActivity;
import com.vyas.pranav.studentcompanion.utils.AutoAttendanceHelper;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.MainApp;

public class FenceAutoAttendanceIntentService extends IntentService {

    public static final String SERVICE_AUTO_ATENDANCE_FENCE_RUN = "com.pranav.vyas.FenceAutoAttendanceIntentService";

    public FenceAutoAttendanceIntentService() {
        super(SERVICE_AUTO_ATENDANCE_FENCE_RUN);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Logger.addLogAdapter(new AndroidLogAdapter());
        FenceState fenceState = FenceState.extract(intent);
        SetUpProcessRepository repository = new SetUpProcessRepository(getApplicationContext());
        String subjectNameFromFence = fenceState.getFenceKey().substring(AutoAttendanceHelper.KEY_PRE_SUBJECT_FENCE.length());
        for (String x :
                repository.getSubjectList()) {
            if (x.equals(subjectNameFromFence)) {
                checkFenceState(fenceState, getApplicationContext(), x);
            }
        }
    }

    void checkFenceState(FenceState fenceState, Context context, String subject) {
        switch (fenceState.getCurrentState()) {
            case FenceState.TRUE:
                Logger.d("Fence Detected : " + Constants.KEY_FENCE_LOCATION);
                Toast.makeText(context, "You are in the Fence of Subject :" + subject, Toast.LENGTH_SHORT).show();
                sendNotification("Callback!", "Fence Available", "Just Received True callback from subject :" + subject);
                break;

            case FenceState.FALSE:
                Logger.d("Fence is Not Detected : " + Constants.KEY_FENCE_LOCATION);
                sendNotification("Callback!", "Fence UnAvailable", "Just Received False callback from subject :" + subject);
                break;

            case FenceState.UNKNOWN:
                Logger.d("Fence Detected : " + Constants.KEY_FENCE_LOCATION + " is in unknown state");
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

        NotificationManagerCompat.from(getApplicationContext()).notify(Constants.FENCE_CALLBACK_NOTIFICATION, notificationBuilder.build());
    }

}
