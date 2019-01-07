package com.vyas.pranav.studentcompanion.ui.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.Drawer;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.repositories.SharedPreferencesRepository;
import com.vyas.pranav.studentcompanion.ui.fragments.AppSettingsFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.AttendanceIndividualFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.HolidayFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.MarketPlaceFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.MyProfileFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.OverallAttendanceFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.TimetableFragment;
import com.vyas.pranav.studentcompanion.utils.NavigationDrawerUtil;
import com.vyas.pranav.studentcompanion.viewmodels.AttendanceIndividualViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
    private FragmentManager fragManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesRepository.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarMainActivity);
        fragManager = getSupportFragmentManager();
        attendanceIndividualViewModel = ViewModelProviders.of(this).get(AttendanceIndividualViewModel.class);
        FirebaseUser currUser = attendanceIndividualViewModel.getCurrUser();
        mDrawer = NavigationDrawerUtil.getMaterialDrawer(MainActivity.this, toolbarMainActivity, currUser);
        OnNavigationItemClicked(attendanceIndividualViewModel.getCurrentFragmentId());
        mDrawer.setSelection(attendanceIndividualViewModel.getCurrentFragmentId());
    }

    @Override
    public void onBackPressed() {
        if (mDrawer != null) {
            if (mDrawer.isDrawerOpen()) {
                mDrawer.closeDrawer();
            } else if (attendanceIndividualViewModel.getCurrentFragmentId() != NavigationDrawerUtil.ID_TODAY_ATTENDANCE) {
                attendanceIndividualViewModel.setCurrentFragmentId(NavigationDrawerUtil.ID_TODAY_ATTENDANCE);
                AttendanceIndividualFragment attendanceFragment = new AttendanceIndividualFragment();
                swapFragment(attendanceFragment);
                mDrawer.setSelection(NavigationDrawerUtil.ID_TODAY_ATTENDANCE);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void OnNavigationItemClicked(int identifier) {
        switch (identifier) {
            case NavigationDrawerUtil.ID_TODAY_ATTENDANCE:
                AttendanceIndividualFragment attendanceFragment = new AttendanceIndividualFragment();
                swapFragment(attendanceFragment);
                attendanceIndividualViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_OVERALL_ATTENDANCE:
                OverallAttendanceFragment overallAttendanceFragment = new OverallAttendanceFragment();
                swapFragment(overallAttendanceFragment);
                attendanceIndividualViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_HOLIDAYS:
                HolidayFragment holidayFragment = new HolidayFragment();
                swapFragment(holidayFragment);
                attendanceIndividualViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_TIMETABLE:
                TimetableFragment timetableFragment = new TimetableFragment();
                swapFragment(timetableFragment);
                attendanceIndividualViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_MARKET_PLACE:
                MarketPlaceFragment marketPlaceFragment = new MarketPlaceFragment();
                swapFragment(marketPlaceFragment);
                attendanceIndividualViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_SETTINGS:
                AppSettingsFragment appSettingsFragment = new AppSettingsFragment();
                swapFragment(appSettingsFragment);
                attendanceIndividualViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_MY_PROFILE:
                MyProfileFragment myProfileFragment = new MyProfileFragment();
                swapFragment(myProfileFragment);
                attendanceIndividualViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_SIGN_OUT:
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Signing out will not remove any data of attendance\nClick ok to sign out\nYou will be redirected to the sign in page to sign in again")
                        .setTitle("Notice")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                signOutUser();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                break;

            case NavigationDrawerUtil.ID_DELETE_ACCOUNT:
                new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(false)
                        .setTitle("Please Read")
                        .setMessage("Deleting account will remove all the attendance data and account from server as well as locally\nPlease proceed by touching \"Delete Anyways\" Or Click On \"Cancel\" to cancel\nApplication will shut down after deleting account, You have to manually start again\nThank You!")
                        .setPositiveButton("Delete Anyways...", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Deleting Account...", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                AuthUI.getInstance().delete(MainActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        attendanceIndividualViewModel.setCurrUser(null);
                                        Toast.makeText(MainActivity.this, "Account Successfully Deleted...\nRedirecting to sign In Page...", Toast.LENGTH_SHORT).show();
                                        Intent startSignInActivity = new Intent(MainActivity.this, SignInActivity.class);
                                        startActivity(startSignInActivity);
                                        ((ActivityManager) MainActivity.this.getSystemService(Context.ACTIVITY_SERVICE)).clearApplicationUserData();
                                        MainActivity.this.finish();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNeutralButton("Sign out Instead", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                signOutUser();
                            }
                        })
                        .create()
                        .show();
        }
    }

    private void signOutUser() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                attendanceIndividualViewModel.setCurrUser(null);
                Intent startSignInActivity = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(startSignInActivity);
                MainActivity.this.finish();
            }
        });
    }

    private void swapFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_activity_container, fragment)
                .commit();
    }
}
