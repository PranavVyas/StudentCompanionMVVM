package com.vyas.pranav.studentcompanion.utils;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;

import com.vyas.pranav.studentcompanion.R;

import androidx.preference.DialogPreference;
import androidx.preference.PreferenceDialogFragmentCompat;

/**
 * The type Time preference dialog fragment compat.
 */
public class TimePreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {

    private TimePicker mTimePicker;

    /**
     * Gets new instance.
     *
     * @param key the key
     * @return the new instance
     */
    static TimePreferenceDialogFragmentCompat getNewInstance(String key) {
        TimePreferenceDialogFragmentCompat sInstance = new TimePreferenceDialogFragmentCompat();
        Bundle b = new Bundle();
        b.putString(ARG_KEY, key);
        sInstance.setArguments(b);

        return sInstance;
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // generate value to save
            int hours = mTimePicker.getCurrentHour();
            int minutes = mTimePicker.getCurrentMinute();
            int minutesAfterMidnight = (hours * 60) + minutes;

            // Get the related Preference and save the value
            DialogPreference preference = getPreference();
            if (preference instanceof TimePreference) {
                TimePreference timePreference =
                        ((TimePreference) preference);
                // This allows the client to ignore the user value.
                if (timePreference.callChangeListener(
                        minutesAfterMidnight)) {
                    // Save the value
                    timePreference.setTime(minutesAfterMidnight);
                }
            }
        }
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mTimePicker = view.findViewById(R.id.time_picker_time_pref);

        // Exception when there is no TimePicker
        if (mTimePicker == null) {
            throw new IllegalStateException("Dialog view must contain a TimePicker with id 'time_picker_time_pref'");
        }

        // Get the time from the related Preference
        Integer timeInMinutes = null;
        DialogPreference preference = getPreference();
        if (preference instanceof TimePreference) {
            timeInMinutes =
                    ((TimePreference) preference).getTime();
        }

        // Set the time to the TimePicker
        if (timeInMinutes != null) {
            int hours = timeInMinutes / 60;
            int minutes = timeInMinutes % 60;
            boolean is24hour = DateFormat.is24HourFormat(getContext());

            mTimePicker.setIs24HourView(is24hour);
            mTimePicker.setCurrentHour(hours);
            mTimePicker.setCurrentMinute(minutes);
        }
    }
}