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
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.viewmodels.SetUpViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class SetUpDetailsSemFragment extends Fragment {

    @BindView(R.id.btn_set_up_details_sem_fragment_go)
    Button go;
    @BindView(R.id.btn_set_up_details_dem_continue)
    Button btnContinue;
    @BindView(R.id.et_set_up_details_sem_fragment_no_of_subjects)
    TextInputEditText etNoOfSubjects;
    @BindView(R.id.et_set_up_sem_details_no_of_lectures)
    TextInputEditText etNoOfLecturesPerDay;
    @BindView(R.id.linear_set_up_details_sem_container)
    LinearLayout linearContainer;
    @BindView(R.id.text_input_set_up_details_sem_fragment_no_of_subjects)
    TextInputLayout inputSubject;
    @BindView(R.id.text_input_set_up_sem_details_no_of_lectures)
    TextInputLayout inputNoOfLectures;

    private SetUpViewModel setUpViewModel;
    private OnSubjectsSelectedListener listener;

    private String noOfSubjects;
    private String noOfLecturesPerDay;


    public SetUpDetailsSemFragment() {
    }

    public static SetUpDetailsSemFragment newInstance() {
        return new SetUpDetailsSemFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_up_details_sem, container, false);
        ButterKnife.bind(this, view);
        showHelp();
        return view;
    }

    private void showHelp() {
        BottomSheetDialog mDialog = new BottomSheetDialog(getContext());
        mDialog.setContentView(R.layout.item_holder_bottom_sheet_set_up_subject);
        mDialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViewModel = ViewModelProviders.of(getActivity()).get(SetUpViewModel.class);
        btnContinue.setEnabled(false);
    }

    @OnClick(R.id.btn_set_up_details_sem_fragment_go)
    void goClicked() {
        noOfSubjects = etNoOfSubjects.getText().toString().trim();
        noOfLecturesPerDay = etNoOfLecturesPerDay.getText().toString().trim();

        if (!validateNoOfSubjects() || !validateNoOfLectures()) {
            if (!validateNoOfLectures()) {
                inputNoOfLectures.setError("Please enter valid No Of Lecture");
            } else {
                inputNoOfLectures.setErrorEnabled(false);
            }

            if (!validateNoOfSubjects()) {
                inputSubject.setError("Please Enter valid No Of Subjects");
            } else {
                inputSubject.setErrorEnabled(false);
            }

            return;
        }
        inputSubject.setErrorEnabled(false);
        inputNoOfLectures.setErrorEnabled(false);
        setUpViewModel.setNoOfLecturesPerDay(Integer.parseInt(noOfLecturesPerDay));
        linearContainer.removeAllViews();
        for (int i = 0; i < Integer.parseInt(noOfSubjects); i++) {
            LinearLayout subjectDetailsContainer = new LinearLayout(getContext());
            subjectDetailsContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            subjectDetailsContainer.setOrientation(LinearLayout.HORIZONTAL);
            TextInputLayout textInputLayout = new TextInputLayout(getContext(), null, R.style.MyCustomOutlinedBoxTextInput);
            textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextInputEditText et = new TextInputEditText(getContext());
            et.setHint("Subject " + (i + 1) + " Name");
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            et.setMaxLines(1);
            et.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            textInputLayout.addView(et);
            subjectDetailsContainer.addView(textInputLayout);
            linearContainer.addView(subjectDetailsContainer, i);
        }
        btnContinue.setEnabled(true);
    }

    @OnClick(R.id.btn_set_up_details_dem_previous)
    void clickedPrevious() {
        if (listener != null) {
            listener.onPreviousClickedOnSemSetUp();
        }
    }

    @OnClick(R.id.btn_set_up_details_dem_continue)
    void clickedContinue() {
        String noOfSubjects = etNoOfSubjects.getText().toString().trim();
        List<String> subjects = new ArrayList<>();
        int noOfSubjectsNo = Integer.parseInt(noOfSubjects);
        for (int i = 0; i < noOfSubjectsNo; i++) {
            LinearLayout subjectDetailsContainer = (LinearLayout) linearContainer.getChildAt(i);
            TextInputLayout textInputLayout = ((TextInputLayout) subjectDetailsContainer.getChildAt(0));
            String strSubject = textInputLayout.getEditText().getText().toString().trim();
            if (strSubject.isEmpty()) {
                Toast.makeText(getContext(), "Please Fill Subject " + (i / 2), Toast.LENGTH_SHORT).show();
                textInputLayout.setError("Please insert correct details");
                return;
            } else {
                textInputLayout.setErrorEnabled(false);
            }
            subjects.add(strSubject);
        }
        setUpViewModel.setSubjectList(subjects);
        if (listener != null) {
            listener.onSubjectSelected();
        }
    }

    public void setOnSubjectSelectedListener(OnSubjectsSelectedListener listener) {
        this.listener = listener;
    }

    private boolean validateNoOfSubjects() {
        if (noOfSubjects.isEmpty()) {
            showSnackbar("No of Subject can not be empty!");
            return false;
        }
        if (Integer.valueOf(noOfSubjects) >= 1 && Integer.valueOf(noOfSubjects) <= 10) {
            return true;
        } else {
            showSnackbar("No of Subjects should be 1 to 10");
            return false;
        }
    }

    private void showSnackbar(String s) {
        Snackbar.make(btnContinue, s, Snackbar.LENGTH_SHORT).show();
    }

    private boolean validateNoOfLectures() {
        if (noOfLecturesPerDay.isEmpty()) {
            showSnackbar("No of Lecture can not be empty!");
            return false;
        }
        if (Integer.valueOf(noOfLecturesPerDay) >= 1 && Integer.valueOf(noOfLecturesPerDay) <= 10) {
            return true;
        } else {
            showSnackbar("No of Lecture should be 1 to 10");
            return false;
        }
    }

    public interface OnSubjectsSelectedListener {
        void onSubjectSelected();
        void onPreviousClickedOnSemSetUp();
    }

}
