package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.jobs.DailyJobForShowingReminder;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import androidx.preference.PreferenceManager;

public class AppSettingsRepository {

    private Context context;
    private SharedPreferences mPreference;
    private SharedPreferences.Editor mEditor;

    public AppSettingsRepository(Context context) {
        this.context = context;
        mPreference = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPreference.edit();
    }

    public boolean isReminderEnabled() {
        return mPreference.getBoolean(context.getString(R.string.pref_key_switch_enable_reminder), context.getResources().getBoolean(R.bool.pref_def_value_switch_enable_reminder));
    }

    public int getReminderTime() {
        return mPreference.getInt(context.getString(R.string.pref_key_time_reminder_time), context.getResources().getInteger(R.integer.pref_def_value_time_reminder_time));
    }

    public void cancelReminderJob() {
        DailyJobForShowingReminder.cancelReminderJob();
        Toast.makeText(context, "Reminder Canceled!", Toast.LENGTH_SHORT).show();
    }

    public void setReminderJob(int timeInMinutes) {
        DailyJobForShowingReminder.scheduleReminderJob(timeInMinutes);
        Toast.makeText(context, "Reminder set for time : " + ConverterUtils.convertTimeIntInString(timeInMinutes), Toast.LENGTH_SHORT).show();
    }
}
