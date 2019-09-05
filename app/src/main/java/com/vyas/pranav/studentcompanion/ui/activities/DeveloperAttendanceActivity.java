package com.vyas.pranav.studentcompanion.ui.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.picker.MaterialStyledDatePickerDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.repositories.OverallAttendanceRepository;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils.setUserTheme;

@SuppressLint("RestrictedApi")
public class DeveloperAttendanceActivity extends AppCompatActivity {

    @BindView(R.id.input_developer_attendance_new_value)
    TextInputLayout inputNewValue;
    @BindView(R.id.et_developer_attendance_new_value)
    TextInputEditText etNewValue;
    @BindView(R.id.btn_developer_attendance_from_date)
    Button btnFromDate;
    @BindView(R.id.btn_developer_attendance_to_date)
    Button btnToDate;
    @BindView(R.id.spinner_developer_attendance_subjects)
    Spinner spinnerSubjects;
    @BindView(R.id.toolbar_developer_attendance)
    Toolbar toolbar;

    private long toDate, fromDate;
    private String currentSubject;
    private int newValue;
    private SharedPreferencesUtils utils;
    private MainDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserTheme(this);
        setContentView(R.layout.activity_developer_attendance);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDb = MainDatabase.getInstance(this);
        utils = SharedPreferencesUtils.getInstance(this);
        List<String> subjectList = utils.getSubjectList();
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, R.layout.spinner_simple_custom_main, subjectList);
        mAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSubjects.setAdapter(mAdapter);
        currentSubject = subjectList.get(0);
        spinnerSubjects.setSelection(0);
        spinnerSubjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentSubject = subjectList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.btn_developer_attendance_from_date)
    void clickedFromDate() {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        MaterialStyledDatePickerDialog mDialog = new MaterialStyledDatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                int date = i2;
                int month = i1 + 1;
                int year = i;
                btnFromDate.setText(date + "/" + month + "/" + year);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month - 1, date);
                fromDate = calendar.getTimeInMillis();
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        mDialog.setTitle("Choose Date");
        mDialog.getDatePicker().setMaxDate(new Date().getTime());
        mDialog.getDatePicker().setMinDate(ConverterUtils.convertStringToDate(utils.getStartingDate()).getTime());
        mDialog.show();
    }

    @OnClick(R.id.btn_developer_attendance_to_date)
    void clickedToDate() {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        MaterialStyledDatePickerDialog mDialog = new MaterialStyledDatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                int date = i2;
                int month = i1 + 1;
                int year = i;
                btnToDate.setText(date + "/" + month + "/" + year);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month - 1, date);
                toDate = calendar.getTimeInMillis();
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        mDialog.setTitle("Choose Date");
        mDialog.getDatePicker().setMaxDate(new Date().getTime());
        mDialog.getDatePicker().setMinDate(ConverterUtils.convertStringToDate(utils.getStartingDate()).getTime());
        mDialog.show();
    }

    @OnClick(R.id.btn_developer_attendance_edit)
    void editClicked() {
        newValue = Integer.parseInt(etNewValue.getText().toString());
        startOperation();
        showSnackBar("New Value is : " + newValue + "\nWill be changed from " + btnFromDate.getText() + " to " + btnToDate.getText() + "\n Subject : " + currentSubject);
        showSnackBar("Date is " + new Date(fromDate) + " to " + new Date(toDate));
    }

    private void showSnackBar(String message) {
        Logger.d(message);
        Snackbar.make(toolbar, message, Snackbar.LENGTH_SHORT).show();
    }

    void startOperation() {
        if ((!TextUtils.isEmpty(currentSubject)) && (toDate > fromDate) && ((newValue == 0) || (newValue == 1))) {
            Date startDate = new Date(fromDate);
            Date endDate = new Date(toDate);
            AppExecutors.getInstance().diskIO().execute(() -> {
                List<AttendanceEntry> daysForSubject = mDb.attendanceDao().getDaysForSubject(currentSubject, startDate, endDate);
                for (AttendanceEntry x :
                        daysForSubject) {
                    x.setPresent(newValue == 1);
                }
                mDb.attendanceDao().updateAttendance(daysForSubject);
                OverallAttendanceRepository.getInstance(this).refreshOverallAttendanceForSubject(currentSubject);
                AppExecutors.getInstance().mainThread().execute(() -> {
                    showSnackBar("Done!");
                });
            });
        } else {
            showSnackBar("Condition does not match! Error 100");
        }
    }

}
