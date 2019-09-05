package com.vyas.pranav.studentcompanion.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.vyas.pranav.studentcompanion.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils.setUserTheme;

public class DeveloperActivity extends AppCompatActivity {

    @BindView(R.id.item_developer_bulk_attendance)
    ConstraintLayout bulkAttendance;
    @BindView(R.id.toolbar_developer)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserTheme(this);
        setContentView(R.layout.activity_developer);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @OnClick(R.id.item_developer_bulk_attendance)
    void clickedBulkAttendance() {
        Intent intent = new Intent(this, DeveloperAttendanceActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.item_developer_edit_timetable)
    void clickedEditTimetable() {
        Intent intent = new Intent(this, DeveloperTimetableActivity.class);
        startActivity(intent);
    }
}
