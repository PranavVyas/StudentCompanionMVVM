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
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.ui.activities.OverallAttendanceDetailActivity;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.itangqi.waveloadingview.WaveLoadingView;

public class OverallAttendanceRecyclerAdapter extends ListAdapter<OverallAttendanceEntry, OverallAttendanceRecyclerAdapter.OverallAttendanceHolder> {

    private static final DiffUtil.ItemCallback<OverallAttendanceEntry> diffCallback = new DiffUtil.ItemCallback<OverallAttendanceEntry>() {
        @Override
        public boolean areItemsTheSame(@NonNull OverallAttendanceEntry oldItem, @NonNull OverallAttendanceEntry newItem) {
            return oldItem.get_ID() == newItem.get_ID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull OverallAttendanceEntry oldItem, @NonNull OverallAttendanceEntry newItem) {
            return (oldItem.getSubName().equals(newItem.getSubName())) &&
                    (oldItem.getPresentDays() == newItem.getPresentDays()) &&
                    (oldItem.getBunkedDays() == newItem.getBunkedDays()) &&
                    (oldItem.getTotalDays() == newItem.getTotalDays());
        }
    };
    private SharedPreferencesUtils sharedPreferencesUtils;
    private int currentAttendance = 0;

    public OverallAttendanceRecyclerAdapter() {
        super(diffCallback);
    }


    @NonNull
    @Override
    public OverallAttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_overall_attendance, parent, false);
        return new OverallAttendanceHolder(view);
    }

    private int getCurrentAttendanceCriteria(Context context) {
        if (sharedPreferencesUtils == null) {
            sharedPreferencesUtils = SharedPreferencesUtils.getInstance(context);
            currentAttendance = sharedPreferencesUtils.getCurrentAttendanceCriteria();
        }
        return currentAttendance;
    }

    @Override
    public void onBindViewHolder(@NonNull OverallAttendanceHolder holder, int position) {
        OverallAttendanceEntry item = getItem(position);

        ViewCompat.setTransitionName(holder.progressPresent, item.getSubName() + item.get_ID());
        ViewCompat.setTransitionName(holder.tvSubject, item.getSubName() + item.getSubName());

        holder.tvSubject.setText(item.getSubName());
        Context context = holder.itemView.getContext();
        int presentDays = item.getPresentDays();
        int bunkedDays = item.getBunkedDays();
        int totalDays = item.getTotalDays();
        int currentAttendanceCriteria = getCurrentAttendanceCriteria(holder.itemView.getContext());
        //For Example currentAttendanceCriteria = 75
        double dangerPercent = 100 - ((100.0 - currentAttendanceCriteria) * 3 / 3);
        //dangerPercent = 75
        double warningPercent = 100 - ((100.0 - currentAttendanceCriteria) * 2 / 3);
        //warning Percent = 83.33
        double safePercent = 100 - ((100.0 - currentAttendanceCriteria) / 3);
        //safe Percent = 91.67
        if (totalDays == 0) {
            holder.tvAvailableToBunk.setText("Subject is not in the timetable");
            holder.progressPresent.setProgressValue(100);
            holder.progressPresent.setCenterTitle("100 %");
            return;
        }

        View.OnClickListener listener = view -> {
            Intent openDetail = new Intent(view.getContext(), OverallAttendanceDetailActivity.class);
            Gson gson = new Gson();
            String JsonOverallAttendance = gson.toJson(getItem(position));
            if (getItem(position).getTotalDays() == 0) {
                Toast.makeText(view.getContext(), "Subject is not available in the timetable", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(),
                    Pair.create(holder.progressPresent, ViewCompat.getTransitionName(holder.progressPresent)),
                    Pair.create(holder.tvSubject, ViewCompat.getTransitionName(holder.tvSubject))).toBundle();
            openDetail.putExtra(OverallAttendanceDetailActivity.EXTRA_OVERALL_ATTENDANCE, JsonOverallAttendance);
            view.getContext().startActivity(openDetail, bundle);
        };

        holder.itemView.setOnClickListener(listener);
        holder.btnMore.setOnClickListener(listener);

        int currentAttendance = (int) Math.ceil((presentDays * 100f) / (presentDays + bunkedDays));
        int daysTotalAvailableToBunk = (int) Math.ceil(totalDays * (1f - (currentAttendanceCriteria / 100.0f)));
        int daysAvailableToBunk = daysTotalAvailableToBunk - bunkedDays;
        if (daysAvailableToBunk > 0) {
            holder.tvAvailableToBunk.setText(Html.fromHtml("You can still bunk <b>" + daysAvailableToBunk + "</b> lectures in this semester"));
        } else if (daysAvailableToBunk == 0) {
            holder.tvAvailableToBunk.setText(Html.fromHtml("You can not bunk <b>any lectures</b> in this semester"));
        } else {
            holder.tvAvailableToBunk.setText(Html.fromHtml("You have already bunked <b>" + (0 - daysAvailableToBunk) + " more lectures than required amount</b>"));
        }
        double maxAttendance = Math.ceil(((totalDays - bunkedDays) * 100.0) / totalDays);
        holder.progressPresent.setProgressValue(currentAttendance);
        holder.progressPresent.setCenterTitle(currentAttendance + " %");
        if (maxAttendance > safePercent) {
            holder.cardMain.setCardBackgroundColor(context.getResources().getColor(R.color.colorSafeOverallAttendance));
            return;
        }
        if (maxAttendance > warningPercent) {
            holder.cardMain.setCardBackgroundColor(context.getResources().getColor(R.color.colorWarningOverallAttendance));
            return;
        }
        if (maxAttendance > 0) {
            holder.cardMain.setCardBackgroundColor(context.getResources().getColor(R.color.colorDangerOverallAttendance));
            return;
        }
    }

    class OverallAttendanceHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_recycler_overall_attendance_present_percent)
        WaveLoadingView progressPresent;
        @BindView(R.id.tv_recycler_overall_attendance_available_to_bunk)
        TextView tvAvailableToBunk;
        @BindView(R.id.tv_recycler_overall_attendance_subject)
        TextView tvSubject;
        @BindView(R.id.card_recycler_overall_main)
        MaterialCardView cardMain;
        @BindView(R.id.btn_overall_attendance_more)
        Button btnMore;

        OverallAttendanceHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
