package com.vyas.pranav.studentcompanion.ui.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.repositories.SharedPreferencesRepository;
import com.vyas.pranav.studentcompanion.ui.fragments.AboutDeveloperFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.AboutThisAppFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.AppSettingsFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.AttendanceIndividualFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.MyProfileFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.NotificationFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.OverallAttendanceFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.ResourcesFragment;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.NavigationDrawerUtil;
import com.vyas.pranav.studentcompanion.viewmodels.MainViewModel;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationDrawerUtil.OnNavigationItemClickedListener {

    @BindView(R.id.toolbar_main_activity)
    Toolbar toolbarMainActivity;
    @BindView(R.id.frame_main_activity_container)
    FrameLayout frameFragmentContainer;
    @BindView(R.id.tv_toolbar_main)
    TextView tvTitle;

    private Drawer mDrawer;
    private MainViewModel mainViewModel;
    private FragmentManager fragManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesRepository.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarMainActivity);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        actionBar = getSupportActionBar();
        fragManager = getSupportFragmentManager();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        FirebaseUser currUser = mainViewModel.getCurrUser();
        mDrawer = NavigationDrawerUtil.getMaterialDrawer(MainActivity.this, toolbarMainActivity, currUser);
        OnNavigationItemClicked(mainViewModel.getCurrentFragmentId());
        mDrawer.setSelection(mainViewModel.getCurrentFragmentId());
        setLiveBadge();
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.d("Received Path is : " + this.getExternalFilesDir(null).getPath() + "\n abs path : " + this.getExternalFilesDir(null).getAbsolutePath());
        dismissCallingNotificationIsAvailable();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer != null) {
            if (mDrawer.isDrawerOpen()) {
                mDrawer.closeDrawer();
            } else if (mainViewModel.getCurrentFragmentId() != NavigationDrawerUtil.ID_TODAY_ATTENDANCE) {
                mainViewModel.setCurrentFragmentId(NavigationDrawerUtil.ID_TODAY_ATTENDANCE);
                AttendanceIndividualFragment attendanceFragment = new AttendanceIndividualFragment();
                swapFragment(attendanceFragment);
                tvTitle.setText("Home");
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
                tvTitle.setText("Home");

                AttendanceIndividualFragment attendanceFragment = new AttendanceIndividualFragment();
                swapFragment(attendanceFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_OVERALL_ATTENDANCE:
                tvTitle.setText("Overall Attendance");

                OverallAttendanceFragment overallAttendanceFragment = new OverallAttendanceFragment();
                swapFragment(overallAttendanceFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;
//
//            case NavigationDrawerUtil.ID_HOLIDAYS:
////                actionBar.setTitle("Holidays");
//                tvTitle.setText("Holidays");
//                HolidayFragment holidayFragment = new HolidayFragment();
//                swapFragment(holidayFragment);
//                mainViewModel.setCurrentFragmentId(identifier);
//                break;
//
//            case NavigationDrawerUtil.ID_TIMETABLE:
////                actionBar.setTitle("Timetable");
//                tvTitle.setText("Timetable");
//                TimetableFragment timetableFragment = new TimetableFragment();
//                swapFragment(timetableFragment);
//                mainViewModel.setCurrentFragmentId(identifier);
//                break;
//
//            case NavigationDrawerUtil.ID_MARKET_PLACE:
////                actionBar.setTitle("Marketplace");
//                tvTitle.setText("Marketplace");
//                MarketPlaceFragment marketPlaceFragment = new MarketPlaceFragment();
//                swapFragment(marketPlaceFragment);
//                mainViewModel.setCurrentFragmentId(identifier);
//                break;

            case NavigationDrawerUtil.ID_SETTINGS:
                tvTitle.setText("Settings");
                AppSettingsFragment appSettingsFragment = new AppSettingsFragment();
                swapFragment(appSettingsFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_NOTIFICATIONS:
                tvTitle.setText("Notifications");
                NotificationFragment notificationFragment = new NotificationFragment();
                swapFragment(notificationFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_MY_PROFILE:
                MyProfileFragment myProfileFragment = new MyProfileFragment();
                tvTitle.setText("My Profile");
                swapFragment(myProfileFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_ABOUT_APP:
                AboutThisAppFragment aboutThisAppFragment = new AboutThisAppFragment();
                tvTitle.setText("About This App");
                swapFragment(aboutThisAppFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_ABOUT_DEVELOPER:
                AboutDeveloperFragment aboutDeveloperFragment = new AboutDeveloperFragment();
                tvTitle.setText("About Developer");
                swapFragment(aboutDeveloperFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;


            case NavigationDrawerUtil.ID_RESOURCES:
                ResourcesFragment resourcesFragment = new ResourcesFragment();
                tvTitle.setText("Resources");
                swapFragment(resourcesFragment);
                mainViewModel.setCurrentFragmentId(identifier);
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
                                        mainViewModel.setCurrUser(null);
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
                mainViewModel.setCurrUser(null);
                Intent startSignInActivity = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(startSignInActivity);
                mainViewModel.removePendingJobs();
                MainActivity.this.finish();
            }
        });
    }

    private void swapFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_activity_container, fragment)
                .commit();
    }

    private void dismissCallingNotificationIsAvailable() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.cancelAll();
    }

    public void setLiveBadge() {
        LiveData<Integer> notificationCount = mainViewModel.getNotificationCount(ConverterUtils.convertDateToString(new Date()));
        notificationCount.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
//                PrimaryDrawerItem notificationItem = ((PrimaryDrawerItem) mDrawer.getDrawerItem(NavigationDrawerUtil.ID_NOTIFICATIONS));
//                notificationItem.withBadge(integer);
                mDrawer.updateBadge(NavigationDrawerUtil.ID_NOTIFICATIONS, new StringHolder(integer.toString()));
//                mDrawer.updateItem(notificationItem);
                //TODO implement badge style here notificationItem.withBadgeStyle(new BadgeStyle());
            }
        });
    }
}
