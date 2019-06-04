package com.vyas.pranav.studentcompanion.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.SetUpTimetableRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.viewmodels.SetUpViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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
    @BindView(R.id.btn_set_up_timetable_fragment_continue)
    Button btnContinue;
    @BindView(R.id.btn_set_up_timetable_fragment_previous)
    Button btnPrevious;

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
        setUpViewModel.initTimetableAttendance();
    }

    private void setUpRecyclerView() {
        mAdapter = new SetUpTimetableRecyclerAdapter();
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvTimetable.setLayoutManager(llm);
        rvTimetable.setAdapter(mAdapter);
        List<String> subjectList = setUpViewModel.getSubjectList();
        subjectList.add(subjectList.size(), Constants.DEFAULT_LECTURE);
        mAdapter.setSubjectsList(subjectList);
        mAdapter.setItem(setUpViewModel.getNoOfLecturesPerDay());
    }

    public void setOnTimeTableSelectedListener(OnTimetableSelectedListener listener) {
        this.listener = listener;
    }

    @OnClick(R.id.btn_set_up_timetable_fragment_previous)
    void clickedPrevious() {
        switch (currentDay) {
            case 1:
                if (listener != null) {
                    listener.onPreviousClickedInSetUpTimetable();
                } else {
                    Toast.makeText(getContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                }
                break;

            case 2:
                currentDay = 1;
                break;

            case 3:
                currentDay = 2;
                break;

            case 4:
                currentDay = 3;
                break;

            case 5:
                currentDay = 4;
                break;
        }
        final LiveData<List<TimetableEntry>> timetableAttendanceForDay = setUpViewModel.getTimetableAttendanceForDay(currentDay);
        timetableAttendanceForDay.observe(this, new Observer<List<TimetableEntry>>() {
            @Override
            public void onChanged(List<TimetableEntry> timetableEntries) {
                timetableAttendanceForDay.removeObserver(this);
                List<String> schedule = new ArrayList<>();
                for (int i = 0; i < timetableEntries.size(); i++) {
                    schedule.add(timetableEntries.get(i).getLectureNo() - 1, timetableEntries.get(i).getSubName());
                }
                mAdapter.setUpSchedule(schedule);
            }
        });
        setUpViewModel.setCurrentDay(currentDay);
        tvDay.setText(getDayFromInt(currentDay));
    }

    @OnClick(R.id.btn_set_up_timetable_fragment_continue)
    void continueClicked() {
        daySchedule = new ArrayList<>();
        daySchedule = mAdapter.getDaySchedule();
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
                    btnContinue.setEnabled(false);
                    listener.onTimetableSelected();
                } else {
                    Toast.makeText(getContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                }
        }
        Logger.d("Schedule for " + getDayFromInt(currentDay) + " is " + daySchedule);
        mAdapter.setItem(setUpViewModel.getNoOfLecturesPerDay());
        tvDay.setText(getDayFromInt(currentDay));
        setUpViewModel.setCurrentDay(currentDay);
    }

    public interface OnTimetableSelectedListener {
        void onTimetableSelected();
        void onPreviousClickedInSetUpTimetable();
    }
}
