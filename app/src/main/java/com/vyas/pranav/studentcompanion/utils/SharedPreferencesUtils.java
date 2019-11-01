package com.vyas.pranav.studentcompanion.utils;
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

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.vyas.pranav.studentcompanion.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SharedPreferencesUtils {

    public static final String SHARED_PREF_AUTO_SYNC_DIGITAL_LIBRARY = "AUTO_SYNC_IN_DIGITAL_LIBRARY";
    private static final String SHARED_PREF_KEY_EVENT_NOTIFICATION = "NOTIFICATION_FOR_EVENT";
    private static final String SHARED_PREF_KEY_NEW_ITEM_NOTIFICATION = "NOTIFICATION_FOR_NEW_ITEM";
    private static final String SHARED_PREF_CURRENT_NOTIFICATION = "CURRENT_NOTIFICATIONS";
    private static final String SHARED_PREF_FIRST_OPEN_COMPONENT = "FIRST_OPEN_COMPONENT_";
    private static final String SHARED_PREF_REMINDER_JOB_TIME = "SHARED_PREFERENCE_REMINDER_TIME";
    private static final String SHARED_PREF_AUTO_SMART_SILENT_LAST_STATE = "SILENT_LAST_STATE";
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
    private static final String SHARED_PREF_TUTORIAL = "TUTORIAL_DONE";
    private static final String KEY_ATTENDANCE_CRITERIA = "SHARED_PREF_ATTENDANCE_CRITERIA";
    private static final String PATH_MAIN_SOURCE_FIRESTORE = "SHARED_PREF.FirestorePath";
    private static final String SHARED_PREF_RESTORE_DONE = "SHARED_PREF_IS_RESTORED";
    public static final String SHARED_PREF_IS_DEVELOPER_ENABLED = "SHARED_PREF_IS_DEVELOPER_ENABLED";


    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final Object LOCK = new Object();

    private static SharedPreferencesUtils instance;

    public static SharedPreferencesUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new SharedPreferencesUtils(context.getApplicationContext());
            }
        }
        return instance;
    }

    public SharedPreferencesUtils(Context context) {
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
        return preferences.getInt(KEY_ATTENDANCE_CRITERIA, 0);
    }

    public boolean isFileFirstOpened(String Filename) {
        return preferences.getBoolean(SHARED_PREF_FIRST_OPEN_COMPONENT + Filename, true);
    }

    public void setFileFirstTimeOpened(String Filename, boolean isFirstTimeOpened) {
        editor.putBoolean(SHARED_PREF_FIRST_OPEN_COMPONENT + Filename, isFirstTimeOpened);
        editor.apply();
    }

    public void setCurrentAttendanceCriteria(int progress) {
        editor.putInt(KEY_ATTENDANCE_CRITERIA, progress).apply();
    }

    public long getReminderJobTime() {
        return preferences.getLong(SHARED_PREF_REMINDER_JOB_TIME, -1);
    }

    public void setReminderJobTime(long timeInMillis) {
        editor.putLong(SHARED_PREF_REMINDER_JOB_TIME, timeInMillis);
        editor.apply();
    }

    public boolean getLastSilentState() {
        return preferences.getBoolean(SHARED_PREF_AUTO_SMART_SILENT_LAST_STATE, false);
    }

    public void setLastSilentState(boolean isOn) {
        editor.putBoolean(SHARED_PREF_AUTO_SMART_SILENT_LAST_STATE, isOn);
        editor.apply();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean isAppFirstRun() {
        return preferences.getBoolean(SHARED_PREF_FIRST_RUN, true);
    }

    public void setAppFirstRun(boolean isAppFirstRun) {
        editor.putBoolean(SHARED_PREF_FIRST_RUN, isAppFirstRun);
        editor.apply();
    }

    public void setUpEndingDate(String endDateStr) {
        editor.putString(SHARED_PREF_ENDING_SEM, endDateStr);
        editor.apply();
    }

    public void setUpStartingDate(String startDateStr) {
        editor.putString(SHARED_PREF_STARTING_SEM, startDateStr);
        editor.apply();
    }

    public String getStartingDate() {
        return preferences.getString(SHARED_PREF_STARTING_SEM, null);
    }

    public String getEndingDate() {
        return preferences.getString(SHARED_PREF_ENDING_SEM, null);
    }

    public void setUpCurrentStep(int step) {
        editor.putInt(SHARED_PREF_CURRENT_STEP, step);
        editor.apply();
    }

    public int getSetUpCurrentStep() {
        return preferences.getInt(SHARED_PREF_CURRENT_STEP, 1);
    }

    public void setSubjectListInSharedPrefrences(List<String> subjects) {
        editor.putStringSet(SHARED_PREF_SUBJECTS_SET, new HashSet<>(subjects));
        editor.apply();
    }

    public List<String> getSubjectList() {
        List<String> subjects = new ArrayList<>(preferences.getStringSet(SHARED_PREF_SUBJECTS_SET, new HashSet<>()));
        if (subjects.isEmpty()) {
            return new ArrayList<>();
        } else {
            return subjects;
        }
    }

    public int getCurrentDay() {
        return preferences.getInt(SHARED_PREF_CURRENT_DAY, 1);
    }

    public void setCurrentDay(int currentDay) {
        editor.putInt(SHARED_PREF_CURRENT_DAY, currentDay);
        editor.apply();
    }

    public void setUpSemester(int semester) {
        editor.putInt(SHARED_PREF_CURRENT_SEMESTER, semester);
        editor.apply();
    }

    public int getSemester() {
        return preferences.getInt(SHARED_PREF_CURRENT_SEMESTER, 1);
    }

    public void setLectureStartTimeInSharedPrefs(int lectureNo, int startTime) {
        editor.putInt(SHARED_PREF_LECTURE_START + lectureNo, startTime);
        editor.apply();
    }

    public void setLectureEndTimeInSharedPrefs(int lectureNo, int endTime) {
        editor.putInt(SHARED_PREF_LECTURE_END + lectureNo, endTime);
        editor.apply();
    }

    public int getStartTimeForLecture(int lectureNo) {
        return preferences.getInt(SHARED_PREF_LECTURE_START + lectureNo, 0);
    }

    public int getEndTimeForLecture(int lectureNo) {
        return preferences.getInt(SHARED_PREF_LECTURE_END + lectureNo, 60);
    }

    public int getNoOfLecturesPerDay() {
        return preferences.getInt(SHARED_PREF_NO_OF_LECTURES_PER_DAY, 4);
    }

    public void setNoOfLecturesPerDay(int noOfLecturesPerDay) {
        editor.putInt(SHARED_PREF_NO_OF_LECTURES_PER_DAY, noOfLecturesPerDay);
        editor.apply();
    }

    public boolean isTutorialDone() {
        return preferences.getBoolean(SHARED_PREF_TUTORIAL, false);
    }

    public void setTutorialDone(boolean isDone) {
        editor.putBoolean(SHARED_PREF_TUTORIAL, isDone).apply();
    }

    public String getCurrentPath() {
        return preferences.getString(PATH_MAIN_SOURCE_FIRESTORE, "surat/svnit");
    }

    public void setCurrentPath(String currentPath) {
        editor.putString(PATH_MAIN_SOURCE_FIRESTORE, currentPath).apply();
    }

    public void clearSharedPreference() {
        editor.clear();
    }

    public boolean isRestoreDone() {
        return preferences.getBoolean(SHARED_PREF_RESTORE_DONE, false);
    }

    public void setRestoreDone(boolean b) {
        editor.putBoolean(SHARED_PREF_RESTORE_DONE, b);
    }

    public boolean isDeveloperEnabled() {
        return preferences.getBoolean(SHARED_PREF_IS_DEVELOPER_ENABLED, true);
    }

    public void setDeveloperEnabled(boolean isDeveloperEnabled) {
        editor.putBoolean(SHARED_PREF_IS_DEVELOPER_ENABLED, isDeveloperEnabled).apply();
    }

    public boolean isAutoAttendanceEnabled() {
        return preferences.getBoolean(context.getString(R.string.pref_key_switch_enable_auto_attendance), context.getResources().getBoolean(R.bool.pref_def_value_switch_enable_auto_attendance));
    }

    public boolean isReminderEnabled() {
        return preferences.getBoolean(context.getString(R.string.pref_key_switch_enable_reminder), context.getResources().getBoolean(R.bool.pref_def_value_switch_enable_reminder));
    }

    public int getReminderTime() {
        return preferences.getInt(context.getString(R.string.pref_key_time_reminder_time), context.getResources().getInteger(R.integer.pref_def_value_time_reminder_time));
    }

    public boolean isSmartSilentEnabled() {
        return preferences.getBoolean(context.getString(R.string.pref_key_switch_enable_smart_silent), false);
    }

    public void toggleNightMode() {
        if (preferences.getBoolean(context.getString(R.string.pref_key_switch_enable_night_mode), false)) {
            context.setTheme(R.style.AppTheme_Night);
        } else {
            context.setTheme(R.style.AppTheme);
        }
    }

    public void setSmartSilentEnabled(boolean isOn) {
        editor.putBoolean(context.getString(R.string.pref_key_switch_enable_smart_silent), isOn);
        editor.apply();
    }
}
