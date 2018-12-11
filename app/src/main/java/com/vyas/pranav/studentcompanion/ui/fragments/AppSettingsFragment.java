package com.vyas.pranav.studentcompanion.ui.fragments;

import android.os.Bundle;

import com.vyas.pranav.studentcompanion.R;

import androidx.preference.PreferenceFragmentCompat;

public class AppSettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_settings);
    }
}
