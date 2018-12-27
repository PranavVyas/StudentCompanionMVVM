package com.vyas.pranav.studentcompanion.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.activities.AutoAttendanceSubjectListActivity;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.TimePreference;
import com.vyas.pranav.studentcompanion.utils.TimePreferenceDialogFragmentCompat;
import com.vyas.pranav.studentcompanion.viewmodels.AppSettingsViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import butterknife.ButterKnife;

public class AppSettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private AppSettingsViewModel appSettingsViewModel;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appSettingsViewModel = ViewModelProviders.of(getActivity()).get(AppSettingsViewModel.class);
        setTimePrefSummery(getContext().getString(R.string.pref_key_time_reminder_time));
        ButterKnife.bind(this, view);
        setSelectTimeStateFromViewModel();
        findPreference("Kdkskd").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), AutoAttendanceSubjectListActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }

//    @OnClick(R.id.pref_delete_account)
//    void clickedcDeleteAccount(){
//        Toast.makeText(getContext(), "Clicked Delete account bro", Toast.LENGTH_SHORT).show();
//    }


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
            appSettingsViewModel.setReminderJobTime(getTimeFromViewModel());
        } else if (s.equals(getString(R.string.pref_key_switch_enable_auto_attendance))) {
            checkAutoAttendanceStateAndExecute();
        } else if (s.equals(getString(R.string.pref_key_switch_enable_night_mode))) {
            toggleNightMode();
        }
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        // Try if the preference is one of our custom Preferences
        DialogFragment dialogFragment = null;
        if (preference instanceof TimePreference) {
            // Create a new instance of TimePreferenceDialogFragment with the key of the relatee Preference
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
            appSettingsViewModel.setReminderJobTime(getTimeFromViewModel());
        } else {
            appSettingsViewModel.cancelReminderJob();
        }
    }

    /**
     * Set up time preference according to the state of switch
     * If switch is enabled set up the time picker true and vise versa
     */
    private void setSelectTimeStateFromViewModel() {
        boolean isEnabled = appSettingsViewModel.isReminderEnabled();
        if (isEnabled) {
            findPreference(getString(R.string.pref_key_time_reminder_time)).setEnabled(true);
        } else {
            findPreference(getString(R.string.pref_key_time_reminder_time)).setEnabled(false);
        }
    }

    private int getTimeFromViewModel() {
        return appSettingsViewModel.getReminderTime();
    }

    private void checkAutoAttendanceStateAndExecute() {
        if (appSettingsViewModel.isAutoAttendanceEnabled()) {
            appSettingsViewModel.enableAutoAttendanceJob();
        } else {
            appSettingsViewModel.cancelAutoAttendanceJob();
        }
    }

    private void toggleNightMode() {
        appSettingsViewModel.toggleNightMode();
        getActivity().recreate();
    }
}
