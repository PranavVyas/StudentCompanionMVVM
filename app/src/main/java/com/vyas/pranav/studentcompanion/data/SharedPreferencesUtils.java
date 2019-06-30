package com.vyas.pranav.studentcompanion.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;

public class SharedPreferencesUtils {

    public static final String SHARED_PREF_AUTO_SYNC_DIGITAL_LIBRARY = "AUTO_SYNC_IN_DIGITAL_LIBRARY";
    public static final String SHARED_PREF_KEY_EVENT_NOTIFICATION = "NOTIFICATION_FOR_EVENT";
    public static final String SHARED_PREF_KEY_NEW_ITEM_NOTIFICATION = "NOTIFICATION_FOR_NEW_ITEM";
    private static final String SHARED_PREF_CURRENT_NOTIFICATION = "CURRENT_NOTIFICATIONS";
    private static final String SHARED_PREF_FIRST_OPEN_COMPONENT = "FIRST_OPEN_COMPONENT_";


    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesUtils(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.apply();
        this.context = context;
    }

    public int getCurrentNotis() {
        return preferences.getInt(SHARED_PREF_CURRENT_NOTIFICATION, 0);
    }

    public void setCurrentNotis(int notis) {
        editor.putInt(SHARED_PREF_CURRENT_NOTIFICATION, notis);
        editor.apply();
    }

    public boolean isEventNotificationEnabed() {
        return preferences.getBoolean(SHARED_PREF_KEY_EVENT_NOTIFICATION, false);
    }

    public boolean isNewItemShopNotificationEnabled() {
        return preferences.getBoolean(SHARED_PREF_KEY_NEW_ITEM_NOTIFICATION, false);
    }

    public void setNotificationForEvent(boolean isEnabled) {
        editor.putBoolean(SHARED_PREF_KEY_EVENT_NOTIFICATION, isEnabled);
        editor.apply();
    }

    public void setNotificationForNewItemInShop(boolean isEnabled) {
        editor.putBoolean(SHARED_PREF_KEY_NEW_ITEM_NOTIFICATION, isEnabled);
        editor.apply();
    }

    public void changeAutoSync(boolean isEnabled) {
        editor.putBoolean(SHARED_PREF_AUTO_SYNC_DIGITAL_LIBRARY, isEnabled);
        editor.apply();
        Toast.makeText(context, "Auto Sync is " + (isEnabled ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
    }

    public boolean getStateOfAutoSync() {
        return preferences.getBoolean(SHARED_PREF_AUTO_SYNC_DIGITAL_LIBRARY, false);
    }

    public int getCurrentAttendanceCriteria() {
        return preferences.getInt(SetUpProcessRepository.KEY_ATTENDANCE_CRITERIA, 0);
    }

    public boolean isFileFirstOpened(String Filename) {
        return preferences.getBoolean(SHARED_PREF_FIRST_OPEN_COMPONENT + Filename, true);
    }

    public void setFileFirstTimeOpened(String Filename, boolean isFirstTimeOpened) {
        editor.putBoolean(SHARED_PREF_FIRST_OPEN_COMPONENT + Filename, isFirstTimeOpened);
        editor.apply();
    }
}
