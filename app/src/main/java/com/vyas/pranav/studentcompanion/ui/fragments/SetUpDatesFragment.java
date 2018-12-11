package com.vyas.pranav.studentcompanion.ui.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.viewmodels.SetUpViewModel;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
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

    private OnDatesSetUpListener mCallback;
    private SetUpViewModel setUpViewModel;


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
    }

    @OnClick(R.id.btn_set_up_dates_fragment_continue)
    void continueClicked() {
        if (mCallback != null) {
            mCallback.onDatesSetUp();
        }
    }

    @OnClick(R.id.btn_set_up_dates_fragment_start_date)
    void selectStartingDate() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String startDateStr = ConverterUtils.formatDateStringFromCalender(i2, i1 + 1, i);
                setUpViewModel.setStartDate(startDateStr);
                btnStartDate.setText(setUpViewModel.getStartDate());
            }
        };
        showDatePickerDialog(listener);
    }

    //TODO Use ViewModel afterwards
    @OnClick(R.id.btn_set_up_dates_fragment_end_date)
    void selectEndingDate() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String endDateStr = ConverterUtils.formatDateStringFromCalender(i2, i1 + 1, i);
                setUpViewModel.setEndDate(endDateStr);
                btnEndDate.setText(setUpViewModel.getEndDate());
            }
        };
        showDatePickerDialog(listener);
    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener mListener) {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), mListener, year, month, day);
        datePickerDialog.setTitle("Pick date Now");
        datePickerDialog.show();
    }

    public void setOnDatesSetUpListener(OnDatesSetUpListener mCallback) {
        this.mCallback = mCallback;
    }

    public interface OnDatesSetUpListener {
        void onDatesSetUp();
    }

}
