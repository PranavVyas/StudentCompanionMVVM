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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.HolidayRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayEntry;
import com.vyas.pranav.studentcompanion.viewmodels.HolidayViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HolidayFragment extends Fragment {

    @BindView(R.id.recycler_holiday_fragment_main)
    RecyclerView rvHoliday;

    private HolidayRecyclerAdapter mAdapter;
    private HolidayViewModel holidayViewModel;

    public HolidayFragment() {
    }

    public static HolidayFragment newInstance() {
        return new HolidayFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_holiday, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
        holidayViewModel = ViewModelProviders.of(this).get(HolidayViewModel.class);
        holidayViewModel.getHolidayEntries().observe(this, new Observer<List<HolidayEntry>>() {
            @Override
            public void onChanged(List<HolidayEntry> holidayEntries) {
                holidayViewModel.getHolidayEntries().removeObserver(this);
                mAdapter.setHolidays(holidayEntries);
            }
        });
    }

    private void setUpRecyclerView() {
        mAdapter = new HolidayRecyclerAdapter();
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        rvHoliday.setAdapter(mAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), llm.getOrientation());
        rvHoliday.addItemDecoration(decoration);
        rvHoliday.setLayoutManager(llm);
    }
}
