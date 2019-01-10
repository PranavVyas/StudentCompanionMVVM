package com.vyas.pranav.studentcompanion.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import androidx.annotation.Nullable;

public class ToggleSilentDeviceIntentService extends IntentService {

    private static final String TAG = "ToggleSilentDeviceIntentService";

    public ToggleSilentDeviceIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent.getAction().equals("ACTION_UNSILENT_DEVICE")) {
            Logger.d("Device is normal now");
            Toast.makeText(this, "Device is normal now", Toast.LENGTH_SHORT).show();
            setRingerMode(getApplicationContext(), AudioManager.RINGER_MODE_NORMAL);
        } else if (intent.getAction().equals("ACTION_SILENT_DEVICE")) {
            Logger.d("Device is silent now");
            Toast.makeText(this, "Device is silent noe", Toast.LENGTH_SHORT).show();
            setRingerMode(getApplicationContext(), AudioManager.RINGER_MODE_SILENT);
        }
    }

    private void setRingerMode(Context context, int mode) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT < 24 ||
                (nm.isNotificationPolicyAccessGranted() && Build.VERSION.SDK_INT >= 24)) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(mode);
        }
    }

}
