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
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.firestore.NotificationFirestoreModel;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.DateAndTimePickerDialog;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.viewmodels.AddEventViewModel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEventActivity extends AppCompatActivity implements DateAndTimePickerDialog.OnDateAndTimeBothSelectedListener {

    @BindView(R.id.et_add_event_name)
    TextInputEditText etEventName;
    @BindView(R.id.et_add_event_event_url)
    TextInputEditText etEventUrl;
    @BindView(R.id.et_add_event_image_url)
    TextInputEditText etImageUrl;
    @BindView(R.id.et_add_event_short_desc)
    TextInputEditText etShortDesc;
    @BindView(R.id.et_add_event_venue)
    TextInputEditText etVenue;

    @BindView(R.id.input_add_event_name)
    TextInputLayout inputEventName;
    @BindView(R.id.input_add_event_event_url)
    TextInputLayout inputEventUrl;
    @BindView(R.id.input_add_event_image_url)
    TextInputLayout inputImageUrl;
    @BindView(R.id.input_add_event_short_desc)
    TextInputLayout inputShortDesc;
    @BindView(R.id.input_add_event_venue)
    TextInputLayout inputVenue;

    @BindView(R.id.toolbar_add_event)
    Toolbar toolbar;
    @BindView(R.id.tv_add_event_status_select_date)
    TextView tvStatus;

    private String name, url, imageUrl, desc, venue;
    private long dateInMillis;

    private FirebaseFirestore mDb;
    private CollectionReference collection;
    private AddEventViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesUtils.setUserTheme(this);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(AddEventViewModel.class);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mDb = FirebaseFirestore.getInstance();
        collection = mDb.collection(SharedPreferencesUtils.getInstance(this).getCurrentPath() + Constants.PATH_EVENTS_SVNIT);
        dateInMillis = viewModel.getDateInMillis();
        tvStatus.setText(TextUtils.isEmpty(viewModel.getDateStr()) ? "Please Select Date and Time!" : viewModel.getDateStr());
    }

    private boolean validateVenue() {
        venue = etVenue.getText().toString().trim();
        if (TextUtils.isEmpty(venue)) {
            inputVenue.setError("Venue is invalid");
            return false;
        } else {
            inputVenue.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUrl() {
        url = etEventUrl.getText().toString().trim();
        if (TextUtils.isEmpty(url)) {
            inputEventUrl.setError("Url is Empty");
            return false;
        } else {
            inputEventUrl.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateShortInfo() {
        desc = etShortDesc.getText().toString().trim();
        if (TextUtils.isEmpty(desc)) {
            inputShortDesc.setError("Short Description is Empty");
            return false;
        } else {
            inputShortDesc.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateName() {
        name = etEventName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            inputEventName.setError("Name is Empty");
            return false;
        } else {
            inputEventName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateImage() {
        imageUrl = etImageUrl.getText().toString().trim();
        if (TextUtils.isEmpty(imageUrl)) {
            inputImageUrl.setError("Image Url is Empty");
            return false;
        } else {
            inputImageUrl.setErrorEnabled(false);
            return true;
        }
    }

    @OnClick(R.id.btn_add_event_post)
    void startUploadingData() {
        if (validateDate() & validateImage() & validateName() & validateShortInfo() & validateUrl() & validateVenue()) {
            collection.add(new NotificationFirestoreModel(name, desc, imageUrl, url, venue, String.valueOf(dateInMillis), Constants.NOTI_TYPE_EVENT))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddEventActivity.this, "Uploaded Event", Toast.LENGTH_SHORT).show();
                            AddEventActivity.this.finish();
                        } else {
                            Toast.makeText(AddEventActivity.this, "Not Uploaded Event", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean validateDate() {
        if (new Date().compareTo(new Date(dateInMillis)) < 0) {
            return true;
        } else {
            Toast.makeText(this, "Date should be after the current date", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @OnClick(R.id.btn_add_event_date)
    void startSelectingDate() {
        dateInMillis = 0;
        DateAndTimePickerDialog pickerDialog = new DateAndTimePickerDialog();
        pickerDialog.show(getSupportFragmentManager(), "PICKER_DIALOG");
        pickerDialog.setOnDateAndTimeBothSelectedListener(this);
    }

    @Override
    public void OnDateAndTimeBothSelected(SublimePicker sublimeMaterialPicker, SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
        dateInMillis = selectedDate.getStartDate().getTimeInMillis() + TimeUnit.HOURS.toMillis(hourOfDay) + TimeUnit.MINUTES.toMillis(minute);
        viewModel.setDateInMillis(dateInMillis);
        viewModel.setDateStr("Event will occur on : " + ConverterUtils.convertDateToString(new Date(dateInMillis)) + " At Time : " + hourOfDay + ":" + minute);
        tvStatus.setText(viewModel.getDateStr());
    }
}