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
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.utils.Constants;

public class ToggleSilentDeviceIntentService extends IntentService {

    private static final String TAG = "ToggleSilentDeviceIntentService";

    public ToggleSilentDeviceIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent.getAction().equals(Constants.INTENT_ACTION_UNSILENT_DEVICE)) {
            Logger.d("Device is normal now");
            Toast.makeText(this, "Device is normal now", Toast.LENGTH_SHORT).show();
            setRingerMode(getApplicationContext(), AudioManager.RINGER_MODE_NORMAL);
        } else if (intent.getAction().equals(Constants.INTENT_ACTION_SILENT_DEVICE)) {
            Logger.d("Device is silent now");
            Toast.makeText(this, "Device is silent noe", Toast.LENGTH_SHORT).show();
            setRingerMode(getApplicationContext(), AudioManager.RINGER_MODE_SILENT);
        }
    }

    private void setRingerMode(Context context, int mode) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT < 24 ||
                (nm.isNotificationPolicyAccessGranted() && Build.VERSION.SDK_INT >= 24)) {
            //We have permission to change device mode
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(mode);
        }
    }

}
