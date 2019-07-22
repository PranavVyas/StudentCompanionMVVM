package com.vyas.pranav.studentcompanion.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.OverallAttendanceRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.viewmodels.OverallAttendanceViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
                if (!mViewModel.isTutorialShownOnStarting()) {
                    showInstruction(getActivity());
                    mViewModel.setTutorialShownOnStarting(true);
                }
            }
        });

    }

    @OnClick(R.id.button2)
    void clickedINfoButton() {
        BottomSheetDialog mDialog = new BottomSheetDialog(getContext());
        mDialog.setContentView(R.layout.item_holder_bottom_sheet_overall_info);
        mDialog.show();
    }

    private void showInstruction(Activity activity) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new BubbleShowCaseBuilder(activity)
                        .title(getContext().getString(R.string.instr_overall_card_title))
                        .description(getContext().getString(R.string.instr_overall_card_desc))
                        .showOnce(TAG + "overallAttCard")
                        .show();
            }
        }, TimeUnit.SECONDS.toMillis(2));

    }
}
