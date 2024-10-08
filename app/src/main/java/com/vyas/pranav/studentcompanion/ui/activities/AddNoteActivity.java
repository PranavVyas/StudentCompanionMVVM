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
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.notedatabase.NotesEntry;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.viewmodels.AddNoteViewModel;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.adapters.NoteRecyclerAdapter.EXTRA_EDIT_NOTE;
import static com.vyas.pranav.studentcompanion.adapters.NoteRecyclerAdapter.EXTRA_TYPE_EDIT_ADD_NOTE;

public class AddNoteActivity extends AppCompatActivity {

    @BindView(R.id.input_add_note_title)
    TextInputLayout inputTitle;
    @BindView(R.id.input_add_note_desc)
    TextInputLayout inputDesc;
    @BindView(R.id.et_add_note_title)
    TextInputEditText etTitle;
    @BindView(R.id.et_add_note_desc)
    TextInputEditText etDesc;
    @BindView(R.id.btn_add_note_select_date)
    Button btnDate;
    @BindView(R.id.toolbar_add_note)
    Toolbar toolbar;
    @BindView(R.id.btn_add_note_add)
    Button btnAdd;

    private String title, desc, date;
    private AddNoteViewModel viewModel;
    private boolean isNewNote = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesUtils.setUserTheme(this);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(AddNoteViewModel.class);

        if (getIntent().hasExtra(EXTRA_TYPE_EDIT_ADD_NOTE)) {
            isNewNote = false;
            NotesEntry note = new Gson().fromJson(getIntent().getStringExtra(EXTRA_EDIT_NOTE), NotesEntry.class);
            viewModel.setNote(note);
            btnAdd.setText("Update");
        }

        title = viewModel.getTitle();
        desc = viewModel.getDesc();
        date = viewModel.getDate();

        etDesc.setText(desc);
        etTitle.setText(title);
        btnDate.setText(date);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @SuppressLint("RestrictedApi")
    @OnClick(R.id.btn_add_note_select_date)
    void selectDate() {
        Calendar now = Calendar.getInstance();
        MaterialStyledDatePickerDialog datePickerDialog = new MaterialStyledDatePickerDialog(
                this,
                (datePicker, year, i1, day) -> {
                    int month = i1 + 1;
                    date = ConverterUtils.formatDateStringFromCalender(day, month, year);
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                viewModel.setDate(date);
                btnDate.setText(date);
            }
        });
    }

    @OnClick(R.id.btn_add_note_add)
    void addNote() {
        if (validateTitle() & validateDesc() & validateDate()) {
            if (isNewNote) {
                viewModel.addNote(new NotesEntry(title, ConverterUtils.convertStringToDate(date), desc));
            } else {
                viewModel.updateNote(new NotesEntry(viewModel.getId(), title, ConverterUtils.convertStringToDate(date), desc));
            }
            finish();
        }
    }

    private boolean validateDate() {
        boolean isValid = !viewModel.getDate().equals(Constants.DATE_BTN_DEFAULT);
        if (!isValid) {
            Toast.makeText(this, "Date is not Valid", Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }

    private boolean validateDesc() {
        desc = etDesc.getText().toString().trim();
        viewModel.setDesc(desc);
        boolean isValid = !TextUtils.isEmpty(viewModel.getDesc());
        if (!isValid) {
            inputDesc.setError("Please Input Desc Correctly");
        } else {
            inputDesc.setErrorEnabled(false);
        }
        return isValid;
    }

    private boolean validateTitle() {
        title = etTitle.getText().toString().trim();
        viewModel.setTitle(title);
        boolean isValid = !TextUtils.isEmpty(viewModel.getTitle());
        if (!isValid) {
            inputTitle.setError("Please Input Title Correctly");
        } else {
            inputTitle.setErrorEnabled(false);
        }
        return isValid;
    }
}
