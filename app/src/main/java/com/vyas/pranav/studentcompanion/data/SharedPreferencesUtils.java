package com.vyas.pranav.studentcompanion.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

public class SharedPreferencesUtils {

    public static final String SHARED_PREF_AUTO_SYNC_DIGITAL_LIBRARY = "AUTO_SYNC_IN_DIGITAL_LIBRARY";

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesUtils(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.apply();
        this.context = context;
    }


    public void changeAutoSync(boolean isEnabled) {
        editor.putBoolean(SHARED_PREF_AUTO_SYNC_DIGITAL_LIBRARY, isEnabled);
        editor.apply();
        Toast.makeText(context, "Auto Sync is " + (isEnabled ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
    }

    public boolean getStateOfAutoSync() {
        return preferences.getBoolean(SHARED_PREF_AUTO_SYNC_DIGITAL_LIBRARY, false);
    }
}
