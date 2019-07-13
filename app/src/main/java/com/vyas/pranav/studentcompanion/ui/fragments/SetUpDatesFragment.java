package com.vyas.pranav.studentcompanion.ui.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.picker.MaterialStyledDatePickerDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.viewmodels.SetUpViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SetUpDatesFragment extends Fragment {


    @BindView(R.id.btn_set_up_dates_fragment_continue)
    Button btnContinue;
    @BindView(R.id.btn_set_up_dates_fragment_end_date)
    Button btnEndDate;
    @BindView(R.id.btn_set_up_dates_fragment_start_date)
    Button btnStartDate;
    @BindView(R.id.et_set_up_dates_sem_no)
    TextInputEditText etSemNo;
    @BindView(R.id.text_input_set_up_dates_sem_no)
    TextInputLayout inputSemNo;
    @BindView(R.id.tv_set_up_dates_attendance_percent)
    TextView tvAttendancePercent;
    @BindView(R.id.seek_set_up_dates_attendance_crieteria)
    SeekBar seekBarAttendance;

    private OnDatesSetUpListener mCallback;
    private SetUpViewModel setUpViewModel;
    private String semNo;


    public SetUpDatesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_up_dates, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViewModel = ViewModelProviders.of(getActivity()).get(SetUpViewModel.class);
        btnStartDate.setText(setUpViewModel.getStartDate());
        btnEndDate.setText(setUpViewModel.getEndDate());
        seekBarAttendance.setProgress(setUpViewModel.getCurrentAttendanceCriteria());
        tvAttendancePercent.setText(setUpViewModel.getCurrentAttendanceCriteria() + " %");
        seekBarAttendance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvAttendancePercent.setText(i + " %");
                setUpViewModel.setCurrentAttendanceCriteria(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @OnClick(R.id.btn_set_up_dates_fragment_continue)
    void continueClicked() {
        semNo = etSemNo.getText().toString().trim();
        if (setUpViewModel.getStartDate().equals("Select Starting Date")) {
            Toast.makeText(getContext(), "Select Starting date", Toast.LENGTH_SHORT).show();
        } else if (setUpViewModel.getEndDate().equals("Select Ending Date")) {
            Toast.makeText(getContext(), "Select Ending date", Toast.LENGTH_SHORT).show();
        } else if (getDifferenceInDates(setUpViewModel.getStartDate(), setUpViewModel.getEndDate()) < 15) {
            Toast.makeText(getContext(), "Difference between dates must be more than 15 Days\nEnding date must be after Starting date", Toast.LENGTH_SHORT).show();
        } else if (!validateSemNo()) {
            inputSemNo.setError("Please Input Correct Sem No");
        } else {
            inputSemNo.setErrorEnabled(false);
            setSemNo();
            if (mCallback != null) {
                mCallback.onDatesSetUp();
            }
        }
    }

    @OnClick(R.id.btn_set_up_dates_fragment_start_date)
    void selectStartingDate() {
        MaterialStyledDatePickerDialog.OnDateSetListener listener = new MaterialStyledDatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String startDateStr = ConverterUtils.formatDateStringFromCalender(i2, i1 + 1, i);
                setUpViewModel.setStartDate(startDateStr);
                btnStartDate.setText(setUpViewModel.getStartDate());
            }
        };
        showDatePickerDialog(listener);
    }

    @SuppressLint("RestrictedApi")
    @OnClick(R.id.btn_set_up_dates_fragment_end_date)
    void selectEndingDate() {
        MaterialStyledDatePickerDialog.OnDateSetListener listener = new MaterialStyledDatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String endDateStr = ConverterUtils.formatDateStringFromCalender(i2, i1 + 1, i);
                setUpViewModel.setEndDate(endDateStr);
                btnEndDate.setText(setUpViewModel.getEndDate());
            }
        };
        showDatePickerDialog(listener);
    }

    @SuppressLint("RestrictedApi")
    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener mListener) {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);
        MaterialStyledDatePickerDialog datePickerDialog = new MaterialStyledDatePickerDialog(getContext(), mListener, year, month, day);
        datePickerDialog.setTitle("Pick date Now");
        datePickerDialog.show();
    }

    public void setOnDatesSetUpListener(OnDatesSetUpListener mCallback) {
        this.mCallback = mCallback;
    }

    private int getDifferenceInDates(String startDate, String endDate) {
        Date start = ConverterUtils.convertStringToDate(startDate);
        Date end = ConverterUtils.convertStringToDate(endDate);
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);
        long diffInMillis = endCal.getTimeInMillis() - startCal.getTimeInMillis();
        //Toast.makeText(getContext(), "Diff in days are "+days, Toast.LENGTH_SHORT).show();
        return (int) TimeUnit.MILLISECONDS.toDays(diffInMillis);
    }

    private boolean validateSemNo() {
        if (semNo == null) {
            return false;
        } else if (semNo.isEmpty()) {
            return false;
        } else return Integer.valueOf(semNo) >= 1;
    }

    private void setSemNo() {
        setUpViewModel.setSemester(Integer.valueOf(semNo));
    }

    public interface OnDatesSetUpListener {
        void onDatesSetUp();
    }
}
