package com.vyas.pranav.studentcompanion.ui.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.repositories.GeoFencingRepository;
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
import androidx.preference.PreferenceManager;
import butterknife.ButterKnife;

public class AppSettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private AppSettingsViewModel appSettingsViewModel;
    private GoogleApiClient mClient;
    private GeoFencingRepository geoFencingRepository;

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
        setEditAutoAttendanceStateFromViewModel();
        geoFencingRepository = new GeoFencingRepository(getContext(), getApiClient());
        findPreference(getString(R.string.pref_key_select_places_auto_attendance)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), AutoAttendanceSubjectListActivity.class);
                startActivity(intent);
                return false;
            }
        });
        findPreference(getString(R.string.pref_key_switch_enable_smart_silent)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isEnabled = (boolean) newValue;
                if (isEnabled) {
                    if (isPermissionGranted()) {
//                        Toast.makeText(getContext(), "Enabled the Silent Mode", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Permission is not granted\nPlease Grant Do Not Disturb Access to StudentCompanion", Toast.LENGTH_SHORT).show();
                        NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        if (Build.VERSION.SDK_INT >= 24 && !nm.isNotificationPolicyAccessGranted()) {
                            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                            startActivity(intent);
                        }
                        return false;
                    }
                } else {
//                    Toast.makeText(getContext(), "Disabled the Silent Mode", Toast.LENGTH_SHORT).show();
                }
                appSettingsViewModel.toggleSmartSilent();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isPermissionGranted()) {
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean(getString(R.string.pref_key_switch_enable_smart_silent), false).apply();
        }
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
            appSettingsViewModel.setReminderJobTime(getTimeFromViewModel());
        } else if (s.equals(getString(R.string.pref_key_switch_enable_auto_attendance))) {
//            checkAutoAttendanceStateAndExecute();
            appSettingsViewModel.setRefreshGeoFence(appSettingsViewModel.isAutoAttendanceEnabled());
            setEditAutoAttendanceStateFromViewModel();
            if (appSettingsViewModel.isAutoAttendanceEnabled()) {
                geoFencingRepository.refreshAllGeoFences();
            } else {
                geoFencingRepository.unRegisterAllGeoFenceAtOnce();
            }
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
            (findPreference(getString(R.string.pref_key_time_reminder_time))).setEnabled(true);
        } else {
            (findPreference(getString(R.string.pref_key_time_reminder_time))).setEnabled(false);
//            CustomSwitchPreference customSwitchPreference = (CustomSwitchPreference) findPreference(getString(R.string.pref_key_time_reminder_time));
//            customSwitchPreference.setSwitchState(false);
        }
    }

    private int getTimeFromViewModel() {
        return appSettingsViewModel.getReminderTime();
    }

    private void toggleNightMode() {
        //appSettingsViewModel.toggleNightMode();
        getActivity().recreate();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    private void setEditAutoAttendanceStateFromViewModel() {
        boolean isAutoAttendanceEnabled = appSettingsViewModel.isAutoAttendanceEnabled();
        if (isAutoAttendanceEnabled) {
            findPreference(getString(R.string.pref_key_select_places_auto_attendance)).setEnabled(true);
        } else {
            findPreference(getString(R.string.pref_key_select_places_auto_attendance)).setEnabled(false);
        }
    }

    private GoogleApiClient getApiClient() {
        if (mClient != null) {
            return mClient;
        }
        mClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
        return mClient;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private boolean isPermissionGranted() {
        NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        return Build.VERSION.SDK_INT < 24 || nm.isNotificationPolicyAccessGranted();
    }
}
