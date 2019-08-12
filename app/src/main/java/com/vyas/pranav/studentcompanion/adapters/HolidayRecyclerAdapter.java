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
import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayEntry;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HolidayRecyclerAdapter extends RecyclerView.Adapter<HolidayRecyclerAdapter.HolidayHolder> {

    private List<HolidayEntry> holidays;

    @NonNull
    @Override
    public HolidayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_holiday, parent, false);
        return new HolidayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolidayHolder holder, int position) {
        if (position == 0) {
            holder.tvName.setText("Holiday Name");
            holder.tvDay.setText("Day");
            holder.tvDate.setText("Date");
            return;
        }
        holder.tvDate.setText(ConverterUtils.convertDateToString(holidays.get(position - 1).getDate()));
        holder.tvDay.setText(holidays.get(position - 1).getDay());
        holder.tvName.setText(holidays.get(position - 1).getName());
    }

    @Override
    public int getItemCount() {
        return holidays == null ? 1 : holidays.size() + 1;
    }

    public void setHolidays(List<HolidayEntry> holidays) {
        this.holidays = holidays;
        notifyDataSetChanged();
    }

    class HolidayHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recycler_holiday_fragment_date)
        TextView tvDate;
        @BindView(R.id.tv_recycler_holiday_fragment_day)
        TextView tvDay;
        @BindView(R.id.tv_recycler_holiday_fragment_name)
        TextView tvName;

        HolidayHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
