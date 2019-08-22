package com.vyas.pranav.studentcompanion.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;

public class DateAndTimePickerDialog extends DialogFragment {

    private OnDateAndTimeBothSelectedListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SublimeListenerAdapter mListener = new SublimeListenerAdapter() {
            @Override
            public void onDateTimeRecurrenceSet(SublimePicker sublimeMaterialPicker, SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
                Toast.makeText(getContext(), "Date is selected :" + selectedDate.toString() + " Time Selected:" + hourOfDay + ":" + minute, Toast.LENGTH_LONG).show();
                if (listener != null) {
                    listener.OnDateAndTimeBothSelected(sublimeMaterialPicker, selectedDate, hourOfDay, minute, recurrenceOption, recurrenceRule);
                }
                dismiss();
            }

            @Override
            public void onCancelled() {
                dismiss();
            }
        };

        SublimePicker mPicker = new SublimePicker(getContext());
        SublimeOptions mOptions = new SublimeOptions();
        mOptions.setDisplayOptions(SublimeOptions.ACTIVATE_DATE_PICKER | SublimeOptions.ACTIVATE_TIME_PICKER);
        mPicker.initializePicker(mOptions, mListener);
        return mPicker;
    }

    public void setOnDateAndTimeBothSelectedListener(OnDateAndTimeBothSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnDateAndTimeBothSelectedListener {
        void OnDateAndTimeBothSelected(SublimePicker sublimeMaterialPicker, SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule);
    }
}
