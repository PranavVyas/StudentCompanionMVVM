package com.vyas.pranav.studentcompanion.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.fragments.AttendanceIndividualFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceIndividualActivity extends AppCompatActivity {

    public static final String EXTRA_DATE = "AttendanceIndividual.EXTRA_DATE";
    @BindView(R.id.toolbar_attendance_individual)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_individual);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent receivedData = getIntent();
        if (receivedData != null) {
            if (receivedData.hasExtra(EXTRA_DATE)) {
                String dateStr = receivedData.getStringExtra(EXTRA_DATE);
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_DATE, dateStr);
                AttendanceIndividualFragment attendanceIndividualFragment = new AttendanceIndividualFragment();
                attendanceIndividualFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_attendance_individual_container, attendanceIndividualFragment)
                        .commit();
            }
        }

    }
}
