package com.vyas.pranav.studentcompanion.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.AttendanceIndividualRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDatabase;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.viewmodels.AttendanceForDateViewModel;
import com.vyas.pranav.studentcompanion.viewmodels.AttendanceForDateViewModelFactory;
import com.vyas.pranav.studentcompanion.viewmodels.OverallAttendanceForSubjectViewModel;
import com.vyas.pranav.studentcompanion.viewmodels.OverallAttendanceForSubjectViewModelFactory;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceIndividualFragment extends Fragment {
    private static final String TAG = "AttendanceIndividualFra";

    @BindView(R.id.recycler_attendance_individual_fragment_main)
    RecyclerView rvMain;
    @BindView(R.id.tv_attendance_individual_date)
    TextView tvDate;

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
        tvDate.setText(Constants.TEST_DATE_1);
        setUpDatabase();
        setUpRecyclerView();
        Date date = ConverterUtils.convertStringToDate(Constants.TEST_DATE_1);
        setUpIndividualAttendance(date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance_individual, container, false);
        ButterKnife.bind(this, view);
        return view;
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
