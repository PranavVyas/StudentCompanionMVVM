package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.vyas.pranav.studentcompanion.R;

import androidx.preference.PreferenceManager;

public class SharedPreferencesRepository {

    private static final String SHARED_PREF_ENDING_SEM = "END_SEM_DATE_STRING";
    private static final String SHARED_PREF_STARTING_SEM = "START_SEM_DATE_STRING";
    private static final String SHARED_PREF_CURRENT_STEP = "CURRENT_STEP_IN_SET_UP";
    private static final String SHARED_PREF_SUBJECTS_SET = "SUBJECT_LIST";
    private static final String SHARED_PREF_CURRENT_DAY = "CURRENT_DAY";
    private static final String SHARED_PREF_CURRENT_SEMESTER = "CURRENT_SEMESTER";
    private static final String SHARED_PREF_FIRST_RUN = "IS_FIRST_RUN";
    private static final String SHARED_PREF_NO_OF_LECTURES_PER_DAY = "NO_OF_LECTURES_PER_DAY";
    private static final String SHARED_PREF_LECTURE_START = "STARTING_TIME_OF_LECTURE";
    private static final String SHARED_PREF_LECTURE_END = "ENDING_TIME_OF_LECTURE";

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesRepository(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.apply();
        this.context = context;
    }

    public static void setUserTheme(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (mPrefs.getBoolean(context.getString(R.string.pref_key_switch_enable_night_mode), false)) {
            context.setTheme(R.style.AppTheme_Night);
        } else {
            context.setTheme(R.style.AppTheme);
        }
    }
}
