package com.vyas.pranav.studentcompanion.ui.activities;
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
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.itangqi.waveloadingview.WaveLoadingView;

public class OverallAttendanceDetailActivity extends AppCompatActivity {

    public static final String EXTRA_OVERALL_ATTENDANCE = "OverallAttendanceDetailActivity.EXTRA_OVERALL_ATTENDANCE";
    @BindView(R.id.progress_overall_attendance_detail)
    WaveLoadingView progressSubject;
    @BindView(R.id.progress_overall_attendance_detail_max_attendance)
    WaveLoadingView progressSubjectMax;
    @BindView(R.id.tv_overall_attendance_detail_bunked)
    TextView tvBunkedDays;
    @BindView(R.id.tv_overall_attendance_detail_left_bunk)
    TextView tvLeftToBunkDays;
    @BindView(R.id.tv_overall_attendance_detail_present)
    TextView tvPresentDays;
    @BindView(R.id.tv_overall_attendance_detail_subject)
    TextView tvSubject;
    @BindView(R.id.tv_overall_attendance_detail_total)
    TextView tvTotalDays;
    @BindView(R.id.toolbar_overall_attendance_detail)
    Toolbar toolbar;
    @BindView(R.id.tv_overall_attendance_detail_att_criteria)
    TextView tvAttendanceCriteria;
    int currentAttendanceCriteria;
    private SharedPreferencesUtils preferencesUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesUtils.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_attendacne_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Intent receivedData = getIntent();
        preferencesUtils = SharedPreferencesUtils.getInstance(this);
        currentAttendanceCriteria = preferencesUtils.getCurrentAttendanceCriteria();
        if (receivedData != null) {
            if (receivedData.hasExtra(EXTRA_OVERALL_ATTENDANCE)) {
                String receivedJson = receivedData.getStringExtra(EXTRA_OVERALL_ATTENDANCE);
                Gson gson = new Gson();
                OverallAttendanceEntry entry = gson.fromJson(receivedJson, OverallAttendanceEntry.class);
                populateUI(entry);

                String transNameSubject = entry.getSubName() + entry.getSubName();
                String transNameProgress = entry.getSubName() + entry.get_ID();

                progressSubject.setTransitionName(transNameProgress);
                tvSubject.setTransitionName(transNameSubject);
            }
        }

    }

    private void populateUI(OverallAttendanceEntry entry) {
        tvSubject.setText(entry.getSubName());
        tvTotalDays.setText(String.format(Locale.US, "%d", entry.getTotalDays()));
        tvPresentDays.setText(String.format(Locale.US, "%d", entry.getPresentDays()));
        tvBunkedDays.setText(String.format(Locale.US, "%d", entry.getBunkedDays()));
        int leftToBunk = (int) Math.ceil(entry.getTotalDays() * ((100.0 - currentAttendanceCriteria) / 100.0)) - entry.getBunkedDays();
        tvLeftToBunkDays.setText(String.format(Locale.US, "%d", leftToBunk));
        int precentPercent = (entry.getPresentDays() * 100) / entry.getTotalDays();
        progressSubject.setProgressValue(precentPercent);
        progressSubject.setCenterTitle(precentPercent + " %");
        int maxAttendance = (int) Math.ceil(((entry.getTotalDays() - entry.getBunkedDays()) * 100.0) / entry.getTotalDays());
        progressSubjectMax.setProgressValue(maxAttendance);
        progressSubjectMax.setCenterTitle(maxAttendance + " %");
        tvAttendanceCriteria.setText("You have set Attendance criteria as : " + currentAttendanceCriteria + "%");

    }
}
