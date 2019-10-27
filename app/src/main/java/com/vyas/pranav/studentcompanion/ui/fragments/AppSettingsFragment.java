package com.vyas.pranav.studentcompanion.ui.fragments;
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

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.google.android.material.snackbar.Snackbar;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.ui.activities.AutoAttendanceSubjectListActivity;
import com.vyas.pranav.studentcompanion.ui.activities.DeveloperActivity;
import com.vyas.pranav.studentcompanion.ui.activities.ImportExportActivity;
import com.vyas.pranav.studentcompanion.ui.activities.NotificationPreferenceActivity;
import com.vyas.pranav.studentcompanion.utils.AutoAttendanceHelper;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.utils.TimePreference;
import com.vyas.pranav.studentcompanion.utils.TimePreferenceDialogFragmentCompat;
import com.vyas.pranav.studentcompanion.viewmodels.AppSettingsViewModel;

import java.util.List;

import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class AppSettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private AppSettingsViewModel appSettingsViewModel;
    private AutoAttendanceHelper helper;
    private OnDatabaseImportedListener mCallback;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_settings, rootKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        helper = new AutoAttendanceHelper(getContext());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Register the listener
     *
     * @param savedInstanceState saved state of the fragment
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * Unregister the Preference change listener
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        setTimePrefSummery(s);
        if (s.equals(getString(R.string.pref_key_switch_enable_reminder))) {
            setSelectTimeStateFromViewModel();
            setReminderIfEnabled();
        } else if (s.equals(getString(R.string.pref_key_time_reminder_time))) {
            appSettingsViewModel.cancelReminderJob();
            if (appSettingsViewModel.setReminderJobTime(getTimeFromViewModel())) {
                showSnackbar("Daily Reminder set successfully at " + ConverterUtils.convertTimeIntInString(appSettingsViewModel.getReminderTime()));
            }
        } else if (s.equals(getString(R.string.pref_key_switch_enable_auto_attendance))) {
//            checkAutoAttendanceStateAndExecute();
//            appSettingsViewModel.setRefreshGeoFence(appSettingsViewModel.isAutoAttendanceEnabled());
            setEditAutoAttendanceStateFromViewModel();
            setFence(appSettingsViewModel.isAutoAttendanceEnabled());
        } else if (s.equals(getString(R.string.pref_key_switch_enable_night_mode))) {
            toggleNightMode();
        } else if (s.equals(getString(R.string.pref_key_switch_enable_smart_silent))) {
//            checkAndApplySmartSilent();
        } else if (s.equals(SharedPreferencesUtils.SHARED_PREF_IS_DEVELOPER_ENABLED)) {
            checkForDeveloperTools();
        }
    }

    private void setFence(boolean isToRegister) {
        appSettingsViewModel.getAutoAttendanceLiveData().observe(getActivity(), new Observer<List<AutoAttendancePlaceEntry>>() {
            @Override
            public void onChanged(List<AutoAttendancePlaceEntry> autoAttendancePlaceEntries) {
                for (AutoAttendancePlaceEntry x :
                        autoAttendancePlaceEntries) {
                    helper.updateOrRemoveFenceForSubject(isToRegister, x.getSubject(), x.getLat(), x.getLang());
                }
            }
        });
    }


    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        // Try if the preference is one of our custom Preferences
        DialogFragment dialogFragment = null;
        if (preference instanceof TimePreference) {
            // Create a new instance of TimePreferenceDialogFragment with the key of the relate Preference
            dialogFragment = TimePreferenceDialogFragmentCompat
                    .getNewInstance(preference.getKey());
        }

        // If it was one of our custom Preferences, show its dialog
        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(),
                    "android.support.v7.preference.PreferenceFragment.DIALOG");
        }
        // Could not be handled here. Try with the super method.
        else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    private void setTimePrefSummery(String key) {
        //show time from the sharedPrefs
        Preference preference = getPreferenceManager().findPreference(key);
        if (preference != null) {
            if (preference instanceof TimePreference) {
                TimePreference mTimePreference = (TimePreference) preference;
                int time = getTimeFromViewModel();
                String timeStr = ConverterUtils.convertTimeIntInString(time);
                mTimePreference.setSummary(timeStr);
            }
        }
    }

    private void setReminderIfEnabled() {
        boolean isEnabled = appSettingsViewModel.isReminderEnabled();
        if (isEnabled) {
            if (appSettingsViewModel.setReminderJobTime(getTimeFromViewModel())) {
                showSnackbar("Daily Reminder set successfully at " + ConverterUtils.convertTimeIntInString(appSettingsViewModel.getReminderTime()));
            }
        } else {
            if (appSettingsViewModel.cancelReminderJob()) {
                showSnackbar("Reminder Cancelled!");
            }
        }
    }

    /**
     * Set up time preference according to the state of switch
     * If switch is enabled set up the time picker true and vise versa
     */
    private void setSelectTimeStateFromViewModel() {
        boolean isEnabled = appSettingsViewModel.isReminderEnabled();
        if (isEnabled) {
            (findPreference(getString(R.string.pref_key_time_reminder_time))).setEnabled(true);
        } else {
            (findPreference(getString(R.string.pref_key_time_reminder_time))).setEnabled(false);

        }
    }

    private int getTimeFromViewModel() {
        return appSettingsViewModel.getReminderTime();
    }

    private void toggleNightMode() {
        getActivity().recreate();
    }

    private void setEditAutoAttendanceStateFromViewModel() {
        boolean isAutoAttendanceEnabled = appSettingsViewModel.isAutoAttendanceEnabled();
        if (isAutoAttendanceEnabled) {
            findPreference(getString(R.string.pref_key_select_places_auto_attendance)).setEnabled(true);
        } else {
            findPreference(getString(R.string.pref_key_select_places_auto_attendance)).setEnabled(false);
        }
    }

    private boolean isPermissionGranted() {
        NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        return Build.VERSION.SDK_INT < 24 || nm.isNotificationPolicyAccessGranted();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mCallback = (OnDatabaseImportedListener) context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appSettingsViewModel = ViewModelProviders.of(getActivity()).get(AppSettingsViewModel.class);
        setTimePrefSummery(getContext().getString(R.string.pref_key_time_reminder_time));
        ButterKnife.bind(this, view);
        setSelectTimeStateFromViewModel();
        setEditAutoAttendanceStateFromViewModel();
        //Select Places for Auto Attendance is Clicked
        findPreference(getString(R.string.pref_key_select_places_auto_attendance)).setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getContext(), AutoAttendanceSubjectListActivity.class);
            startActivity(intent);
            return false;
        });
        //Smart Silent is Clicked
        findPreference(getString(R.string.pref_key_switch_enable_smart_silent)).setOnPreferenceClickListener(preference -> checkAndApplySmartSilent());

        //Notification is clicked
        findPreference(getString(R.string.pref_key_list_notification)).setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getContext(), NotificationPreferenceActivity.class);
            startActivity(intent);
            return true;
        });
        //Backup or Restore is clicked
        findPreference(getString(R.string.pref_key_backup_database)).setOnPreferenceClickListener((preference) -> {
            Intent intent = new Intent(getContext(), ImportExportActivity.class);
            intent.putExtra(ImportExportActivity.EXTRA_FORCE_START_ACTIVITY, true);
            startActivityForResult(intent, Constants.RC_OPEN_BACKUP_RESTORE_ACTIVITY, null);
            return false;
        });
        checkForDeveloperTools();
    }

    private void checkForDeveloperTools() {
        if (!appSettingsViewModel.isDeveloperEnabled()) {
            if (findPreference(getString(R.string.pref_key_developer_tools)) != null) {
                findPreference(getString(R.string.pref_key_developer_tools)).setVisible(false);
//                getPreferenceScreen().removePreference(findPreference(getString(R.string.pref_key_developer_tools)));
            }
        } else {
            if (findPreference(getString(R.string.pref_key_developer_tools)) == null) {
                findPreference(getString(R.string.pref_key_developer_tools)).setVisible(true);
//                getPreferenceScreen().addPreference(findPreference(getString(R.string.pref_key_developer_tools)));
            }
            findPreference(getString(R.string.pref_key_developer_tools)).setOnPreferenceClickListener((preference -> {
                Intent intent = new Intent(getContext(), DeveloperActivity.class);
                startActivity(intent);
                return false;
            }));
        }
    }

    private boolean checkAndApplySmartSilent() {
        if (isPermissionGranted()) {
            if (appSettingsViewModel.toggleSmartSilent()) {
                showSnackbar("Enabled Smart Silent, Your device will automatically silent from next time");
            } else {
                showSnackbar("Disabled Smart Silent");
            }
            return true;
        } else {
            //We can not change value here..We need to revert back now

            Toast.makeText(getContext(), "Permission is not granted\nPlease Grant Do Not Disturb Access to Student Companion", Toast.LENGTH_LONG).show();
            NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= 24 && !nm.isNotificationPolicyAccessGranted()) {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivityForResult(intent, Constants.RC_SETTINGS_SILENT_DEVICE, null);
            }
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SETTINGS_SILENT_DEVICE) {
            ((SwitchPreference) getPreferenceScreen().findPreference(getString(R.string.pref_key_switch_enable_smart_silent))).setChecked(false);
            if (resultCode == RESULT_OK) {
                showSnackbar("Thanks For Permission", Snackbar.LENGTH_SHORT);
            } else {
                if (isPermissionGranted()) {
                    showSnackbar("Thank You For Permission! Please Enable it now", Snackbar.LENGTH_LONG);
                } else {
                    showSnackbar("Please Provide me with permission", Snackbar.LENGTH_LONG);
                }
            }
        } else if (requestCode == Constants.RC_OPEN_BACKUP_RESTORE_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                showSnackbar("Successfully changed in database");
                getActivity().finish();
            } else if (resultCode == 2) {
                showSnackbar("Continue Clicked");
                getActivity().finish();
            }
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(getListView(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void showSnackbar(String message, int length) {
        Snackbar.make(getListView(), message, length).show();
    }

    public interface OnDatabaseImportedListener {
        void databaseImported();
    }

}
