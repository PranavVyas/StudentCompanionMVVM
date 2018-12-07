package com.vyas.pranav.studentcompanion.ui.activities;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.mikepenz.materialdrawer.Drawer;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.fragments.AttendanceIndividualFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.OverallAttendanceFragment;
import com.vyas.pranav.studentcompanion.utils.NavigationDrawerUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationDrawerUtil.OnNavigationItemClickedListener {

    @BindView(R.id.toolbar_main_activity)
    Toolbar toolbarMainActivity;
    @BindView(R.id.frame_main_activity_container)
    FrameLayout frameFragmentContainer;

    Drawer mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarMainActivity);
        mDrawer = NavigationDrawerUtil.getMaterialDrawer(MainActivity.this, toolbarMainActivity);
        AttendanceIndividualFragment attendanceFragment = new AttendanceIndividualFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_main_activity_container, attendanceFragment)
                .commit();
    }

    @Override
    public void OnNavigationItemClicked(int identifier) {
        switch (identifier) {
            case NavigationDrawerUtil.ID_TODAY_ATTENDACE:
                AttendanceIndividualFragment attendanceFragment = new AttendanceIndividualFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_main_activity_container, attendanceFragment)
                        .commit();
                break;

            case NavigationDrawerUtil.ID_OVERALL_ATTENDANCE:
                OverallAttendanceFragment overallAttendanceFragment = new OverallAttendanceFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_main_activity_container, overallAttendanceFragment)
                        .commit();
                break;
        }
    }
}
