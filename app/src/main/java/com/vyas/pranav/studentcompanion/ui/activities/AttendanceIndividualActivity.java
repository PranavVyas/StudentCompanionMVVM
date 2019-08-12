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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.fragments.AttendanceIndividualFragment;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceIndividualActivity extends AppCompatActivity {

    public static final String EXTRA_DATE = "AttendanceIndividual.EXTRA_DATE";
    @BindView(R.id.toolbar_attendance_individual)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesUtils.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_individual);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setTitle("Attendance Details");
        Intent receivedData = getIntent();
        if (receivedData != null) {
            if (receivedData.hasExtra(EXTRA_DATE)) {
                String dateStr = receivedData.getStringExtra(EXTRA_DATE);
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_DATE, dateStr);
                AttendanceIndividualFragment attendanceIndividualFragment = new AttendanceIndividualFragment();
                attendanceIndividualFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_attendance_individual_container, attendanceIndividualFragment)
                        .commit();
            }
        }

    }
}
