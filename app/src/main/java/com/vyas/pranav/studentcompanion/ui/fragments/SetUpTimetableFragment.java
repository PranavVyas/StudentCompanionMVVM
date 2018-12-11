package com.vyas.pranav.studentcompanion.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.SetUpTimetableRecyclerAdapter;
import com.vyas.pranav.studentcompanion.viewmodels.SetUpViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.utils.ConverterUtils.getDayFromInt;

public class SetUpTimetableFragment extends Fragment {

    @BindView(R.id.recycler_set_up_timetable_fragment)
    RecyclerView rvTimetable;
    @BindView(R.id.tv_set_up_timetable_day)
    TextView tvDay;

    private OnTimetableSelectedListener listener;
    private SetUpTimetableRecyclerAdapter mAdapter;
    private SetUpViewModel setUpViewModel;
    private int currentDay;
    private List<String> daySchedule;

    public SetUpTimetableFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_up_time_table, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViewModel = ViewModelProviders.of(getActivity()).get(SetUpViewModel.class);
        currentDay = setUpViewModel.getCurrentDay();
        tvDay.setText(getDayFromInt(currentDay));
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mAdapter = new SetUpTimetableRecyclerAdapter();
        mAdapter.setHasStableIds(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvTimetable.setLayoutManager(llm);
        rvTimetable.setAdapter(mAdapter);
        List<String> subjectList = setUpViewModel.getSubjectList();
        subjectList.add(subjectList.size(), "No Lecture");
        mAdapter.setSubjectsList(subjectList);
        mAdapter.setItem(currentDay, 4);
    }

    public void setOnTimeTableSelectedListener(OnTimetableSelectedListener listener) {
        this.listener = listener;
    }

    @OnClick(R.id.btn_set_up_timetable_fragment_continue)
    void continueClicked() {
        daySchedule = new ArrayList<>();
        daySchedule = mAdapter.getDaySchedule();
        setUpViewModel.initTimetableAttendance();
        setUpViewModel.setTimetableAttendanceForDay(currentDay, daySchedule);
        switch (currentDay) {
            case 1:
                currentDay = 2;
                break;

            case 2:
                currentDay = 3;
                break;

            case 3:
                currentDay = 4;
                break;

            case 4:
                currentDay = 5;
                break;

            case 5:
                if (listener != null) {
                    listener.onTimetableSelected();
                } else {
                    Toast.makeText(getContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                }
        }
        Logger.d("Schedule for " + getDayFromInt(currentDay) + " is " + daySchedule);
        mAdapter.setItem(currentDay, 4);
        tvDay.setText(getDayFromInt(currentDay));
        setUpViewModel.setCurrentDay(currentDay);
    }

    public interface OnTimetableSelectedListener {
        void onTimetableSelected();
    }
}
