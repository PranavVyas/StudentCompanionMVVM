package com.vyas.pranav.studentcompanion.ui.fragments;

import android.app.ActivityOptions;
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
import androidx.preference.PreferenceManager;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.ui.activities.AutoAttendanceSubjectListActivity;
import com.vyas.pranav.studentcompanion.ui.activities.NotificationPreferenceActivity;
import com.vyas.pranav.studentcompanion.utils.AutoAttendanceHelper;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.TimePreference;
import com.vyas.pranav.studentcompanion.utils.TimePreferenceDialogFragmentCompat;
import com.vyas.pranav.studentcompanion.viewmodels.AppSettingsViewModel;

import java.util.List;

import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class AppSettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener
//        ,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    private AppSettingsViewModel appSettingsViewModel;
    private AutoAttendanceHelper helper;
//    private GeoFencingRepository geoFencingRepository;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_settings, rootKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        helper = new AutoAttendanceHelper(getContext());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        getActivity().registerReceiver(myReceiver, new IntentFilter(Constants.FENCE_RECEIVER_ACTION));
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
//            appSettingsViewModel.setRefreshGeoFence(appSettingsViewModel.isAutoAttendanceEnabled());
            setEditAutoAttendanceStateFromViewModel();
//            TODO Register and unregister fence accordingly
            setFence(appSettingsViewModel.isAutoAttendanceEnabled());
        } else if (s.equals(getString(R.string.pref_key_switch_enable_night_mode))) {
            toggleNightMode();
        } else if (s.equals(getString(R.string.pref_key_list_notification))) {
            //TODO
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
//        if(appSettingsViewModel.isNightModeEnabled()) {
//            AppCompatDelegate.setDefaultNightMode(
//                    AppCompatDelegate.MODE_NIGHT_YES);
//        } else {
//            AppCompatDelegate.setDefaultNightMode(
//                    AppCompatDelegate.MODE_NIGHT_NO);
//        }
    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        mClient.disconnect();
//    }

    private void setEditAutoAttendanceStateFromViewModel() {
        boolean isAutoAttendanceEnabled = appSettingsViewModel.isAutoAttendanceEnabled();
        if (isAutoAttendanceEnabled) {
            findPreference(getString(R.string.pref_key_select_places_auto_attendance)).setEnabled(true);
        } else {
            findPreference(getString(R.string.pref_key_select_places_auto_attendance)).setEnabled(false);
        }
    }

    //    private GoogleApiClient getApiClient() {
//        if (mClient != null) {
//            return mClient;
//        }
//        mClient = new GoogleApiClient.Builder(getContext())
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .addApi(Places.GEO_DATA_API)
//                .build();
//        return mClient;
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
    private boolean isPermissionGranted() {
        NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        return Build.VERSION.SDK_INT < 24 || nm.isNotificationPolicyAccessGranted();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appSettingsViewModel = ViewModelProviders.of(getActivity()).get(AppSettingsViewModel.class);
        setTimePrefSummery(getContext().getString(R.string.pref_key_time_reminder_time));
        ButterKnife.bind(this, view);
        setSelectTimeStateFromViewModel();
        setEditAutoAttendanceStateFromViewModel();
//        geoFencingRepository = new GeoFencingRepository(getContext(), getApiClient());
        findPreference(getString(R.string.pref_key_select_places_auto_attendance)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
                Intent intent = new Intent(getContext(), AutoAttendanceSubjectListActivity.class);
                startActivity(intent, bundle);
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
                            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
                            startActivityForResult(intent, Constants.RC_SETTINGS_SILENT_DEVICE, bundle);
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

        findPreference(getString(R.string.pref_key_list_notification)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), NotificationPreferenceActivity.class);
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
                startActivity(intent, bundle);
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SETTINGS_SILENT_DEVICE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getContext(), "Thanks For Permission", Toast.LENGTH_SHORT).show();
            } else {
                if (isPermissionGranted()) {
                    Toast.makeText(getContext(), "Thank You For Permission! Please Enable it now", Toast.LENGTH_LONG).show();
                } else {
                    //TODO turn off switch here
                    Toast.makeText(getContext(), "Please Provide me with permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
