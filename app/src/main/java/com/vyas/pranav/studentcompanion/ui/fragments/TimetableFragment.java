package com.vyas.pranav.studentcompanion.ui.fragments;
/*
Student Companion - An Android App that has features like attendance manager, note manager etc
Copyright (C) 2019  Pranav Vyas

This file is a part of Student Companion.

Student Companion is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Student Companion is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.
*/

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.evrencoskun.tableview.TableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.TimetableTableAdapter;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.viewmodels.TimetableViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class TimetableFragment extends Fragment {
    @BindView(R.id.table_timetable_fragment_main)
    TableView tableTimetable;
    @BindView(R.id.switch_fragment_timetable_productive_view)
    LabeledSwitch switchProductiveView;
    @BindView(R.id.viewpager_fragment_timetable_day_switcher)
    ViewPager viewPagerDaySwitcher;

    private TimetableTableAdapter mAdapter;
    private TimetableViewModel timetableViewModel;
    private int lecturesPerDay;
    private int currentPage = 0;
    private boolean isProductiveViewOn = false;

    public TimetableFragment() {
    }

    public static TimetableFragment newInstance() {
        return new TimetableFragment();
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
        currentPage = timetableViewModel.getCurrentPage();
        isProductiveViewOn = timetableViewModel.isProductiveViewOn();

        switchProductiveView.setOn(isProductiveViewOn);
        final LiveData<List<TimetableEntry>> timetableEntriesLiveData = timetableViewModel.getTimetableEntries();
        timetableEntriesLiveData.observe(this, timetableEntries -> {
//                timetableEntriesLiveData.removeObserver(this);
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
            loadDataInViewPager(daysLectures, lectureNo);
            refreshView();
        });

        switchProductiveView.setOnToggledListener((toggleableView, isOn) -> {
            isProductiveViewOn = isOn;
            refreshView();
            timetableViewModel.setProductiveViewOn(isOn);
        });
    }

    private void loadDataInViewPager(List<List<String>> daysLectures, List<String> lectureNo) {
        viewPagerDaySwitcher.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                timetableViewModel.setCurrentPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerDaySwitcher.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                String day = null;
                ArrayList<String> lectures = null;
                switch (position) {
                    case 0:
                        lectures = (ArrayList<String>) daysLectures.get(0);
                        day = "Monday";
                        break;

                    case 1:
                        lectures = (ArrayList<String>) daysLectures.get(1);
                        day = "Tuesday";
                        break;

                    case 2:
                        lectures = (ArrayList<String>) daysLectures.get(2);
                        day = "Wednesday";
                        break;

                    case 3:
                        lectures = (ArrayList<String>) daysLectures.get(3);
                        day = "Thursday";
                        break;

                    case 4:
                        lectures = (ArrayList<String>) daysLectures.get(4);
                        day = "Friday";
                        break;
                }
                return TimetableDayFragment.newInstance(day, lectures);

            }

            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Monday";

                    case 1:
                        return "Tuesday";

                    case 2:
                        return "Wednesday";

                    case 3:
                        return "Thursday";

                    case 4:
                        return "Friday";

                    default:
                        return "Default Day";
                }
            }
        });
    }

    private void refreshView() {
        if (isProductiveViewOn) {
            viewPagerDaySwitcher.setVisibility(View.GONE);
            tableTimetable.setVisibility(View.VISIBLE);
        } else {
            viewPagerDaySwitcher.setVisibility(View.VISIBLE);
            tableTimetable.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_timetable_frag_sem_info)
    void semInfoClicked() {
        BottomSheetDialog mDialog = new BottomSheetDialog(getContext());
        mDialog.setContentView(R.layout.item_holder_alert_dialog_sem_info);
        mDialog.show();
        TextView tvSemNo = mDialog.findViewById(R.id.tv_holder_dialog_sem_info_sem);
        TextView tvStartDate = mDialog.findViewById(R.id.tv_holder_dialog_sem_info_start_date);
        TextView tvEndDate = mDialog.findViewById(R.id.tv_holder_dialog_sem_info_end_date);
        tvSemNo.setText("" + timetableViewModel.getSemInfo());
        tvStartDate.setText("" + timetableViewModel.getStartDate());
        tvEndDate.setText("" + timetableViewModel.getEndDate());
        Button btnOk = mDialog.findViewById(R.id.btn_holder_dialog_sem_info_ok);
        btnOk.setOnClickListener(view -> mDialog.dismiss());
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
