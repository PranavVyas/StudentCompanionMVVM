package com.vyas.pranav.studentcompanion.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.vyas.pranav.studentcompanion.R;

import androidx.preference.PreferenceViewHolder;
import androidx.preference.TwoStatePreference;

public class CustomSwitchPreference extends TwoStatePreference {

    //
//    // Switch text for on and off states
//    private CharSequence mSwitchOn;
//    private CharSequence mSwitchOff;
    private final Listener mListener = new Listener();
    private boolean currentState;

    public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setLayoutResource(R.layout.custom_item_switch_prefence_full);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SwitchPreferenceCustom, defStyleAttr, defStyleRes);
        setSummaryOn(a.getString(R.styleable.SwitchPreferenceCustom_summaryOn_custom));
        Toast.makeText(context, "Summery On :" + a.getString(R.styleable.SwitchPreferenceCustom_summaryOn_custom), Toast.LENGTH_SHORT).show();
        setSummaryOff(a.getString(R.styleable.SwitchPreference_summaryOff));
//        setSwitchTextOn(a.getString(
//                R.styleable.SwitchPreferenceCustom_switchTextOn_custom));
//        setSwitchTextOff(a.getString(
//                R.styleable.SwitchPreferenceCustom_switchTextOff_custom));
        setDisableDependentsState(a.getBoolean(
                R.styleable.SwitchPreferenceCustom_disableDependentsState_custom, false));
        a.recycle();
    }

    public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.custom_item_switch_prefence_full);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SwitchPreferenceCustom, defStyleAttr, 0);
        setSummaryOn(a.getString(R.styleable.SwitchPreferenceCustom_summaryOn_custom));
        Toast.makeText(context, "Summery On :" + a.getString(R.styleable.SwitchPreferenceCustom_summaryOn_custom), Toast.LENGTH_SHORT).show();
        setSummaryOff(a.getString(R.styleable.SwitchPreference_summaryOff));
//        setSwitchTextOn(a.getString(
//                R.styleable.SwitchPreferenceCustom_switchTextOn_custom));
//        setSwitchTextOff(a.getString(
//                R.styleable.SwitchPreferenceCustom_switchTextOff_custom));
        setDisableDependentsState(a.getBoolean(
                R.styleable.SwitchPreferenceCustom_disableDependentsState_custom, false));
        a.recycle();
    }

    public CustomSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.custom_item_switch_prefence_full);
    }

//    public void setCustomSwitchSummery(){
//        if(currentState){
//            setSummaryOff(getSummaryOff());
//        }else{
//            setSummaryOn(getSummaryOn());
//        }
//    }

    public CustomSwitchPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.custom_item_switch_prefence_full);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        View itemView = holder.itemView;
//        ToggleableView customSwitch = itemView.findViewById(R.id.custom_switch_widget_switch);
//        if (customSwitch.isOn()) {
//            Logger.d("Switch is On in the preference");
//        }
//        refreshState(customSwitch);
////        itemView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                currentState = customSwitch.isOn();
////                persistBoolean(currentState);
////                Logger.d("Boolean is persistaed for " + getKey() + " as " + customSwitch.isOn());
////                refreshState(customSwitch);
////            }
////        });
//        customSwitch.setOnToggledListener(new OnToggledListener() {
//            @Override
//            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
//                persistBoolean(customSwitch.isOn());
//                Logger.d("Boolean is persistaed for " + getKey() + " as " + customSwitch.isOn());
//                refreshState(customSwitch);
//            }
//        });

        View checkableView = itemView.findViewById(R.id.custom_switch_widget_switch);
        if (checkableView != null && checkableView instanceof ToggleableView) {
            ((ToggleableView) checkableView).setOn(mChecked);
            if (checkableView instanceof LabeledSwitch) {
                final LabeledSwitch switchView = (LabeledSwitch) checkableView;
                switchView.setOnToggledListener(mListener);
                if (switchView.isOn()) {
                    setSummary(getSummaryOn());
                } else {
                    setSummary(getSummaryOff());
                }
            }
        }
        syncSummaryView(holder);
    }
//
//    public boolean isSwitchOn() {
//        return getPreferenceManager().getSharedPreferences().getBoolean(getKey(), false);
//    }

//    @Override
//    public void setEnabled(boolean enabled) {
//        super.setEnabled(enabled);
//        Toast.makeText(getContext(), "Message", Toast.LENGTH_SHORT).show();
//    }

//    public void setSwitchState(boolean b) {
//        getPreferenceManager().getSharedPreferences().edit().putBoolean(getKey(), b).apply();
//    }

    public void refreshState(ToggleableView customSwitch) {
        currentState = getPreferenceManager().getSharedPreferences().getBoolean(getKey(), false);
        customSwitch.setOn(currentState);
//        setCustomSwitchSummery();
    }

    private class Listener implements OnToggledListener {

        @Override
        public void onSwitched(ToggleableView toggleableView, boolean isOn) {
            if (!callChangeListener(isOn)) {
                // Listener didn't like it, change it back.
                // CompoundButton will make sure we don't recurse.
                toggleableView.setOn(!isOn);
                return;
            }
            CustomSwitchPreference.this.setChecked(isOn);
            notifyChanged();
        }
    }

//
//    /**
//     * Set the text displayed on the switch widget in the on state.
//     * This should be a very short string; one word if possible.
//     *
//     * @param onText Text to display in the on state
//     */
//    public void setSwitchTextOn(CharSequence onText) {
//        mSwitchOn = onText;
//        notifyChanged();
//    }
//    /**
//     * Set the text displayed on the switch widget in the off state.
//     * This should be a very short string; one word if possible.
//     *
//     * @param offText Text to display in the off state
//     */
//    public void setSwitchTextOff(CharSequence offText) {
//        mSwitchOff = offText;
//        notifyChanged();
//    }
//    /**
//     * Set the text displayed on the switch widget in the on state.
//     * This should be a very short string; one word if possible.
//     *
//     * @param resId The text as a string resource ID
//     */
//    public void setSwitchTextOn(int resId) {
//        setSwitchTextOn(getContext().getString(resId));
//    }
//    /**
//     * Set the text displayed on the switch widget in the off state.
//     * This should be a very short string; one word if possible.
//     *
//     * @param resId The text as a string resource ID
//     */
//    public void setSwitchTextOff(int resId) {
//        setSwitchTextOff(getContext().getString(resId));
//    }
//    /**
//     * @return The text that will be displayed on the switch widget in the on state
//     */
//    public CharSequence getSwitchTextOn() {
//        return mSwitchOn;
//    }
//    /**
//     * @return The text that will be displayed on the switch widget in the off state
//     */
//    public CharSequence getSwitchTextOff() {
//        return mSwitchOff;
//    }
}
