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

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.viewmodels.SetUpViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.utils.ConverterUtils.convertTimeInInt;

public class SetUpLectureTimeFragment extends Fragment {

    @BindView(R.id.linear_set_up_lecture_time_fragment_container)
    LinearLayout linearContainer;

    private SetUpViewModel setUpViewModel;
    private int noOfLecturesPerDay;
    private OnLectureTimeSelectedListener listener;

    public SetUpLectureTimeFragment() {
    }

    public static SetUpLectureTimeFragment newInstance() {
        return new SetUpLectureTimeFragment();
    }

    private void clickedInfo() {
        BottomSheetDialog mDialog = new BottomSheetDialog(getContext());
        mDialog.setContentView(R.layout.item_holder_bottom_sheet_setup_time_info);
        mDialog.show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_lecture_time, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clickedInfo();
        setUpViewModel = ViewModelProviders.of(getActivity()).get(SetUpViewModel.class);
        populateUI();
    }

    private void populateUI() {
        noOfLecturesPerDay = setUpViewModel.getNoOfLecturesPerDay();
        linearContainer.removeAllViews();
        linearContainer.setPadding(4, 4, 4, 4);

        for (int i = 0; i < noOfLecturesPerDay * 2; i++) {
            if (i % 2 == 0) {
                TextView tv = new TextView(getContext());
                tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setText("Lecture : " + ((i / 2) + 1));
                linearContainer.addView(tv, i);
            } else {
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setPadding(4, 4, 4, 4);
                layout.setDividerPadding(4);
                layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                final MaterialButton btnStartTime = new MaterialButton(getContext());
                btnStartTime.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                btnStartTime.setText("Select start time");
                layout.addView(btnStartTime, 0);

                final MaterialButton btnEndTime = new MaterialButton(getContext());
                btnEndTime.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                btnEndTime.setText("Select end time");
                layout.addView(btnEndTime, 1);

                int finalI = i;
                btnStartTime.setOnClickListener(view -> {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, i12, i1) -> btnStartTime.setText(ConverterUtils.convertTimeIntInString(ConverterUtils.convertTimeInInt(i12 + ":" + i1))), 0, 0, false);
                    timePickerDialog.setTitle("Choose Starting Time for Lecture " + ((finalI / 2) + 1));
                    timePickerDialog.show();
                });

                btnEndTime.setOnClickListener(view -> {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i13, int i1) {
                            btnEndTime.setText(ConverterUtils.convertTimeIntInString(ConverterUtils.convertTimeInInt(i13 + ":" + i1)));
                        }
                    }, 0, 0, false);
                    timePickerDialog.setTitle("Choose Ending Time for Lecture " + ((finalI / 2) + 1));
                    timePickerDialog.show();
                });
                linearContainer.addView(layout, i);
            }
        }
    }

    @OnClick(R.id.btn_set_up_lecture_time_continue)
    void clickedContinue() {
        List<Integer> startTimes = new ArrayList<>();
        List<Integer> endTimes = new ArrayList<>();
        for (int i = 0; i < noOfLecturesPerDay * 2; i++) {
            if (i % 2 != 0) {
                LinearLayout linearLayout = (LinearLayout) linearContainer.getChildAt(i);

                String startTimeStr = ((MaterialButton) linearLayout.getChildAt(0)).getText().toString();
                String endTimeStr = ((MaterialButton) linearLayout.getChildAt(1)).getText().toString();

                if (!startTimeStr.contains(":") || !endTimeStr.contains(":")) {
                    Toast.makeText(getContext(), "Please Select time for All the Lectures", Toast.LENGTH_SHORT).show();
                    return;
                }

                int startTime = convertTimeInInt(startTimeStr);
                int endTime = convertTimeInInt(endTimeStr);

                if (endTime - startTime < 1) {
                    Toast.makeText(getContext(), "End time must be after Start time", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((i - 2) > 0) {
                    if (startTime < convertTimeInInt(((MaterialButton) ((LinearLayout) linearContainer.getChildAt(i - 2)).getChildAt(1)).getText().toString())) {
                        showSnackbar("Lecture " + i % 2 + "can not be before " + ((i % 2) - 2));
                    }
                }

                startTimes.add(startTime);
                endTimes.add(endTime);
                setUpViewModel.setLectureStartTimeInSharedPrefs(i / 2, startTime);
                Logger.d("Setting starting time for Lecture " + i / 2 + "as " + startTime + " Minutes");
                setUpViewModel.setLectureEndTimeInSharedPrefs(i / 2, endTime);
                Logger.d("Setting ending time for Lecture " + i / 2 + "as " + endTime + " Minutes");
            }
        }
        setUpViewModel.setTimeInDb(startTimes, endTimes);
        if (listener != null) {
            listener.OnLectureTimeSelected();
        }
    }

    private void showSnackbar(String s) {
        Snackbar.make(linearContainer, s, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_set_up_lecture_time_previous)
    void clickedPrevious() {
        if (listener != null) {
            listener.OnPreviousClickedOnSetUpLectureTime();
        }
    }

    public void setOnLectureTimeSelectedListener(OnLectureTimeSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnLectureTimeSelectedListener {
        void OnLectureTimeSelected();
        void OnPreviousClickedOnSetUpLectureTime();
    }
}
