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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.vyas.pranav.studentcompanion.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils.setUserTheme;

public class DeveloperActivity extends AppCompatActivity {

    @BindView(R.id.item_developer_bulk_attendance)
    ConstraintLayout bulkAttendance;
    @BindView(R.id.toolbar_developer)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserTheme(this);
        setContentView(R.layout.activity_developer);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @OnClick(R.id.item_developer_bulk_attendance)
    void clickedBulkAttendance() {
        Intent intent = new Intent(this, DeveloperAttendanceActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.item_developer_edit_timetable)
    void clickedEditTimetable() {
        Intent intent = new Intent(this, DeveloperTimetableActivity.class);
        startActivity(intent);
    }
}
