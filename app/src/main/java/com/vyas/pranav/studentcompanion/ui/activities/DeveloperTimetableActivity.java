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

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.TimetableTableAdapter;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.viewmodels.DeveloperTimetableViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.utils.AttendanceUtils.refreshNewTimetable;
import static com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils.setUserTheme;

public class DeveloperTimetableActivity extends AppCompatActivity implements ITableViewListener {

    @BindView(R.id.toolbar_developer_timetable)
    Toolbar toolbar;
    @BindView(R.id.table_developer_timetable)
    TableView tableTimetable;
    List<String> weekDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
    private TimetableTableAdapter mAdapter;
    private DeveloperTimetableViewModel viewModel;
    private int lecturesPerDay;
    private List<String> columnHeadings;
    private List<List<String>> daysLectures;
    private List<String> Monday, Tuesday, Wednesday, Thursday, Friday;
    private String oldSub, newSub;
    private List<String> subList;
    private MainDatabase mDb;
    private int semester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserTheme(this);
        setContentView(R.layout.activity_developer_timetable);
        ButterKnife.bind(this);
        init();
        bindUi();
    }

    private void init() {
        viewModel = ViewModelProviders.of(this).get(DeveloperTimetableViewModel.class);
        lecturesPerDay = viewModel.getLecturesPerDay();
        subList = viewModel.getSubjectList();
        subList.add(Constants.DEFAULT_LECTURE);
        mDb = MainDatabase.getInstance(this);
        semester = viewModel.getSemInfo();
    }

    private void bindUi() {
        mAdapter = new TimetableTableAdapter(this);
        tableTimetable.setAdapter(mAdapter);
        columnHeadings = getColumnHeaders(lecturesPerDay);
        mAdapter.setAllItems(columnHeadings, weekDays, daysLectures);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewModel.getDaysLectureLiveData().observe(this, lists -> {
            daysLectures = lists;
            mAdapter.setCellItems(daysLectures);
        });
        tableTimetable.setTableViewListener(this);
    }

    private List<String> getColumnHeaders(int lecturesPerDay) {
        List<String> columnHeader = new ArrayList<>();
        for (int i = 0; i < lecturesPerDay; i++) {
            String header = "Lecture" + (i + 1);
            int startingTime = viewModel.getStartingTimeOfLecture(i);
            int endingTime = viewModel.getEndingTimeOfLecture(i);
            String start = ConverterUtils.convertTimeIntInString(startingTime);
            String end = ConverterUtils.convertTimeIntInString(endingTime);
            header = header + "\n" + start + "\nTo\n" + end;
            columnHeader.add(header);
        }
        return columnHeader;
    }

    //Ex no of lectures = 7, days = 5
    //Weekdays = maximum 5
    //column Index = maximum as no of lecture = 7

    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder viewHolder, int columnIndex, int rawIndex) {
        oldSub = daysLectures.get(rawIndex).get(columnIndex);
        int lectureNo = (columnIndex + 1);
        String day = weekDays.get(rawIndex);
        showSnackBar("Day : " + day + " Lecture No :" + lectureNo + " Subject Old : " + oldSub);
        BottomSheetDialog promptDialog = new BottomSheetDialog(this);
        promptDialog.setContentView(R.layout.item_holder_bottom_sheet_timetable_edit);
        promptDialog.setOnDismissListener(dialogInterface -> {
            if (!newSub.equals(oldSub)) {
                showSnackBar("Subject : " + oldSub + " was changed to " + newSub);
            } else {
                showSnackBar("That entry was not changed!");
            }
        });
        promptDialog.show();

        TextView tvLecture = promptDialog.findViewById(R.id.tv_bottom_dev_timetable_lecture);
        TextView tvSubject = promptDialog.findViewById(R.id.tv_bottom_dev_timetable_subject);
        TextView tvDay = promptDialog.findViewById(R.id.tv_bottom_dev_timetable_day);
        Spinner spinnerSubjects = promptDialog.findViewById(R.id.spinner_bottom_dev_timetable_new_subject);
        tvLecture.setText("Lecture No : " + lectureNo);
        tvDay.setText("on : " + day);
        tvSubject.setText("Old Subject : " + oldSub);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, R.layout.spinner_simple_custom_main, subList);
        mAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSubjects.setAdapter(mAdapter);
        spinnerSubjects.setSelection(subList.indexOf(oldSub));
        newSub = oldSub;
        spinnerSubjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                newSub = subList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btnEdit = promptDialog.findViewById(R.id.btn_bottom_dev_timetable_done);
        btnEdit.setOnClickListener((view) -> {
            changeTemporary(columnIndex, rawIndex, newSub);
            promptDialog.dismiss();
        });

    }

    private void changeTemporary(int column, int raw, String newSub) {
        viewModel.setDaysLectures(raw, column, newSub);
    }


    private void showSnackBar(String s) {
        Logger.d(s);
        Snackbar.make(toolbar, s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCellLongPressed(@NonNull RecyclerView.ViewHolder viewHolder, int i, int i1) {

    }

    @Override
    public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }


    @OnClick(R.id.btn_developer_timetable_edit)
    void finalized() {
        showSnackBar("Please wait for 15 seconds to apply changes! Then You have to RESTART app to work properly.");
        refreshNewTimetable(this, viewModel.getSemInfo(), daysLectures, weekDays, columnHeadings, ConverterUtils.convertDateToString(new Date()));
    }
}
