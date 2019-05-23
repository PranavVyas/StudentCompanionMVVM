package com.vyas.pranav.studentcompanion.ui.fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
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
import com.vyas.pranav.studentcompanion.viewmodels.OverallAttendanceViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AttendanceIndividualFragment extends Fragment {
    private static final String TAG = "AttendanceIndividualFra";

    @BindView(R.id.recycler_attendance_individual_fragment_main)
    RecyclerView rvMain;
    @BindView(R.id.tv_attendance_individual_date)
    TextView tvDate;
    @BindView(R.id.btn_attendance_individual_fragment_other_attendance)
    Button btnOpenOtherAttendance;
    @BindView(R.id.progress_attendance_individul_fragment_main)
    ProgressBar mProgress;
    @BindView(R.id.placeholder_attendance_individual_holidays)
    ConstraintLayout placeHolderHoldidays;

    private AttendanceForDateViewModel attendanceViewModel;
    private OverallAttendanceForSubjectViewModel overallAttendanceViewModel;
    private OverallAttendanceViewModel model;
    private OverallAttendanceDatabase mOverallDb;
    private AttendanceDatabase mAttendanceDb;
    private AttendanceIndividualRecyclerAdapter mAdapter;
//    private SkeletonScreen skeletonScreen;

    public AttendanceIndividualFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpDatabase();
        setUpRecyclerView();
        Date date = new Date();
        date = ConverterUtils.convertStringToDate(ConverterUtils.convertDateToString(date));
        model = ViewModelProviders.of(getActivity()).get(OverallAttendanceViewModel.class);
        if (getArguments() != null) {
            String dateStr = getArguments().getString(AttendanceIndividualActivity.EXTRA_DATE);
            date = ConverterUtils.convertStringToDate(dateStr);
            btnOpenOtherAttendance.setVisibility(View.GONE);
        }
        setUpIndividualAttendance(date);
        tvDate.setText(ConverterUtils.convertDateToString(date) + "\n" + ConverterUtils.getDayOfWeek(date));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        datePickerDialog.setTitle("Choose Date");
        datePickerDialog.getDatePicker().setMaxDate(DateConverter.toTimeStamp(new Date()));
        datePickerDialog.getDatePicker().setMinDate(DateConverter.toTimeStamp(model.getStartingDate()));
        datePickerDialog.show();
    }

    //TODO Next step : Don't setUpDatabase in the UI Class but use getContext.getApplicationContext() to send application context in the viewModel
    private void setUpDatabase() {
        mOverallDb = OverallAttendanceDatabase.getInstance(getContext());
        mAttendanceDb = AttendanceDatabase.getInstance(getContext());
    }

    private void setUpRecyclerView() {
        mAdapter = new AttendanceIndividualRecyclerAdapter();
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.VERTICAL);
        rvMain.setLayoutManager(lm);
//        skeletonScreen = Skeleton.bind(rvMain).adapter(mAdapter).load(R.layout.item_holder_recycler_indivdual_attendance_shimmer).count(3).show();
        mAdapter.setHasStableIds(true);
//        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), lm.getOrientation());
//        rvMain.addItemDecoration(decoration);
        startProgress();
        rvMain.setAdapter(mAdapter);
    }

    private void setUpIndividualAttendance(Date date) {
        AttendanceForDateViewModelFactory factory = new AttendanceForDateViewModelFactory(mAttendanceDb, date);
        attendanceViewModel = ViewModelProviders.of(getActivity(), factory).get(AttendanceForDateViewModel.class);
        attendanceViewModel.getAttendanceForDate().observe(this, new Observer<List<AttendanceEntry>>() {
            @Override
            public void onChanged(final List<AttendanceEntry> attendanceEntries) {
                List<AttendanceEntry> finalAttendance;
                if (!attendanceEntries.isEmpty()) {
                    finalAttendance = new ArrayList<>();
                    for (int i = 0; i < attendanceEntries.size(); i++) {
                        AttendanceEntry attendance = attendanceEntries.get(i);
                        if (!attendance.getSubjectName().equals(Constants.DEFAULT_LECTURE)) {
                            finalAttendance.add(attendance);
                        }
                    }
                    mAdapter.setAttendanceForDate(finalAttendance);
                    stopProgress();
                    showHolidayPlaceHolder(false);
                    return;
                }
                showHolidayPlaceHolder(true);
                stopProgress();
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

    private void startProgress() {
        rvMain.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
    }

    private void stopProgress() {
        rvMain.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }

    private void setUpOverallAttendance(String subName) {
        OverallAttendanceForSubjectViewModelFactory factory = new OverallAttendanceForSubjectViewModelFactory(subName, mOverallDb, mAttendanceDb, getContext().getApplicationContext());
        overallAttendanceViewModel = ViewModelProviders.of(getActivity(), factory).get(OverallAttendanceForSubjectViewModel.class);
        overallAttendanceViewModel.refreshOverallAttendance(subName);
    }

    private void showHolidayPlaceHolder(boolean isShown) {
        if (isShown) {
            rvMain.setVisibility(View.GONE);
            placeHolderHoldidays.setVisibility(View.VISIBLE);
        } else {
            rvMain.setVisibility(View.VISIBLE);
            placeHolderHoldidays.setVisibility(View.GONE);
        }
    }
}
