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
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils.setUserTheme;

public class DeveloperActivity extends AppCompatActivity {

    @BindView(R.id.item_developer_bulk_attendance)
    ConstraintLayout bulkAttendance;
    @BindView(R.id.toolbar_developer)
    Toolbar toolbar;

    SharedPreferencesUtils utils;
    MainDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserTheme(this);
        setContentView(R.layout.activity_developer);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        utils = new SharedPreferencesUtils(this);
        mDb = MainDatabase.getInstance(this);
//        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(this);
//        if (sharedPreferencesUtils.isDeveloperEnabled()){
//            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
//            materialAlertDialogBuilder.setMessage("\u25CF Please Note that This settings are only for developer use and should not be changed by any other but developer.\n\u25CF This may harm or stop application from working properly also\n\u25CF Make sure to take backup before doing anything here!")
//                    .setTitle("Caution")
//                    .setBackgroundInsetTop(R.drawable.image_account_background_new)
//                    .setPositiveButton("I understand", ((dialogInterface, i) -> {
//                        dialogInterface.dismiss();
//                    }))
//                    .setNegativeButton("Don't show me again", ((dialogInterface, i) -> {
//                        sharedPreferencesUtils.setDeveloperEnabled(false);
//                        dialogInterface.dismiss();
//                    }));
//        }
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

    @OnClick(R.id.item_developer_delete_subject)
    void clickedDeleteSubject() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.item_holder_bottom_sheet_delete_sub);
        bottomSheetDialog.show();

        Spinner subSpinner = bottomSheetDialog.findViewById(R.id.spinner_holder_delete_subject_subject);
        Button btnDelete = bottomSheetDialog.findViewById(R.id.btn_holder_delete_subject_edit);

        List<String> subjects = utils.getSubjectList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_simple_custom_main, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subSpinner.setSelection(0);
        subSpinner.setAdapter(adapter);

        btnDelete.setOnClickListener((view -> {
            deleteSubject((String) subSpinner.getSelectedItem(), subjects, bottomSheetDialog);
        }));
    }

    @OnClick(R.id.item_developer_add_subject)
    void clickedAddSubject() {
        BottomSheetDialog mDialog = new BottomSheetDialog(this);
        mDialog.setContentView(R.layout.item_holder_bottom_sheet_add_subject);

        mDialog.show();

        TextInputLayout inputSub = mDialog.findViewById(R.id.input_holder_add_subject);
        TextInputEditText etSub = mDialog.findViewById(R.id.et_holder_add_subject);
        Button btnAdd = mDialog.findViewById(R.id.btn_holder_add_subject_add);

        btnAdd.setOnClickListener((view -> {
            String subject = etSub.getText().toString().trim();
            if (TextUtils.isEmpty(subject)) {
                Toast.makeText(this, "Subject can not be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            List<String> subjectList = utils.getSubjectList();
            if (subjectList.contains(subject)) {
                Toast.makeText(this, "Subject is already present!", Toast.LENGTH_SHORT).show();
                return;
            }
            subjectList.add(subject);
            utils.setSubjectListInSharedPrefrences(subjectList);
            AppExecutors.getInstance().diskIO().execute(() -> {
                AttendanceDao attendanceDao = MainDatabase.getInstance(this).attendanceDao();
                OverallAttendanceEntry x = new OverallAttendanceEntry();
                Date todayDate = new Date();
                int presentDays = attendanceDao.getAttendedDaysForSubject(subject, ConverterUtils.convertStringToDate(utils.getStartingDate()), todayDate);
                int bunkedDays = attendanceDao.getBunkedDaysForSubject(subject, ConverterUtils.convertStringToDate(utils.getStartingDate()), todayDate);
                int totalDays = attendanceDao.getTotalDaysForSubject(subject, ConverterUtils.convertStringToDate(utils.getStartingDate()), ConverterUtils.convertStringToDate(utils.getEndingDate()));
                x.setTotalDays(totalDays);
                x.setBunkedDays(bunkedDays);
                x.setPresentDays(presentDays);
                x.setSubName(subject);
                mDb.overallAttendanceDao().insertOverall(x);
                mDialog.dismiss();
            });
        }));
    }

    private void deleteSubject(String deleteSub, List<String> subjectList, BottomSheetDialog bottomSheetDialog) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            subjectList.remove(deleteSub);
            utils.setSubjectListInSharedPrefrences(subjectList);
            mDb.overallAttendanceDao().deleteAttendance(deleteSub);
            mDb.autoAttendancePlaceDao().deletePlaceEntry(deleteSub);
            List<AttendanceEntry> allAttendanceOfSub = mDb.attendanceDao().getAllAttendanceOfSub(deleteSub);
            for (AttendanceEntry x :
                    allAttendanceOfSub) {
                x.setSubjectName(Constants.DEFAULT_LECTURE);
            }
            mDb.attendanceDao().updateAttendance(allAttendanceOfSub);
            List<TimetableEntry> timetableForSubjectToDelete = mDb.timetableDao().getTimetableForSubjectToDelete(deleteSub);
            for (TimetableEntry x :
                    timetableForSubjectToDelete) {
                x.setSubName(Constants.DEFAULT_LECTURE);
            }
            mDb.timetableDao().updateTimetableEntries(timetableForSubjectToDelete);
            AppExecutors.getInstance().mainThread().execute(() -> {
                Toast.makeText(this, "Subject: " + deleteSub + " is deleted", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });
        });

    }
}
