package com.vyas.pranav.studentcompanion.ui.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.repositories.SharedPreferencesRepository;
import com.vyas.pranav.studentcompanion.ui.fragments.SetUpDatesFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.SetUpDetailsSemFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.SetUpLectureTimeFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.SetUpTimetableFragment;
import com.vyas.pranav.studentcompanion.viewmodels.SetUpViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetUpActivity extends AppCompatActivity implements SetUpDatesFragment.OnDatesSetUpListener, SetUpDetailsSemFragment.OnSubjectsSelectedListener, SetUpTimetableFragment.OnTimetableSelectedListener, SetUpLectureTimeFragment.OnLectureTimeSelectedListener {

    @BindView(R.id.toolbar_setup_activity)
    Toolbar toolbar;

    private SetUpViewModel setUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesRepository.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setUpViewModel = ViewModelProviders.of(this).get(SetUpViewModel.class);
        if (!setUpViewModel.isFirstRun()) {
            if (setUpViewModel.isTutorialDone()) {
                Intent openMainActivity = new Intent(this, MainActivity.class);
                startActivity(openMainActivity);
            } else {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
                Intent openTutorial = new Intent(this, TutorialActivity.class);
                startActivity(openTutorial, bundle);
            }
            finish();
            return;
        }
        setUpViewModel.init();
        executeSetUpStep(setUpViewModel.getCurrentStep());
    }

    public void executeSetUpStep(int step) {
        switch (step) {
            case 1:
                SetUpDatesFragment setUpDatesFragment = new SetUpDatesFragment();
                addAnimationsToFragment(setUpDatesFragment);
                setUpDatesFragment.setOnDatesSetUpListener(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_setup_activity_container, setUpDatesFragment)
                        .commit();
                break;

            case 2:
                SetUpDetailsSemFragment setUpDetailsSemFragment = new SetUpDetailsSemFragment();
                setUpDetailsSemFragment.setOnSubjectSelectedListener(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_setup_activity_container, setUpDetailsSemFragment)
                        .commit();
                break;

            case 3:
                SetUpLectureTimeFragment setUpLectureTimeFragment = new SetUpLectureTimeFragment();
                addAnimationsToFragment(setUpLectureTimeFragment);
                setUpLectureTimeFragment.setOnLectureTimeSelectedListener(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_setup_activity_container, setUpLectureTimeFragment)
                        .commit();
                break;

            case 4:
                SetUpTimetableFragment setUpTimetableFragment = new SetUpTimetableFragment();
                addAnimationsToFragment(setUpTimetableFragment);
                setUpTimetableFragment.setOnTimeTableSelectedListener(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_setup_activity_container, setUpTimetableFragment)
                        .commit();
                break;
        }
    }

    private void addAnimationsToFragment(Fragment fragment) {
        fragment.setEnterTransition(new Slide(Gravity.RIGHT));
        fragment.setExitTransition(new Slide(Gravity.LEFT));
        fragment.setReturnTransition(new Slide(Gravity.LEFT));
        fragment.setReenterTransition(new Slide(Gravity.RIGHT));
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

    @Override
    public void onTimetableSelected() {
        setUpViewModel.saveHolidaysAndInitAttendance();
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent, bundle);
        setUpViewModel.setFirstRun(false);
        finish();
    }

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

    @Override
    public void onPreviousClickedInSetUpTimetable() {
        setUpViewModel.setCurrentStep(3);
        executeSetUpStep(setUpViewModel.getCurrentStep());
    }
}
