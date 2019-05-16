package com.vyas.pranav.studentcompanion.ui.fragments;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDatabase;
import com.vyas.pranav.studentcompanion.repositories.GeoFencingRepository;
import com.vyas.pranav.studentcompanion.ui.activities.AutoAttendanceSubjectListActivity;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.TimePreference;
import com.vyas.pranav.studentcompanion.utils.TimePreferenceDialogFragmentCompat;
import com.vyas.pranav.studentcompanion.viewmodels.AppSettingsViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

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
        setPreferencesFromResource(R.xml.app_settings, rootKey);
    }

    public static boolean copyFile(String from, String to) {
        try {
//            File sd = Environment.getExternalStorageDirectory();
//            if (sd.canWrite()) {
//                Logger.d("External Storage Exists");
//                int end = from.toString().lastIndexOf("/");
//                String str1 = from.toString().substring(0, end);
//                String str2 = from.toString().substring(end + 1, from.length());
//                File source = new File(str1, str2);
//                File destination = new File(to, str2);
//                if(new File(to, str2).exists()){
////                    boolean isNewFileCreated = new File(to, str2).createNewFile();
////                    Logger.d("File Created : "+isNewFileCreated);
//                    if (!destination.getParentFile().exists())
//                        destination.getParentFile().mkdirs();
//
//                    if (!destination.exists()) {
//                        destination.createNewFile();
//                    }
//                }
//                if (source.exists()) {
//                    FileChannel src = new FileInputStream(source).getChannel();
//                    FileChannel dst = new FileOutputStream(destination).getChannel();
//                    dst.transferFrom(src, 0, src.size());
//                    src.close();
//                    dst.close();
//                    Logger.d("Copying File Successfully");
//                } else {
//                    Logger.d("File Does not exists");
//                    return false;
//                }
            File src = new File(from);
            File dst = new File(to, src.getName());

            if (!dst.getParentFile().exists())
                dst.getParentFile().mkdirs();

            if (!dst.exists()) {
                dst.createNewFile();
            }

            FileChannel source = null;
            FileChannel destination = null;

            try {
                source = new FileInputStream(src).getChannel();
                destination = new FileOutputStream(src).getChannel();
                destination.transferFrom(source, 0, source.size());
            } finally {
                if (source != null) {
                    source.close();
                }
                if (destination != null) {
                    destination.close();
                }
                Logger.d("Copy Successfully done");
            }
            return true;
        } catch (Exception e) {
            Logger.d("Copying File Failed\n Error : " + e.getMessage());
            return false;
        }
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
        findPreference(getString(R.string.pref_key_backup_database)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //TODO
                exportDatabaseFile(getContext());
                return false;
            }
        });
        findPreference(getString(R.string.pref_key_restore_database)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //TODO
                importDatabaseFile(getContext());
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

    private void exportDatabaseFile(Context context) {
        String dirName = "backup_" + String.valueOf(ConverterUtils.getCurrentTimeInMillis()) + "_";
        copyData(
                context.getDatabasePath(OverallAttendanceDatabase.DB_NAME).getPath(),
                Environment.getExternalStorageDirectory().getPath() + "/Download/" + dirName + OverallAttendanceDatabase.DB_NAME
        );
//        copyData(
//                context.getDatabasePath(OverallAttendanceDatabase.DB_NAME + "-shm").getPath(),
//                OverallAttendanceDatabase.DB_NAME + "-shm"
//        );
//        copyData(
//                context.getDatabasePath(OverallAttendanceDatabase.DB_NAME + "-wal").getPath(),
//                OverallAttendanceDatabase.DB_NAME + "-wal"
//        );
    }

    private void importDatabaseFile(Context context) {
        copyData(
                Environment.getExternalStorageDirectory().getPath() + "/Download/" + "backup_" + OverallAttendanceDatabase.DB_NAME,
                context.getDatabasePath(OverallAttendanceDatabase.DB_NAME).getPath()
        );
        copyData(
                Environment.getExternalStorageDirectory().getPath() + "/Download/" + "backup_" + OverallAttendanceDatabase.DB_NAME + "-shm",
                context.getDatabasePath(OverallAttendanceDatabase.DB_NAME + "-shm").getPath()
        );
        copyData(
                Environment.getExternalStorageDirectory().getPath() + "/Download/" + "backup_" + OverallAttendanceDatabase.DB_NAME + "-wal",
                context.getDatabasePath(OverallAttendanceDatabase.DB_NAME + "-wal").getPath()
        );
//        Intent intent = new Intent(getContext(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        getActivity().finish();
    }

    private void copyData(String fromPath, String toPath) {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        try {
//                            File src = new File(fromPath);
//                            File dst = new File(toPath, src.getName());
//
//                            if (src.isDirectory()) {
//                                String files[] = src.list();
//                                int filesLength = files.length;
//                                for (int i = 0; i < filesLength; i++) {
//                                    String src1 = (new File(src, files[i]).getPath());
//                                    String dst1 = dst.getPath();
//                                    copyData(src1, dst1);
//                                }
//                            } else {
//                                if (!dst.getParentFile().exists())
//                                if (!dst.exists()) {
//                                    dst.createNewFile();
//                                }
//
//                                FileChannel source = null;
//                                FileChannel destination = null;
//
//                                try {
//                                    source = new FileInputStream(src).getChannel();
//                                    destination = new FileOutputStream(dst).getChannel();
//                                    destination.transferFrom(source, 0, source.size());
//                                } finally {
//                                    if (source != null) {
//                                        source.close();
//                                    }
//                                    if (destination != null) {
//                                        destination.close();
//                                    }
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        try {
                        copyFile(fromPath, toPath);
//                            copy(getContext(),fromPath,toPath);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                })
                .check();
    }

}
