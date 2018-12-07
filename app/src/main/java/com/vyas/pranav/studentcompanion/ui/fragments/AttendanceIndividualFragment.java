package com.vyas.pranav.studentcompanion.ui.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.AttendanceIndividualRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.ConvertterUtils;
import com.vyas.pranav.studentcompanion.viewmodels.AttendanceForDateViewModel;
import com.vyas.pranav.studentcompanion.viewmodels.AttendanceForDateViewModelFactory;

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
    private AttendanceDatabase mDb;
    private AttendanceForDateViewModel attendanceViewModel;

    private AttendanceIndividualRecyclerAdapter mAdapter;

    public AttendanceIndividualFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
        mDb = AttendanceDatabase.getInstance(getContext());
        AttendanceForDateViewModelFactory factory = new AttendanceForDateViewModelFactory(mDb, ConvertterUtils.convertStringToDate("06/12/2018"));
        attendanceViewModel = ViewModelProviders.of(getActivity(), factory).get(AttendanceForDateViewModel.class);
        attendanceViewModel.getAttendanceForDate().observe(this, new Observer<List<AttendanceEntry>>() {
            @Override
            public void onChanged(List<AttendanceEntry> attendanceEntries) {
                Log.d(TAG, "onChanged: Size of list is " + attendanceEntries.size());
                Log.d(TAG, "onChanged: Subject 1 Name is " + attendanceEntries.get(0).getSubjectName());
                mAdapter.setAttendanceForDate(attendanceEntries);
            }
        });
        mAdapter.setOnAttendanceSwitchToggledListener(new AttendanceIndividualRecyclerAdapter.onAttendanceSwitchToggleListener() {
            @Override
            public void onAttendanceSwitchToggled(AttendanceEntry attendanceEntry) {
                attendanceViewModel.updateAttendance(attendanceEntry);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance_individual, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void setUpRecyclerView() {
        mAdapter = new AttendanceIndividualRecyclerAdapter();
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.VERTICAL);
        rvMain.setAdapter(mAdapter);
        rvMain.setLayoutManager(lm);
    }

}
