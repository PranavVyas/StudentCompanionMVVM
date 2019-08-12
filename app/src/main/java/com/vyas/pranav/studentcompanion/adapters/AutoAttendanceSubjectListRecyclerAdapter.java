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
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.activities.AutoAttendanceSubjectDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AutoAttendanceSubjectListRecyclerAdapter extends RecyclerView.Adapter<AutoAttendanceSubjectListRecyclerAdapter.AutoAttendanceSubjectHolder> {

    public static final String EXTRA_SUBJECT_NAME = "AutoAttendanceSubjectHolder.EXTRA_SUBJECT_NAME";

    private List<String> subjectList;

    @NonNull
    @Override
    public AutoAttendanceSubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AutoAttendanceSubjectHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_auto_attendance_list_subject, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AutoAttendanceSubjectHolder holder, int position) {
        holder.tvNo.setText("" + (position + 1));
        holder.tvSubject.setText(subjectList.get(position));
    }

    @Override
    public int getItemCount() {
        return subjectList == null ? 0 : subjectList.size();
    }

    public void setSubjectList(List<String> subjectList) {
        this.subjectList = subjectList;
        notifyDataSetChanged();
    }

    class AutoAttendanceSubjectHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recycler_auto_attendance_list_subject)
        TextView tvSubject;
        @BindView(R.id.tv_recycler_auto_attendance_list_subject_no)
        TextView tvNo;

        AutoAttendanceSubjectHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                Intent startAutoAttendanceEditActivity = new Intent(itemView.getContext(), AutoAttendanceSubjectDetailActivity.class);
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && subjectList != null) {
                    startAutoAttendanceEditActivity.putExtra(EXTRA_SUBJECT_NAME, subjectList.get(position));
                }
                itemView.getContext().startActivity(startAutoAttendanceEditActivity);
            });
        }


    }
}
