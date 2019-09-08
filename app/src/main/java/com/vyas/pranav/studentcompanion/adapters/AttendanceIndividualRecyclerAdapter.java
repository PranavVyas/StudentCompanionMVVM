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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        @BindView(R.id.image_recycler_individual_attendance_present)
        ImageView btnPresent;
        @BindView(R.id.image_recycler_individual_attendance_absent)
        ImageView btnAbsent;
        @BindView(R.id.image_recycler_individual_attendance_cancel)
        ImageView btnCancel;
        @BindView(R.id.tv_recycler_individual_attendance_status)
        TextView tvStatus;
        @BindView(R.id.card_recycler_individual_attendance)
        MaterialCardView cardMain;

        private int present;

        AttendanceIndividualHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.image_recycler_individual_attendance_present)
        void presentClicked() {
            present = Constants.PRESENT;
//            RotateAnimation animation = new RotateAnimation(0,360);
            Animation rotateAnimation = AnimationUtils.loadAnimation(btnPresent.getContext(), R.anim.rotate_clockwise);
            btnPresent.startAnimation(rotateAnimation);
            refreshTextView();
            sendLatestData();
        }

        @OnClick(R.id.image_recycler_individual_attendance_absent)
        void absentClicked() {
            present = Constants.ABSENT;
            Animation rotateAnimation = AnimationUtils.loadAnimation(btnPresent.getContext(), R.anim.rotate_anticlockwise);
            btnAbsent.startAnimation(rotateAnimation);
            refreshTextView();
            sendLatestData();
        }

        @OnClick(R.id.image_recycler_individual_attendance_cancel)
        void cancelClicked() {
            present = Constants.CANCELLED;
            Animation rotateAnimation = AnimationUtils.loadAnimation(btnPresent.getContext(), R.anim.rotate_clockwise);
            btnCancel.startAnimation(rotateAnimation);
            refreshTextView();
            sendLatestData();
        }

        void refreshTextView() {
            switch (present) {
                case Constants.PRESENT:
                    tvStatus.setText("Present");
                    cardMain.setCardBackgroundColor(cardMain.getContext().getResources().getColor(R.color.colorSafeOverallAttendance));
                    break;

                case Constants.ABSENT:
                    tvStatus.setText("Absent");
                    cardMain.setCardBackgroundColor(cardMain.getContext().getResources().getColor(R.color.colorDangerOverallAttendance));
                    break;

                case Constants.CANCELLED:
                    tvStatus.setText("Cancelled");
                    cardMain.setCardBackgroundColor(cardMain.getContext().getResources().getColor(R.color.colorWarningOverallAttendance));
                    break;
            }
        }

        void sendLatestData() {
            AttendanceEntry attendanceOfDay = getItem(getAdapterPosition());
            attendanceOfDay.setPresent(present);
            if (listener != null) {
                listener.onAttendanceSwitchToggled(attendanceOfDay);
            } else {
                Toast.makeText(cardMain.getContext(), "Listener is not attached Properly", Toast.LENGTH_SHORT).show();
            }
        }

        public void bindTo(AttendanceEntry attendanceOfDay) {
            present = attendanceOfDay.getPresent();
            tvLectureNo.setText("Lecture\n" + attendanceOfDay.getLectureNo());
            tvSubjectName.setText(attendanceOfDay.getSubjectName());
            refreshTextView();
        }
    }

}
