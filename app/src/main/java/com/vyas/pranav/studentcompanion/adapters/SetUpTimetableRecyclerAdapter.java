package com.vyas.pranav.studentcompanion.adapters;
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vyas.pranav.studentcompanion.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetUpTimetableRecyclerAdapter extends RecyclerView.Adapter<SetUpTimetableRecyclerAdapter.SetUpTimetableHolder> {
    private int lecturesPerDay = 0;
    private List<String> subjectsList = new ArrayList<>();
    private List<String> daySchedule = new ArrayList<>();

    @NonNull
    @Override
    public SetUpTimetableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_setup_timetable, parent, false);
        return new SetUpTimetableHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetUpTimetableHolder holder, int position) {
        holder.tvTitle.setText("Select subject for lecture No : " + (position + 1));
        holder.mSpinner.setSelection(holder.adapter.getPosition(daySchedule.get(position)));
    }

    @Override
    public int getItemCount() {
        return lecturesPerDay;
    }

    public void setItem(int lecturesPerDay) {
        this.lecturesPerDay = lecturesPerDay;
        String defaultSubject = subjectsList.get(0);
        daySchedule = new ArrayList<>();
        for (int i = 0; i < lecturesPerDay; i++) {
            daySchedule.add(i, defaultSubject);
        }
        notifyDataSetChanged();
    }

    public void setSubjectsList(List<String> subjectsList) {
        this.subjectsList = subjectsList;
    }

    public List<String> getDaySchedule() {
        return daySchedule;
    }

    class SetUpTimetableHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recycler_setup_timetable_title)
        TextView tvTitle;
        @BindView(R.id.spinner_recycler_setup_timetable_choose_subject)
        Spinner mSpinner;

        private ArrayAdapter<String> adapter;
        private String selectedSubject;

        SetUpTimetableHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            adapter = new ArrayAdapter<>(itemView.getContext(), R.layout.spinner_simple_custom_main, subjectsList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedSubject = (String) adapterView.getSelectedItem();
                    daySchedule.set(getAdapterPosition(), selectedSubject);
                    //Toast.makeText(view.getContext(), "Selected Item is " + selectedSubject, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Toast.makeText(adapterView.getContext(), "Nothing is selected", Toast.LENGTH_SHORT).show();
                }
            });
            mSpinner.setAdapter(adapter);
        }
    }

    public void setUpSchedule(List<String> schedule) {
        this.daySchedule = schedule;
        notifyDataSetChanged();
    }

}
