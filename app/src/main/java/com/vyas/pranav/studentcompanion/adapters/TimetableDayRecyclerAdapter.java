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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vyas.pranav.studentcompanion.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimetableDayRecyclerAdapter extends RecyclerView.Adapter<TimetableDayRecyclerAdapter.TimetableDayHolder> {

    private List<String> lectures;

    @NonNull
    @Override
    public TimetableDayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimetableDayHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_timetable_day, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimetableDayHolder holder, int position) {
        holder.tvDetail.setText("Lecture : " + (position + 1) +
                "\n" + lectures.get(position));
    }

    @Override
    public int getItemCount() {
        return (lectures == null) ? 0 : lectures.size();
    }

    public void setData(List<String> lectures) {
        this.lectures = lectures;
        notifyDataSetChanged();
    }

    class TimetableDayHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recycler_timetable_day_lecture_detail)
        TextView tvDetail;

        TimetableDayHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
