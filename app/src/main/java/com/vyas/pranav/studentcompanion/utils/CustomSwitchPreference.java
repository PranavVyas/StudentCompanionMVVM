package com.vyas.pranav.studentcompanion.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreferenceCompat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomSwitchPreference extends SwitchPreferenceCompat {

    @BindView(android.R.id.switchInputMethod)
    CompoundButton widget;
    private boolean value;

    /**
     * Construct a new SwitchPreference with the given style options.
     *
     * @param context  The Context that will style this preference
     * @param attrs    Style attributes that differ from the default
     * @param defStyle Theme attribute defining the default style options
     */
    public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Construct a new SwitchPreference with the given style options.
     *
     * @param context The Context that will style this preference
     * @param attrs   Style attributes that differ from the default
     */
    public CustomSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Construct a new SwitchPreference with default style options.
     *
     * @param context The Context that will style this preference
     */
    public CustomSwitchPreference(Context context) {
        super(context, null);
    }

    public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        ButterKnife.bind(this, holder.itemView);
        if (widget == null) return; // no custom widget provided
        // Clean listener before invoke SwitchPreference.onBindView
        ViewGroup viewGroup = (ViewGroup) holder.itemView;
        clearListenerInViewGroup(viewGroup);
        // Set initial value and main check-listener
        widget.setChecked(value);
        widget.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    setChecked(isChecked);
                }
        );
        super.onBindViewHolder(holder);
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        value = checked;
    }

    /**
     * Clear listener in Switch for specify ViewGroup.
     *
     * @param viewGroup The ViewGroup that will need to clear the listener.
     */
    private void clearListenerInViewGroup(ViewGroup viewGroup) {
        if (null == viewGroup) {
            return;
        }

        int count = viewGroup.getChildCount();
        for (int n = 0; n < count; ++n) {
            View childView = viewGroup.getChildAt(n);
            if (childView instanceof CompoundButton) {
                final CompoundButton switchView = (CompoundButton) childView;
                switchView.setOnCheckedChangeListener(null);
                return;
            } else if (childView instanceof ViewGroup) {
                ViewGroup childGroup = (ViewGroup) childView;
                clearListenerInViewGroup(childGroup);
            }
        }
    }
}
