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
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.github.angads25.toggle.widget.LabeledSwitch;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceIndividualRecyclerAdapter extends ListAdapter<AttendanceEntry, AttendanceIndividualRecyclerAdapter.AttendanceIndividualHolder> {

    public static final DiffUtil.ItemCallback<AttendanceEntry> diffCallback = new DiffUtil.ItemCallback<AttendanceEntry>() {
        @Override
        public boolean areItemsTheSame(@NonNull AttendanceEntry oldItem, @NonNull AttendanceEntry newItem) {
            return oldItem.get_ID() == newItem.get_ID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull AttendanceEntry oldItem, @NonNull AttendanceEntry newItem) {
            return (oldItem.getDate().equals(newItem.getDate())) &&
                    (oldItem.getLectureNo() == newItem.getLectureNo()) &&
                    (oldItem.getSubjectName().equals(newItem.getSubjectName())) &&
                    (oldItem.getPresent() == newItem.getPresent());
        }
    };
    private boolean isCancelled = false;
    private static final String TAG = "AttendanceIndividualRec";
    private onAttendanceSwitchToggleListener listener;

    public AttendanceIndividualRecyclerAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public AttendanceIndividualHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_individual_attendance, parent, false);
        return new AttendanceIndividualHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceIndividualHolder holder, final int position) {
        holder.bindTo(getItem(position));
    }

    public void setOnAttendanceSwitchToggledListener(onAttendanceSwitchToggleListener listener) {
        this.listener = listener;
    }

    public interface onAttendanceSwitchToggleListener {
        void onAttendanceSwitchToggled(AttendanceEntry attendanceEntry);
    }

    class AttendanceIndividualHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recycler_individual_attendance_leture_no)
        TextView tvLectureNo;
        @BindView(R.id.tv_recycler_individual_attendance_subject_name)
        TextView tvSubjectName;
        @BindView(R.id.switch_recycler_individual_attendance_present)
        LabeledSwitch switchPresent;
        @BindView(R.id.button)
        Button btnCancel;

        AttendanceIndividualHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindTo(AttendanceEntry attendanceOfDay) {
            isCancelled = false;
            tvLectureNo.setText("Lecture " + attendanceOfDay.getLectureNo());
            tvSubjectName.setText(attendanceOfDay.getSubjectName());
            switchPresent.setOn(attendanceOfDay.getPresent() == Constants.PRESENT);

            View.OnClickListener onClickListener = v -> switchPresent.performClick();
            itemView.setOnClickListener(onClickListener);
            switchPresent.setOnToggledListener((toggleableView, isOn) -> {
                if (listener != null) {
                    attendanceOfDay.setPresent(isOn ? Constants.PRESENT : Constants.ABSENT);
                    listener.onAttendanceSwitchToggled(attendanceOfDay);
                } else {
                    Logger.d("Listener is not init");
                }
            });

            btnCancel.setOnClickListener((view) -> {
                if (!isCancelled) {
                    //class is cancelled
                    btnCancel.setText("UnCancel");
                    isCancelled = true;
                    attendanceOfDay.setPresent(0);
                    listener.onAttendanceSwitchToggled(attendanceOfDay);
                    switchPresent.setEnabled(false);
                } else {
                    btnCancel.setText("Cancel");
                    isCancelled = false;
                    attendanceOfDay.setPresent((switchPresent.isOn()) ? 1 : -1);
                    listener.onAttendanceSwitchToggled(attendanceOfDay);
                    switchPresent.setEnabled(true);
                }
            });
        }
    }

}
