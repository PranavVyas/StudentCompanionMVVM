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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.schibstedspain.leku.LocationPickerActivityKt;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.AutoAttendanceSubjectListRecyclerAdapter;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.viewmodels.AutoAttendanceSubjectListViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AutoAttendanceSubjectListActivity extends AppCompatActivity {

    @BindView(R.id.recycler_auto_attendance_main)
    RecyclerView rvMain;
    @BindView(R.id.toolbar_auto_attendance_subject_list)
    Toolbar toolbar;

    private AutoAttendanceSubjectListRecyclerAdapter mAdapter;

    private AutoAttendanceSubjectListViewModel autoAttendanceSubjectListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesUtils.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_attendance_subject_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        autoAttendanceSubjectListViewModel = ViewModelProviders.of(this).get(AutoAttendanceSubjectListViewModel.class);
        populateUI();
    }

    private void populateUI() {
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mAdapter = new AutoAttendanceSubjectListRecyclerAdapter();
        rvMain.setAdapter(mAdapter);
        rvMain.setLayoutManager(llm);
        mAdapter.setSubjectList(autoAttendanceSubjectListViewModel.getSubjectList());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RC_OPEN_PLACE_PICKER_CUSTOM && resultCode == RESULT_OK) {
            if (data != null) {
//                Place place = PlacePicker.getPlace(this, data);
//                currPlace.setPlaceId(place.getId());
//                autoAttendanceSubjectDetailViewModel.updatePlaceId(currPlace);
//                autoAttendanceSubjectDetailViewModel.refreshAllGeoFences();
                double lat = data.getDoubleExtra(LocationPickerActivityKt.LATITUDE, 0.0);
                double lang = data.getDoubleExtra(LocationPickerActivityKt.LONGITUDE, 0.0);
                Logger.d("Location is : " + lat + " , " + lang);
                //autoAttendanceSubjectDetailViewModel.refreshGeoFences(place);
            } else {
                Toast.makeText(this, "Error Occurred While Retrieving Data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
