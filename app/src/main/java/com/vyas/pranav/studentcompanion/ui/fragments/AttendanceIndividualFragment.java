package com.vyas.pranav.studentcompanion.ui.fragments;
/*
Student Companion - An Android App that has features like attendance manager, note manager etc
Copyright (C) 2019  Pranav Vyas

This file is a part of Student Companion.

Student Companion is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Student Companion is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.
*/

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;
import com.google.android.material.picker.MaterialStyledDatePickerDialog;
import com.google.android.material.snackbar.Snackbar;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.AttendanceIndividualRecyclerAdapter;
import com.vyas.pranav.studentcompanion.ui.activities.AttendanceIndividualActivity;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.viewmodels.AttendanceForDateViewModel;
import com.vyas.pranav.studentcompanion.viewmodels.AttendanceForDateViewModelFactory;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AttendanceIndividualFragment extends Fragment {
    private static final String TAG = "AttendanceIndividualFra";

    @BindView(R.id.recycler_attendance_individual_fragment_main)
    RecyclerView rvMain;
    @BindView(R.id.tv_attendance_individual_date)
    TextView tvDate;
    @BindView(R.id.btn_attendance_individual_fragment_other_attendance)
    Button btnOpenOtherAttendance;
    @BindView(R.id.progress_attendance_individual_fragment_main)
    ProgressBar mProgress;
    @BindView(R.id.placeholder_attendance_individual_holidays)
    ConstraintLayout placeHolderHolidays;

    private AttendanceForDateViewModel attendanceViewModel;
    private AttendanceIndividualRecyclerAdapter mAdapter;


    public AttendanceIndividualFragment() {
    }

    public static AttendanceIndividualFragment newInstance(String dateStr) {
        if (dateStr != null) {
            Bundle args = new Bundle();
            args.putString(AttendanceIndividualActivity.EXTRA_DATE, dateStr);
            AttendanceIndividualFragment fragment = new AttendanceIndividualFragment();
            fragment.setArguments(args);
            return fragment;
        }
        return new AttendanceIndividualFragment();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
        Date date = new Date();
        date = ConverterUtils.convertStringToDate(ConverterUtils.convertDateToString(date));
        if (getArguments() != null) {
            String dateStr = getArguments().getString(AttendanceIndividualActivity.EXTRA_DATE);
            date = ConverterUtils.convertStringToDate(dateStr);
            btnOpenOtherAttendance.setVisibility(View.GONE);
        }
        setUpIndividualAttendance(date);
        tvDate.setText(ConverterUtils.convertDateToString(date) + "\n" + ConverterUtils.getDayOfWeek(date));
        startInstruction(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance_individual, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint("RestrictedApi")
    @OnClick(R.id.btn_attendance_individual_fragment_other_attendance)
    void openDatePicker() {
        Calendar now = Calendar.getInstance();
        MaterialStyledDatePickerDialog datePickerDialog = new MaterialStyledDatePickerDialog(
                getContext(),
                (datePicker, year, i1, day) -> {
                    int month = i1 + 1;
                    String selectedDate = ConverterUtils.formatDateStringFromCalender(day, month, year);
                    Intent intent = new Intent(getContext(), AttendanceIndividualActivity.class);
                    intent.putExtra(AttendanceIndividualActivity.EXTRA_DATE, selectedDate);
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
                    startActivity(intent);
                    //Toast.makeText(getContext(), "i = "+i+" i1 = "+i1+" i2 = "+i2, Toast.LENGTH_SHORT).show();
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Choose Date");
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.getDatePicker().setMinDate(attendanceViewModel.getStartingDate().getTime());
        datePickerDialog.show();
    }


    private void setUpRecyclerView() {
        mAdapter = new AttendanceIndividualRecyclerAdapter();
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.VERTICAL);
        rvMain.setLayoutManager(lm);
        startProgress();
        rvMain.setAdapter(mAdapter);
    }

    private void setUpIndividualAttendance(Date date) {
        AttendanceForDateViewModelFactory factory = new AttendanceForDateViewModelFactory(getActivity().getApplicationContext(), date);
        attendanceViewModel = ViewModelProviders.of(getActivity(), factory).get(AttendanceForDateViewModel.class);
        attendanceViewModel.getAttendanceForDate().observe(this, attendanceEntries -> {
            if (!attendanceEntries.isEmpty()) {
                mAdapter.submitList(attendanceEntries);
                stopProgress();
                showHolidayPlaceHolder(false);
                return;
            }
            showHolidayPlaceHolder(true);
            stopProgress();
        });
        mAdapter.setOnAttendanceSwitchToggledListener(attendanceEntry -> attendanceViewModel.updateAttendanceInSequence(attendanceEntry));
    }

    private void startProgress() {
        rvMain.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
    }

    private void stopProgress() {
        rvMain.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }

    private void showHolidayPlaceHolder(boolean isShown) {
        if (isShown) {
            rvMain.setVisibility(View.GONE);
            placeHolderHolidays.setVisibility(View.VISIBLE);
            if (new Date().getTime() - (attendanceViewModel.getEndingDate().getTime()) > 1) {
                showSnackBar("Your Semester is Over!", Snackbar.LENGTH_LONG);
            }
        } else {
            rvMain.setVisibility(View.VISIBLE);
            placeHolderHolidays.setVisibility(View.GONE);
        }
    }

    private void showSnackBar(String s, int duration) {
        Snackbar.make(placeHolderHolidays, s, duration).show();
    }

    private void startInstruction(Activity activity) {
        BubbleShowCaseBuilder attendance = new BubbleShowCaseBuilder(activity)
                .title(getContext().getString(R.string.instr_atd_card_title))
                .description(getContext().getString(R.string.instr_atd_card_desc))
                .showOnce(TAG + "AttendanceCard");
        BubbleShowCaseBuilder previousAttendance = new BubbleShowCaseBuilder(activity)
                .title(getContext().getString(R.string.instr_atd_previous_atd_title))
                .targetView(btnOpenOtherAttendance)
                .description(getContext().getString(R.string.instr_atd_previous_atd_desc))
                .showOnce(TAG + "PreviousAtt");
        new BubbleShowCaseSequence()
                .addShowCase(previousAttendance)
                .addShowCase(attendance)
                .show();
    }
}
