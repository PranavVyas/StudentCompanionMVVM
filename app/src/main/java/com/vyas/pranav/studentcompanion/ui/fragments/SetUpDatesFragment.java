package com.vyas.pranav.studentcompanion.ui.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.picker.MaterialStyledDatePickerDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.models.CollageModel;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.viewmodels.SetUpViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SetUpDatesFragment extends Fragment {


    @BindView(R.id.btn_set_up_dates_fragment_continue)
    Button btnContinue;
    @BindView(R.id.btn_set_up_dates_fragment_end_date)
    Button btnEndDate;
    @BindView(R.id.btn_set_up_dates_fragment_start_date)
    Button btnStartDate;
    @BindView(R.id.et_set_up_dates_sem_no)
    TextInputEditText etSemNo;
    @BindView(R.id.text_input_set_up_dates_sem_no)
    TextInputLayout inputSemNo;
    @BindView(R.id.tv_set_up_dates_attendance_percent)
    TextView tvAttendancePercent;
    @BindView(R.id.seek_set_up_dates_attendance_crieteria)
    SeekBar seekBarAttendance;
    @BindView(R.id.spinner_setup_collage)
    Spinner spinnerCollage;

    private OnDatesSetUpListener mCallback;
    private SetUpViewModel setUpViewModel;
    private String semNo;


    public SetUpDatesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_up_dates, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViewModel = ViewModelProviders.of(getActivity()).get(SetUpViewModel.class);
        btnStartDate.setText(setUpViewModel.getStartDate());
        btnEndDate.setText(setUpViewModel.getEndDate());
        seekBarAttendance.setProgress(setUpViewModel.getCurrentAttendanceCriteria());
        tvAttendancePercent.setText(setUpViewModel.getCurrentAttendanceCriteria() + " %");
        seekBarAttendance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvAttendancePercent.setText(i + " %");
                setUpViewModel.setCurrentAttendanceCriteria(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setUpViewModel.getCollagesLiveData().observe(getActivity(), queryDocumentSnapshots -> {
            List<CollageModel> collageModels = queryDocumentSnapshots.toObjects(CollageModel.class);
            List<String> paths = new ArrayList<>();
            for (CollageModel x :
                    collageModels) {
                paths.add(x.getCity() + "/" + x.getName());
            }
            ArrayAdapter<String> mAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_simple_custom_main, paths);
            mAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinnerCollage.setAdapter(mAdapter);
            spinnerCollage.setSelection(paths.indexOf(setUpViewModel.getCurrentPath()));
            spinnerCollage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    setUpViewModel.setCurrentPath(paths.get(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        });
        showHelp();
    }

    private void showHelp() {
        BottomSheetDialog mDialog = new BottomSheetDialog(getContext());
        mDialog.setContentView(R.layout.item_holder_bottom_sheet_set_up_dates);
        mDialog.show();
    }

    @OnClick(R.id.btn_set_up_dates_fragment_continue)
    void continueClicked() {
        semNo = etSemNo.getText().toString().trim();
        if (setUpViewModel.getStartDate().equals("Select Starting Date")) {
            Toast.makeText(getContext(), "Select Starting date", Toast.LENGTH_SHORT).show();
        } else if (setUpViewModel.getEndDate().equals("Select Ending Date")) {
            Toast.makeText(getContext(), "Select Ending date", Toast.LENGTH_SHORT).show();
        } else if (getDifferenceInDates(setUpViewModel.getStartDate(), setUpViewModel.getEndDate()) < 15) {
            Toast.makeText(getContext(), "Difference between dates must be more than 15 Days\nEnding date must be after Starting date", Toast.LENGTH_SHORT).show();
        } else if (!validateSemNo()) {
            inputSemNo.setError("Please Input Correct Sem No");
        } else if (TextUtils.isEmpty(setUpViewModel.getCurrentPath())) {
            Toast.makeText(getContext(), "Collage is not Selected", Toast.LENGTH_SHORT).show();
        } else {
            inputSemNo.setErrorEnabled(false);
            setSemNo();
            if (mCallback != null) {
                mCallback.onDatesSetUp();
            }
        }
    }

    @OnClick(R.id.btn_set_up_dates_fragment_start_date)
    void selectStartingDate() {
        MaterialStyledDatePickerDialog.OnDateSetListener listener = (datePicker, i, i1, i2) -> {
            String startDateStr = ConverterUtils.formatDateStringFromCalender(i2, i1 + 1, i);
            setUpViewModel.setStartDate(startDateStr);
            btnStartDate.setText(setUpViewModel.getStartDate());
        };
        showDatePickerDialog(listener);
    }

    @SuppressLint("RestrictedApi")
    @OnClick(R.id.btn_set_up_dates_fragment_end_date)
    void selectEndingDate() {
        MaterialStyledDatePickerDialog.OnDateSetListener listener = (datePicker, i, i1, i2) -> {
            String endDateStr = ConverterUtils.formatDateStringFromCalender(i2, i1 + 1, i);
            setUpViewModel.setEndDate(endDateStr);
            btnEndDate.setText(setUpViewModel.getEndDate());
        };
        showDatePickerDialog(listener);
    }

    @SuppressLint("RestrictedApi")
    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener mListener) {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);
        MaterialStyledDatePickerDialog datePickerDialog = new MaterialStyledDatePickerDialog(getContext(), mListener, year, month, day);
        datePickerDialog.setTitle("Pick date Now");
        datePickerDialog.show();
    }

    public void setOnDatesSetUpListener(OnDatesSetUpListener mCallback) {
        this.mCallback = mCallback;
    }

    private int getDifferenceInDates(String startDate, String endDate) {
        Date start = ConverterUtils.convertStringToDate(startDate);
        Date end = ConverterUtils.convertStringToDate(endDate);
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);
        long diffInMillis = endCal.getTimeInMillis() - startCal.getTimeInMillis();
        //Toast.makeText(getContext(), "Diff in days are "+days, Toast.LENGTH_SHORT).show();
        return (int) TimeUnit.MILLISECONDS.toDays(diffInMillis);
    }

    private boolean validateSemNo() {
        if (semNo == null) {
            return false;
        } else if (semNo.isEmpty()) {
            return false;
        } else return Integer.valueOf(semNo) >= 1;
    }

    private void setSemNo() {
        setUpViewModel.setSemester(Integer.valueOf(semNo));
    }

    public interface OnDatesSetUpListener {
        void onDatesSetUp();
    }
}
