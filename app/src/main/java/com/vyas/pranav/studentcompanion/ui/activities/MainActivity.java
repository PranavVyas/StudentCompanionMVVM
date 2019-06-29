package com.vyas.pranav.studentcompanion.ui.activities;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.Drawer;
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
import com.vyas.pranav.studentcompanion.utils.NavigationDrawerUtil;
import com.vyas.pranav.studentcompanion.viewmodels.MainViewModel;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesRepository.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarMainActivity);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        actionBar = getSupportActionBar();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        FirebaseUser currUser = mainViewModel.getCurrUser();
        mDrawer = NavigationDrawerUtil.getMaterialDrawer(MainActivity.this, toolbarMainActivity, currUser);
        OnNavigationItemClicked(mainViewModel.getCurrentFragmentId());
        mDrawer.setSelection(mainViewModel.getCurrentFragmentId());
//        setLiveBadge();
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.d("Received Path is : " + this.getExternalFilesDir(null).getPath() + "\n abs path : " + this.getExternalFilesDir(null).getAbsolutePath());
        dismissCallingNotificationIsAvailable();
//        showInstruction();
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
                new MaterialAlertDialogBuilder(MainActivity.this)
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
                new MaterialAlertDialogBuilder(MainActivity.this)
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
                                        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                                        startActivity(startSignInActivity, bundle);
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
        fragment.setEnterTransition(new Slide(Gravity.RIGHT));
        fragment.setExitTransition(new Slide(Gravity.LEFT));
        fragment.setReturnTransition(new Slide(Gravity.LEFT));
        fragment.setReenterTransition(new Slide(Gravity.RIGHT));
        fragment.setAllowEnterTransitionOverlap(false);
        fragment.setAllowReturnTransitionOverlap(false);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frame_main_activity_container, fragment)
                .commit();
    }

    private void dismissCallingNotificationIsAvailable() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.cancelAll();
    }

//    public void setLiveBadge() {
//        LiveData<Integer> notificationCount = mainViewModel.getNotificationCount(ConverterUtils.convertDateToString(new Date()));
////        notificationCount.observe(this, new Observer<Integer>() {
////            @Override
////            public void onChanged(Integer integer) {
//////                PrimaryDrawerItem notificationItem = ((PrimaryDrawerItem) mDrawer.getDrawerItem(NavigationDrawerUtil.ID_NOTIFICATIONS));
//////                notificationItem.withBadge(integer);
////                mDrawer.updateBadge(NavigationDrawerUtil.ID_NOTIFICATIONS, new StringHolder(integer.toString()));
//////                mDrawer.updateItem(notificationItem);
////                TODO implement badge style here notificationItem.withBadgeStyle(new BadgeStyle());
////            }
//////        });
////        NotificationsViewModel notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
////        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(this);
////        notificationsViewModel.getAllNotifications().observe(this, new Observer<List<NotificationEntry>>() {
////            @Override
////            public void onChanged(List<NotificationEntry> notificationEntries) {
////                int currentNotis = notificationEntries.size();
////                int previousNotis = sharedPreferencesUtils.getCurrentNotis();
//////                PrimaryDrawerItem notificationItem = ((PrimaryDrawerItem) mDrawer.getDrawerItem(NavigationDrawerUtil.ID_NOTIFICATIONS));
//////                notificationItem.withBadge(currentNotis - previousNotis);
////                mDrawer.updateBadge(NavigationDrawerUtil.ID_NOTIFICATIONS, new StringHolder((String.valueOf(currentNotis-previousNotis))));
//////                mDrawer.updateItem(notificationItem);
////            }
////        });
//    }

    private void showInstruction() {
////        if(mainViewModel.isInstructionDone()){
////            return;
////        }
//        // We load a drawable and create a location to show a tap target here
//        // We need the display to get the width and height at this point in time
//        final Display display = getWindowManager().getDefaultDisplay();
//        // Load our little droid guy
//        final Drawable droid = ContextCompat.getDrawable(this, R.drawable.ic_info_black);
//        // Tell our droid buddy where we want him to appear
//        final Rect droidTarget = new Rect(0, 0, droid.getIntrinsicWidth() * 2, droid.getIntrinsicHeight() * 2);
//        // Using deprecated methods makes you look way cool
//        droidTarget.offset(display.getWidth() / 2, display.getHeight() / 2);
//        TapTarget start = TapTarget.forBounds(droidTarget, "Welcome Aboard!", "Welcome to the tutorial\n● In this you will be introduced to the common features of the app that will be useful!\n\n\n● So let's go!!!")
//                .cancelable(false)
//                .icon(droid)
//                .id(1);
//        TapTarget menuOpenerTarget = TapTarget.forToolbarNavigationIcon(toolbarMainActivity, "Navigation Drawer", "This is navigation drawer, It is the most useful menu that is the gateway to all the features")
//                .cancelable(false)
//                .icon(getDrawable(android.R.drawable.ic_menu_camera))
//                .id(2);
//        TapTarget sas = TapTarget.forView()
//
//        TapTargetSequence targetSequence = new TapTargetSequence(this)
//                .targets(
//                        ,
//                ).listener(new TapTargetSequence.Listener() {
//                    @Override
//                    public void onSequenceFinish() {
//                        Toast.makeText(MainActivity.this, "Finished Tutorial", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
//                        if (lastTarget.id() == 1) {
//                            Toast.makeText(MainActivity.this, "Clicked First Step", Toast.LENGTH_SHORT).show();
//                        }
//                        if (lastTarget.id() == 2) {
//                            Toast.makeText(MainActivity.this, "Clicked 2nd Step", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onSequenceCanceled(TapTarget lastTarget) {
//
//                    }
//                });
//        targetSequence.start();
    }
}
