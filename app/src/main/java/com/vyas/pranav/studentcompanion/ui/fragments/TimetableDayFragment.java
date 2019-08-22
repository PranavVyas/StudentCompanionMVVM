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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.TimetableDayRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vyas.pranav.studentcompanion.utils.Constants.EXTRA_TIMETABLE_DAY;
import static com.vyas.pranav.studentcompanion.utils.Constants.EXTRA_TIMETABLE_DAY_KEY;

public class TimetableDayFragment extends Fragment {

    @BindView(R.id.tv_fragment_timetable_day_day)
    TextView tvDay;
    @BindView(R.id.recycler_fragment_timetable_day_main)
    RecyclerView rvDays;
    private List<String> lectures;
    private String day;
    private TimetableDayRecyclerAdapter mAdapter;

    public TimetableDayFragment() {
    }

    public static TimetableDayFragment newInstance(String day, ArrayList<String> lectures) {
        TimetableDayFragment fragment = new TimetableDayFragment();
        if (day != null) {
            Bundle args = new Bundle();
            args.putStringArrayList(EXTRA_TIMETABLE_DAY_KEY, lectures);
            args.putString(EXTRA_TIMETABLE_DAY, day);
            fragment.setArguments(args);
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable_day, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle receivedData = getArguments();
        if (receivedData != null) {
            day = receivedData.getString(EXTRA_TIMETABLE_DAY);
            lectures = receivedData.getStringArrayList(EXTRA_TIMETABLE_DAY_KEY);
        }
        setUpRecycler();
        tvDay.setText(day);
    }

    private void setUpRecycler() {
        rvDays.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mAdapter = new TimetableDayRecyclerAdapter();
        rvDays.setAdapter(mAdapter);
        mAdapter.setData(lectures);
    }
}
