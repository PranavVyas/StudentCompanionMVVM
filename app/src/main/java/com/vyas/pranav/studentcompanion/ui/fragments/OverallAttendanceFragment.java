package com.vyas.pranav.studentcompanion.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.OverallAttendanceRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.viewmodels.OverallAttendanceViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OverallAttendanceFragment extends Fragment {

    @BindView(R.id.recycler_overall_attendance_fragment_main)
    RecyclerView rvOverallAttendance;

    private OverallAttendanceRecyclerAdapter mAdapter;
    private OverallAttendanceViewModel mViewModel;
    private static final String TAG = "OverallAttendanceFragment";

    public OverallAttendanceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overall_attendance, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
        setUpOverallAttendance();
        showInstruction(getActivity());
    }

    private void setUpRecyclerView() {
        mAdapter = new OverallAttendanceRecyclerAdapter();
        rvOverallAttendance.setAdapter(mAdapter);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.VERTICAL);
        rvOverallAttendance.setLayoutManager(lm);
    }

    private void setUpOverallAttendance() {
        mViewModel = ViewModelProviders.of(getActivity()).get(OverallAttendanceViewModel.class);
        mViewModel.getAllOverallAttendance().observe(getActivity(), new Observer<List<OverallAttendanceEntry>>() {
            @Override
            public void onChanged(List<OverallAttendanceEntry> overallAttendanceEntries) {
                mAdapter.submitList(overallAttendanceEntries);
            }
        });
    }

    @OnClick(R.id.button2)
    void clickedINfoButton() {
        Toast.makeText(getContext(), "Please Click on the Subject card to know more!", Toast.LENGTH_SHORT).show();
    }

    private void showInstruction(Activity activity) {
        new BubbleShowCaseBuilder(activity)
                .title(getContext().getString(R.string.instr_overall_card_title))
                .description(getContext().getString(R.string.instr_overall_card_desc))
                .targetView(rvOverallAttendance.getChildCount() == 0 ? rvOverallAttendance : rvOverallAttendance.getChildAt(0))
                .showOnce(TAG + "overallAttCard")
                .show();
    }
}
