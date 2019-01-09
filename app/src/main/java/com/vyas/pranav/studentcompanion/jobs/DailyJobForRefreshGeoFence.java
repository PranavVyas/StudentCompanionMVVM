package com.vyas.pranav.studentcompanion.jobs;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.repositories.GeoFencingRepository;
import com.vyas.pranav.studentcompanion.ui.activities.MainActivity;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class DailyJobForRefreshGeoFence extends DailyJob implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = "DailyJobForRefreshGeoFe";
    private static final int RC_OPEN_APP = 45489;
    private static final int RC_SHOW_NOTIFICATION = 151351;

    public static void scheduleJob() {
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            Logger.d("Job is scheduled Skipping check now");
            return;
        }
        long startTimeMillis = TimeUnit.HOURS.toMillis(0) + TimeUnit.MINUTES.toMillis(5);
        long endTimeMillis = startTimeMillis + TimeUnit.HOURS.toMillis(0) + TimeUnit.MINUTES.toMillis(5);
        JobRequest.Builder builder = new JobRequest.Builder(TAG).setUpdateCurrent(true);
        DailyJob.schedule(builder, startTimeMillis, endTimeMillis);
    }

    public static void cancelJob() {
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            JobManager.instance().cancelAllForTag(TAG);
        }
    }

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {
        Context context = getContext();
        GoogleApiClient mClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
        mClient.connect();
        GeoFencingRepository geoFencingRepository = new GeoFencingRepository(context, mClient);
        geoFencingRepository.refreshAllGeoFences();
        mClient.disconnect();
        //showNotification();
        mClient = null;
        return DailyJobResult.SUCCESS;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
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
                .setContentTitle("Refreshed GeoFences")
                .setContentText("Geofences refreshed now")
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
        return (new NotificationCompat.Action.Builder(R.drawable.ic_launcher_foreground, "Open App", openAppFromNotification).build());
    }
}
