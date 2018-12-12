package com.vyas.pranav.studentcompanion.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.fragments.SetUpDatesFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.SetUpDetailsSemFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.SetUpTimetableFragment;
import com.vyas.pranav.studentcompanion.viewmodels.SetUpViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SetUpActivity extends AppCompatActivity implements SetUpDatesFragment.OnDatesSetUpListener, SetUpDetailsSemFragment.OnSubjectsSelectedListener, SetUpTimetableFragment.OnTimetableSelectedListener {

    @BindView(R.id.toolbar_setup_activity)
    Toolbar toolbar;

    private SetUpViewModel setUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setUpViewModel = ViewModelProviders.of(this).get(SetUpViewModel.class);
        if (!setUpViewModel.isFirstRun()) {
            Intent openMainActivity = new Intent(this, MainActivity.class);
            startActivity(openMainActivity);
            finish();
            return;
        }
        executeSetUpStep(setUpViewModel.getCurrentStep());
    }


    @Override
    public void onDatesSetUp() {
        setUpViewModel.setCurrentStep(2);
        executeSetUpStep(setUpViewModel.getCurrentStep());
    }

    public void executeSetUpStep(int step) {
        switch (step) {
            case 1:
                SetUpDatesFragment setUpDatesFragment = new SetUpDatesFragment();
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
                SetUpTimetableFragment setUpTimetableFragment = new SetUpTimetableFragment();
                setUpTimetableFragment.setOnTimeTableSelectedListener(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_setup_activity_container, setUpTimetableFragment)
                        .commit();
                break;
        }
    }

    @Override
    public void onSubjectSelected() {
        setUpViewModel.setCurrentStep(3);
        executeSetUpStep(setUpViewModel.getCurrentStep());
    }

    @Override
    public void onPreviousClickedOnSemSetUp() {
        setUpViewModel.setCurrentStep(1);
        executeSetUpStep(setUpViewModel.getCurrentStep());
    }

    @Override
    public void onTimetableSelected() {
        setUpViewModel.saveHolidaysAndInitAttendance();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        setUpViewModel.setFirstRun(false);
        finish();
        Toast.makeText(this, "Timetable successfully done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPreviousClickedInSetUpTimetable() {
        setUpViewModel.setCurrentStep(2);
        executeSetUpStep(setUpViewModel.getCurrentStep());
    }
}
