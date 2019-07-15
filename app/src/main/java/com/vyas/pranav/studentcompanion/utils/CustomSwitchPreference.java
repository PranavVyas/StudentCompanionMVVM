package com.vyas.pranav.studentcompanion.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreferenceCompat;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.vyas.pranav.studentcompanion.R;

public class CustomSwitchPreference extends SwitchPreferenceCompat {

    private final Listener mListener = new Listener();
    private boolean currentState;

    public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setLayoutResource(R.layout.custom_item_switch_prefence_full);
//        TypedArray a = context.obtainStyledAttributes(attrs,
//                R.styleable.SwitchPreferenceCustom, defStyleAttr, defStyleRes);
//        setDisableDependentsState(a.getBoolean(
//                R.styleable.SwitchPreferenceCustom_disableDependentsState_custom, false));
//        a.recycle();
    }

    public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.custom_item_switch_prefence_full);
//        TypedArray a = context.obtainStyledAttributes(attrs,
//                R.styleable.SwitchPreferenceCustom, defStyleAttr, 0);
//        setSummaryOn(a.getString(R.styleable.SwitchPreferenceCustom_summaryOn_custom));
//        Toast.makeText(context, "Summery On :" + a.getString(R.styleable.SwitchPreferenceCustom_summaryOn_custom), Toast.LENGTH_SHORT).show();
//        setSummaryOff(a.getString(R.styleable.SwitchPreference_summaryOff));
//        setDisableDependentsState(a.getBoolean(
//                R.styleable.SwitchPreferenceCustom_disableDependentsState_custom, false));
//        a.recycle();
    }

    public CustomSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.custom_item_switch_prefence_full);
    }

    public CustomSwitchPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.custom_item_switch_prefence_full);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        View itemView = holder.itemView;
        View checkableView = itemView.findViewById(R.id.custom_switch_widget_switch);
        if (checkableView != null && checkableView instanceof ToggleableView) {
            ((ToggleableView) checkableView).setOn(mChecked);
            if (checkableView instanceof LabeledSwitch) {
                final LabeledSwitch switchView = (LabeledSwitch) checkableView;
                switchView.setOnToggledListener(mListener);
            }
        }
        GlideApp.with(getContext())
                .load(getIcon())
                .into((ImageView) itemView.findViewById(android.R.id.icon));
        syncSummaryView(holder);
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
}
