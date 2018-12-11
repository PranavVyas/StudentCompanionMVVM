package com.vyas.pranav.studentcompanion.ui.fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.AttendanceIndividualRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.DateConverter;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDatabase;
import com.vyas.pranav.studentcompanion.ui.activities.AttendanceIndividualActivity;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.viewmodels.AttendanceForDateViewModel;
import com.vyas.pranav.studentcompanion.viewmodels.AttendanceForDateViewModelFactory;
import com.vyas.pranav.studentcompanion.viewmodels.OverallAttendanceForSubjectViewModel;
import com.vyas.pranav.studentcompanion.viewmodels.OverallAttendanceForSubjectViewModelFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceIndividualFragment extends Fragment {
    private static final String TAG = "AttendanceIndividualFra";

    @BindView(R.id.recycler_attendance_individual_fragment_main)
    RecyclerView rvMain;
    @BindView(R.id.tv_attendance_individual_date)
    TextView tvDate;
    @BindView(R.id.btn_attendance_individual_fragment_other_attendance)
    Button btnOpenOtherAttendance;

    private AttendanceForDateViewModel attendanceViewModel;
    private OverallAttendanceForSubjectViewModel overallAttendanceViewModel;
    private OverallAttendanceDatabase mOverallDb;
    private AttendanceDatabase mAttendanceDb;
    private AttendanceIndividualRecyclerAdapter mAdapter;

    public AttendanceIndividualFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpDatabase();
        setUpRecyclerView();
        if (getArguments() == null) {
            Date date = ConverterUtils.convertStringToDate(Constants.TEST_DATE_1);
            setUpIndividualAttendance(date);
            tvDate.setText(Constants.TEST_DATE_1);
        } else {
            String dateStr = getArguments().getString(AttendanceIndividualActivity.EXTRA_DATE);
            Date date = ConverterUtils.convertStringToDate(dateStr);
            setUpIndividualAttendance(date);
            tvDate.setText(dateStr);
            btnOpenOtherAttendance.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance_individual, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_attendance_individual_fragment_other_attendance)
    void openDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int i1, int day) {
                        int month = i1 + 1;
                        String selectedDate = ConverterUtils.formatDateStringFromCalender(day, month, year);
                        Intent intent = new Intent(getContext(), AttendanceIndividualActivity.class);
                        intent.putExtra(AttendanceIndividualActivity.EXTRA_DATE, selectedDate);
                        getContext().startActivity(intent);
                        //Toast.makeText(getContext(), "i = "+i+" i1 = "+i1+" i2 = "+i2, Toast.LENGTH_SHORT).show();
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        //TODO [ENHANCEMENT] Set title of date picker
        datePickerDialog.getDatePicker().setMaxDate(DateConverter.toTimeStamp(new Date()));
        datePickerDialog.getDatePicker().setMinDate(DateConverter.toTimeStamp(ConverterUtils.convertStringToDate(Constants.SEM_START_DATE_STR)));
        datePickerDialog.show();
    }

    //TODO Next step : Don't setUpDatabase in the UI Class but use getContext.getApplicationContext() to send application context in the viewModel
    private void setUpDatabase() {
        mOverallDb = OverallAttendanceDatabase.getInstance(getContext());
        mAttendanceDb = AttendanceDatabase.getInstance(getContext());
    }

    private void setUpRecyclerView() {
        mAdapter = new AttendanceIndividualRecyclerAdapter();
        mAdapter.setHasStableIds(true);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.VERTICAL);
        rvMain.setAdapter(mAdapter);
        rvMain.setLayoutManager(lm);
    }

    private void setUpIndividualAttendance(Date date) {
        AttendanceForDateViewModelFactory factory = new AttendanceForDateViewModelFactory(mAttendanceDb, date);
        attendanceViewModel = ViewModelProviders.of(getActivity(), factory).get(AttendanceForDateViewModel.class);
        attendanceViewModel.getAttendanceForDate().observe(this, new Observer<List<AttendanceEntry>>() {
            @Override
            public void onChanged(List<AttendanceEntry> attendanceEntries) {
                mAdapter.setAttendanceForDate(attendanceEntries);
            }
        });
        /*TODO [PROBABLE BUG] Might be needed to refresh all the overall attendance(Which depends on the attendance database updated) as updating the attendance database and refreshing attendance happens simultaniously
        So case happens that the attendance is being updated but the Overall attendance is refreshed already
        Sol : create a member string (in viewModel) and init as null than check if the string is null and if not than setupOverallAttendance() in onChanged of attendanceEntries*/
        mAdapter.setOnAttendanceSwitchToggledListener(new AttendanceIndividualRecyclerAdapter.onAttendanceSwitchToggleListener() {
            @Override
            public void onAttendanceSwitchToggled(AttendanceEntry attendanceEntry) {
                attendanceViewModel.updateAttendance(attendanceEntry);
                setUpOverallAttendance(attendanceEntry.getSubjectName());
            }
        });
    }

    private void setUpOverallAttendance(String subName) {
        OverallAttendanceForSubjectViewModelFactory factory = new OverallAttendanceForSubjectViewModelFactory(subName, mOverallDb, mAttendanceDb);
        overallAttendanceViewModel = ViewModelProviders.of(getActivity(), factory).get(OverallAttendanceForSubjectViewModel.class);
        overallAttendanceViewModel.refreshOverallAttendance(subName);
    }
}
