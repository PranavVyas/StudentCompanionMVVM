package com.vyas.pranav.studentcompanion.ui.activities;
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
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.fragments.SetUpDatesFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.SetUpDetailsSemFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.SetUpLectureTimeFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.SetUpTimetableNew;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.AttendanceUtils;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.viewmodels.SetUpViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetUpActivity extends AppCompatActivity implements SetUpDatesFragment.OnDatesSetUpListener, SetUpDetailsSemFragment.OnSubjectsSelectedListener, SetUpTimetableNew.OnTimetableSelectedListenerNew, SetUpLectureTimeFragment.OnLectureTimeSelectedListener {

    private static final String TAG = "SetUpActivity";

    @BindView(R.id.toolbar_setup_activity)
    Toolbar toolbar;
    @BindView(R.id.constraint_set_up_placeholder)
    ConstraintLayout placeHolder;
    @BindView(R.id.frame_setup_activity_container)
    FrameLayout rootLayout;

    private SetUpViewModel setUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesUtils.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setUpViewModel = ViewModelProviders.of(this).get(SetUpViewModel.class);
        if (setUpViewModel.isFirstRun()) {
            new Handler().postDelayed(this::showInstruction, TimeUnit.SECONDS.toMillis(1));
        }
        if (!setUpViewModel.isFirstRun()) {
            if (setUpViewModel.isTutorialDone()) {
                Intent openMainActivity = new Intent(this, MainActivity.class);
                startActivity(openMainActivity);
            } else {
                Intent openTutorial = new Intent(this, TutorialActivity.class);
                startActivity(openTutorial);
            }
            finish();
            return;
        }
        setUpViewModel.init();
        executeSetUpStep(setUpViewModel.getCurrentStep());
    }

    private void showInstruction() {
        new BubbleShowCaseBuilder(this)
                .title("Dynamic Help is Here!")
                .description("For Help on each Step, Here is Help Menu!!\nFor Each page help is changed according to the step!")
                .targetView(findViewById(R.id.menu_set_up))
                .showOnce(TAG + "SetUpActivity")
                .show();
    }

    private void executeSetUpStep(int step) {
        retryClicked();
        switch (step) {
            case 1:
                SetUpDatesFragment setUpDatesFragment = SetUpDatesFragment.newInstance();
                addAnimationsToFragment(setUpDatesFragment);
                setUpDatesFragment.setOnDatesSetUpListener(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_setup_activity_container, setUpDatesFragment)
                        .commit();
                break;

            case 2:
                SetUpDetailsSemFragment setUpDetailsSemFragment = SetUpDetailsSemFragment.newInstance();
                setUpDetailsSemFragment.setOnSubjectSelectedListener(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_setup_activity_container, setUpDetailsSemFragment)
                        .commit();
                break;

            case 3:
                SetUpLectureTimeFragment setUpLectureTimeFragment = SetUpLectureTimeFragment.newInstance();
                addAnimationsToFragment(setUpLectureTimeFragment);
                setUpLectureTimeFragment.setOnLectureTimeSelectedListener(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_setup_activity_container, setUpLectureTimeFragment)
                        .commit();
                break;

            case 4:
                SetUpTimetableNew setUpTimetableFragment = SetUpTimetableNew.newInstance();
                addAnimationsToFragment(setUpTimetableFragment);
                setUpTimetableFragment.setOnTimeTableSelectedListenerNew(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_setup_activity_container, setUpTimetableFragment)
                        .commit();
                break;

            case 5:
                Intent intent = new Intent(this, TutorialActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void addAnimationsToFragment(Fragment fragment) {
        fragment.setEnterTransition(new Slide(Gravity.END));
        fragment.setExitTransition(new Slide(Gravity.START));
        fragment.setReturnTransition(new Slide(Gravity.START));
        fragment.setReenterTransition(new Slide(Gravity.END));
        fragment.setAllowEnterTransitionOverlap(false);
        fragment.setAllowReturnTransitionOverlap(false);
    }

    @Override
    public void onDatesSetUp() {
        setUpViewModel.setCurrentStep(2);
        executeSetUpStep(setUpViewModel.getCurrentStep());
    }

    @Override
    public void onSubjectSelected() {
        setUpViewModel.setCurrentStep(3);
        executeSetUpStep(setUpViewModel.getCurrentStep());
    }

    @Override
    public void OnLectureTimeSelected() {
        setUpViewModel.setCurrentStep(4);
        executeSetUpStep(setUpViewModel.getCurrentStep());
    }

//    @Override
//    public void onTimetableSelected() {
//        setUpViewModel.saveHolidaysAndInitAttendance();
//        Intent intent = new Intent(this, TutorialActivity.class);
//        startActivity(intent);
//        setUpViewModel.setFirstRun(false);
//        finish();
//    }

    @Override
    public void onPreviousClickedOnSemSetUp() {
        setUpViewModel.setCurrentStep(1);
        executeSetUpStep(setUpViewModel.getCurrentStep());
    }

    @Override
    public void OnPreviousClickedOnSetUpLectureTime() {
        setUpViewModel.setCurrentStep(2);
        executeSetUpStep(setUpViewModel.getCurrentStep());
    }
//
//    @Override
//    public void onPreviousClickedInSetUpTimetable() {
//        setUpViewModel.setCurrentStep(3);
//        executeSetUpStep(setUpViewModel.getCurrentStep());
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setup_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        BottomSheetDialog mDialog = new BottomSheetDialog(this);
        switch (setUpViewModel.getCurrentStep()) {
            case 1:
                mDialog.setContentView(R.layout.item_holder_bottom_sheet_set_up_dates);
                mDialog.show();
                return true;

            case 2:
                mDialog.setContentView(R.layout.item_holder_bottom_sheet_set_up_subject);
                mDialog.show();
                return true;


            case 3:
                mDialog.setContentView(R.layout.item_holder_bottom_sheet_setup_time_info);
                mDialog.show();
                return true;

            case 4:
                mDialog.setContentView(R.layout.item_holder_bottom_sheet_set_up_lecture);
                mDialog.show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showPlaceHolder(boolean isShown) {
        AppExecutors.getInstance().mainThread().execute(() -> {
            if (isShown) {
                rootLayout.setVisibility(View.GONE);
                placeHolder.setVisibility(View.VISIBLE);
            } else {
                rootLayout.setVisibility(View.VISIBLE);
                placeHolder.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.btn_set_up_placeholder_retry)
    void retryClicked() {
        AppExecutors.getInstance().networkIO().execute(() -> {
            if (AttendanceUtils.hasInternetAccess(SetUpActivity.this)) {
                //Hide Placeholder
                showPlaceHolder(false);
            } else {
                //Show Placeholder
                showPlaceHolder(true);
            }
        });
    }

    @Override
    public void onTimetableSelectedNew(List<List<String>> subjects, List<String> days, List<String> columnTitles) {
        setUpViewModel.setCurrentStep(5);
        setUpViewModel.saveHolidaysAndInitAttendance(subjects, days, columnTitles);
        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
        setUpViewModel.setFirstRun(false);
        finish();
    }

    @Override
    public void onPreviousClickedInSetUpTimetableNew() {
        setUpViewModel.setCurrentStep(3);
        executeSetUpStep(setUpViewModel.getCurrentStep());
    }
}
