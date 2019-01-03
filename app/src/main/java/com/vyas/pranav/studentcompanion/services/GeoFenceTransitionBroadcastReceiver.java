package com.vyas.pranav.studentcompanion.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class GeoFenceTransitionBroadcastReceiver extends BroadcastReceiver {

    private static final int RC_SHOW_NOTIFICATION = 123456;
    private static final int RC_OPEN_APP = 654321;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Just Entered GeoFence Now!", Toast.LENGTH_LONG).show();
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.d("Entered GeoFence Now");
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event.hasError()) {
            switch (event.getErrorCode()) {
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    Toast.makeText(context, "Oops..Error Occurred : Location not available", Toast.LENGTH_SHORT).show();
                    break;

                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    Toast.makeText(context, "Oops..Error Occurred : too Many Pending intent", Toast.LENGTH_SHORT).show();
                    break;

                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                    Toast.makeText(context, "Oops..Error Occurred : Too many GeoFences", Toast.LENGTH_SHORT).show();
                    break;
            }
            return;
        }
        if (event.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Toast.makeText(context, "Enter Occured", Toast.LENGTH_LONG).show();
            Logger.d("Entered in geo fence...");
            List<Geofence> geos = event.getTriggeringGeofences();
            List<String> ids = new ArrayList<>();
            for (int i = 0; i < geos.size(); i++) {
                Geofence geo = geos.get(i);
                Logger.d("GeoFence Id is " + geo.getRequestId());
                ids.add(geo.getRequestId());
            }
            showNotification(context);
        }

    }

    private void showNotification(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MainChannel";
            String description = "Show Main Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("NOTIFICATION_MAIN", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "NOTIFICATION_MAIN")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Entered")
                .setContentText("Entered in GepFences Now")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(getOpenAppAction(context))
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context.getApplicationContext());

        notificationManager.notify(RC_SHOW_NOTIFICATION, mBuilder.build());
    }

    /**
     * Gets action for notification
     *
     * @return Notification Action to open app
     */
    private NotificationCompat.Action getOpenAppAction(Context context) {
        Intent openAppIntent = new Intent(context, MainActivity.class);
        PendingIntent openAppFromNotification = PendingIntent.getActivity(context, RC_OPEN_APP, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return (new NotificationCompat.Action.Builder(R.drawable.ic_launcher_foreground, "Title", openAppFromNotification).build());
    }

}
