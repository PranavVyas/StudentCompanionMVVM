package com.vyas.pranav.studentcompanion.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.TableView;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.TimetableTableAdapter;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.viewmodels.TimetableViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TimetableFragment extends Fragment {
    @BindView(R.id.table_timetable_fragment_main)
    TableView tableTimetable;

    private TimetableTableAdapter mAdapter;
    private TimetableViewModel timetableViewModel;
    private int lecturesPerDay;

    public TimetableFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new TimetableTableAdapter(getContext());
        timetableViewModel = ViewModelProviders.of(getActivity()).get(TimetableViewModel.class);
        lecturesPerDay = timetableViewModel.getLecturesPerDay();

        final LiveData<List<TimetableEntry>> timetableEntriesLiveData = timetableViewModel.getTimetableEntries();
        timetableEntriesLiveData.observe(this, new Observer<List<TimetableEntry>>() {
            @Override
            public void onChanged(List<TimetableEntry> timetableEntries) {
                timetableEntriesLiveData.removeObserver(this);
                List<String> Monday = new ArrayList<>();
                List<String> Tuesday = new ArrayList<>();
                List<String> Wednesday = new ArrayList<>();
                List<String> Thursday = new ArrayList<>();
                List<String> Friday = new ArrayList<>();
                List<String> weekDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));

                for (int i = 0; i < timetableEntries.size(); i++) {
                    int day = i / lecturesPerDay;
                    int lecture = i % lecturesPerDay;

                    switch (day) {
                        case 0:
                            //Monday
                            Monday.add(lecture, timetableEntries.get(i).getSubName());
                            break;

                        case 1:
                            //Tuesday
                            Tuesday.add(lecture, timetableEntries.get(i).getSubName());
                            break;

                        case 2:
                            //Wednesday
                            Wednesday.add(lecture, timetableEntries.get(i).getSubName());
                            break;

                        case 3:
                            //Thursday
                            Thursday.add(lecture, timetableEntries.get(i).getSubName());
                            break;

                        case 4:
                            //Friday
                            Friday.add(lecture, timetableEntries.get(i).getSubName());
                            break;
                    }

                }
                List<List<String>> daysLectures = new ArrayList<>(Arrays.asList(
                        Monday, Tuesday, Wednesday, Thursday, Friday
                ));
                List<String> lectureNo = getColumnHeaders(lecturesPerDay);
                tableTimetable.setAdapter(mAdapter);
                mAdapter.setAllItems(lectureNo, weekDays, daysLectures);
                //tableTimetable.setColumnWidth(1,LinearLayout.LayoutParams.WRAP_CONTENT);
                //tableTimetable.setColumnWidth(2,LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        });
    }

    private List<String> getColumnHeaders(int lecturesPerDay) {
        List<String> columnHeader = new ArrayList<>();
        for (int i = 0; i < lecturesPerDay; i++) {
            String header = "Lecture" + (i + 1);
            int startingTime = timetableViewModel.getStartingTimeOfLecture(i);
            int endingTime = timetableViewModel.getEndingTimeOfLecture(i);
            String start = ConverterUtils.convertTimeIntInString(startingTime);
            String end = ConverterUtils.convertTimeIntInString(endingTime);
            header = header + "\n" + start + "\nTo\n" + end;
            columnHeader.add(header);
        }
        return columnHeader;
    }
}
