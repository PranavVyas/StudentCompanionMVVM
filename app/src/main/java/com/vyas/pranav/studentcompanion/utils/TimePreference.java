package com.vyas.pranav.studentcompanion.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.vyas.pranav.studentcompanion.R;

import androidx.preference.DialogPreference;

/**
 * The type Time preference.
 * Custom preference that creates dialog of the time picker and sets time when the dialog is closed
 */
public class TimePreference extends DialogPreference {

    private int mTime;

    /**
     * Instantiates a new Time preference.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     * @param defStyleRes  the def style res
     */
    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Instantiates a new Time preference.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    /**
     * Instantiates a new Time preference.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public TimePreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.dialogPreferenceStyle);
    }

    /**
     * Instantiates a new Time preference.
     *
     * @param context the context
     */
    public TimePreference(Context context) {
        this(context, null);
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public int getTime() {
        return mTime;
    }

    /**
     * Sets time.
     *
     * @param time the time
     */
    public void setTime(int time) {
        this.mTime = time;
        //Time Saved to Shared Preference
        persistInt(time);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        //Get Default Value of preference
        //If don't have the default value (the value is not set via xml attribute "android:defaultValue : " returns "0" as default value
        return a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        // Read the value. Use the default value if it is not possible.
        setTime(restorePersistedValue ? getPersistedInt(mTime) : (int) defaultValue);
    }

    @Override
    public int getDialogLayoutResource() {
        return R.layout.pref_dialog_time;
    }
}
