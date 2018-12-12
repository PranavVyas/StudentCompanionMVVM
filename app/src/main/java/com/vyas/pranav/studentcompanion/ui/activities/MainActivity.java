package com.vyas.pranav.studentcompanion.ui.activities;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.mikepenz.materialdrawer.Drawer;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.fragments.AttendanceIndividualFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.HolidayFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.OverallAttendanceFragment;
import com.vyas.pranav.studentcompanion.utils.NavigationDrawerUtil;
import com.vyas.pranav.studentcompanion.viewmodels.AttendanceIndividualViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationDrawerUtil.OnNavigationItemClickedListener {

    @BindView(R.id.toolbar_main_activity)
    Toolbar toolbarMainActivity;
    @BindView(R.id.frame_main_activity_container)
    FrameLayout frameFragmentContainer;

    private Drawer mDrawer;
    private AttendanceIndividualViewModel attendanceIndividualViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarMainActivity);
        mDrawer = NavigationDrawerUtil.getMaterialDrawer(MainActivity.this, toolbarMainActivity);
        attendanceIndividualViewModel = ViewModelProviders.of(this).get(AttendanceIndividualViewModel.class);
        OnNavigationItemClicked(attendanceIndividualViewModel.getCurrentFragmentId());
        mDrawer.setSelection(attendanceIndividualViewModel.getCurrentFragmentId());
    }

    @Override
    public void OnNavigationItemClicked(int identifier) {
        switch (identifier) {
            case NavigationDrawerUtil.ID_TODAY_ATTENDANCE:
                AttendanceIndividualFragment attendanceFragment = new AttendanceIndividualFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_main_activity_container, attendanceFragment)
                        .commit();
                attendanceIndividualViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_OVERALL_ATTENDANCE:
                OverallAttendanceFragment overallAttendanceFragment = new OverallAttendanceFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_main_activity_container, overallAttendanceFragment)
                        .commit();
                attendanceIndividualViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_HOLIDAYS:
                HolidayFragment holidayFragment = new HolidayFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_main_activity_container, holidayFragment)
                        .commit();
                attendanceIndividualViewModel.setCurrentFragmentId(identifier);
                break;
        }
    }
}
