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

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.fragments.AboutDeveloperFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.AboutThisAppFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.AppSettingsFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.AttendanceIndividualFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.MyProfileFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.NotificationFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.OverallAttendanceFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.ResourcesFragment;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.viewmodels.MainViewModel;

import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AppSettingsFragment.OnDatabaseImportedListener {

    public static final int ID_TODAY_ATTENDANCE = 1;
    public static final int ID_OVERALL_ATTENDANCE = 2;
    public static final int ID_RESOURCES = 3;
    public static final int ID_NOTIFICATIONS = 4;
    public static final int ID_MY_PROFILE = 5;
    public static final int ID_ABOUT_APP = 6;
    public static final int ID_SHARE_APP = 7;
    public static final int ID_ABOUT_DEVELOPER = 8;
    public static final int ID_TIPS_TRICKS = 9;
    public static final int ID_SETTINGS = 10;
    public static final int ID_SIGN_OUT = 11;
    public static final int ID_DELETE_ACCOUNT = 12;
    private static final String TAG = "MainActivity";
    private static final long ID_REPORT_BUG = 13;
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
        SharedPreferencesUtils.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarMainActivity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
//        actionBar = getSupportActionBar();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        if (getIntent().hasExtra(Constants.EXTRA_MAIN_ACT_OPEN_OVERALL)) {
            mainViewModel.setCurrentFragmentId(getIntent().getIntExtra(Constants.EXTRA_MAIN_ACT_OPEN_OVERALL, ID_TODAY_ATTENDANCE));
            OnNavigationItemClicked(getIntent().getIntExtra(Constants.EXTRA_MAIN_ACT_OPEN_OVERALL, ID_TODAY_ATTENDANCE));
        }
        FirebaseUser currUser = mainViewModel.getCurrUser();
        mDrawer = getMaterialDrawer(currUser);
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
            } else if (mainViewModel.getCurrentFragmentId() != ID_TODAY_ATTENDANCE) {
                mainViewModel.setCurrentFragmentId(ID_TODAY_ATTENDANCE);
                AttendanceIndividualFragment attendanceFragment = AttendanceIndividualFragment.newInstance(null);
                swapFragment(attendanceFragment);
                tvTitle.setText(R.string.title_home);
                mDrawer.setSelection(ID_TODAY_ATTENDANCE);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void OnNavigationItemClicked(int identifier) {
        switch (identifier) {
            case ID_TODAY_ATTENDANCE:
                tvTitle.setText(R.string.navigation_home);
                AttendanceIndividualFragment attendanceFragment = AttendanceIndividualFragment.newInstance(null);
                swapFragment(attendanceFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case ID_OVERALL_ATTENDANCE:
                tvTitle.setText(R.string.navigation_overall_attendance);
                OverallAttendanceFragment overallAttendanceFragment = OverallAttendanceFragment.newInstance();
                swapFragment(overallAttendanceFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case ID_SETTINGS:
                tvTitle.setText(R.string.navigation_settings);
                AppSettingsFragment appSettingsFragment = new AppSettingsFragment();
                swapFragment(appSettingsFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case ID_NOTIFICATIONS:
                tvTitle.setText(R.string.navigation_notifications);
                NotificationFragment notificationFragment = NotificationFragment.newInstance();
                swapFragment(notificationFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case ID_MY_PROFILE:
                MyProfileFragment myProfileFragment = MyProfileFragment.newInstance();
                tvTitle.setText(R.string.navigation_my_profile);
                swapFragment(myProfileFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case ID_ABOUT_APP:
                AboutThisAppFragment aboutThisAppFragment = AboutThisAppFragment.newInstance();
                tvTitle.setText(R.string.navigation_about_this_app);
                swapFragment(aboutThisAppFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case ID_ABOUT_DEVELOPER:
                AboutDeveloperFragment aboutDeveloperFragment = AboutDeveloperFragment.newInstance();
                tvTitle.setText(R.string.navigation_about_developer);
                swapFragment(aboutDeveloperFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;


            case ID_RESOURCES:
                ResourcesFragment resourcesFragment = ResourcesFragment.newInstance();
                tvTitle.setText(R.string.navigation_resources);
                swapFragment(resourcesFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case ID_SIGN_OUT:
                BottomSheetDialog mSignOutSheet = new BottomSheetDialog(this);
                mSignOutSheet.setContentView(R.layout.item_holder_bottom_sheet_sign_out);
                mSignOutSheet.show();
                Button signOutBtn = mSignOutSheet.findViewById(R.id.btn_holder_bottom_sheet_ok);
                Button cancel = mSignOutSheet.findViewById(R.id.btn_holder_bottom_sheet_cancel);
                signOutBtn.setOnClickListener(view -> {
                    signOutUser();
                    mSignOutSheet.dismiss();
                });
                cancel.setOnClickListener(view -> mSignOutSheet.dismiss());
                break;

            case ID_DELETE_ACCOUNT:
                BottomSheetDialog mDeleteSheet = new BottomSheetDialog(this);
                mDeleteSheet.setContentView(R.layout.item_holder_bottom_sheet_delete_account);
                mDeleteSheet.show();
                Button btnDelete = mDeleteSheet.findViewById(R.id.btn_holder_bottom_sheet_delete_ok);
                Button btnCancel = mDeleteSheet.findViewById(R.id.btn_holder_bottom_sheet_delete_cancel);
                Button btnSignOut = mDeleteSheet.findViewById(R.id.btn_holder_bottom_sheet_delete_sign_out);

                btnDelete.setOnClickListener(view -> {
                    Toast.makeText(MainActivity.this, "Deleting Account...", Toast.LENGTH_SHORT).show();
                    mDeleteSheet.dismiss();
                    AuthUI.getInstance().delete(MainActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mainViewModel.setCurrUser(null);
                            Toast.makeText(MainActivity.this, "Account Successfully Deleted...\nRedirecting to sign In Page...", Toast.LENGTH_SHORT).show();
                            Intent startSignInActivity = new Intent(MainActivity.this, SignInActivity.class);
                            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                            startActivity(startSignInActivity);
                            ((ActivityManager) MainActivity.this.getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
                            MainActivity.this.finish();
                        }
                    });
                });

                btnCancel.setOnClickListener(view -> mDeleteSheet.dismiss());

                btnSignOut.setOnClickListener(view -> {
                    signOutUser();
                    mDeleteSheet.dismiss();
                });
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
        fragment.setEnterTransition(new Slide(Gravity.END));
        fragment.setExitTransition(new Slide(Gravity.START));
        fragment.setReturnTransition(new Slide(Gravity.START));
        fragment.setReenterTransition(new Slide(Gravity.END));
        fragment.setAllowEnterTransitionOverlap(false);
        fragment.setAllowReturnTransitionOverlap(false);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_main_activity_container, fragment)
                .commit();
    }

    private void dismissCallingNotificationIsAvailable() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.cancelAll();
    }

    public void setLiveBadge() {
        LiveData<Integer> notificationCount = mainViewModel.getNotificationCount(new Date());
        notificationCount.observe(this, integer -> {
            mDrawer.updateBadge(ID_NOTIFICATIONS, new StringHolder(integer.toString()));
        });
    }

    @Override
    public void databaseImported() {
        Toast.makeText(this, "Activity recreated", Toast.LENGTH_SHORT).show();
        recreate();
    }

//    private void refreshOverallAttendance(){
//        mainViewModel.getAllOverallAttendance().observe(this, new Observer<List<OverallAttendanceEntry>>() {
//            @Override
//            public void onChanged(List<OverallAttendanceEntry> overallAttendanceEntries) {
//                for (OverallAttendanceEntry x :
//                        overallAttendanceEntries) {
//                    AttendanceUtils.checkForSmartCards(x,MainActivity.this);
//                }
//            }
//        });
//    }


    public Drawer getMaterialDrawer(FirebaseUser currUser) {

        String userName = "ANONYMOUS", email = "ANONYMOUS";
        Uri photoUri = null;
        if (currUser != null) {
            userName = currUser.getDisplayName();
            email = currUser.getEmail();
            photoUri = currUser.getPhotoUrl();
        }
        PrimaryDrawerItem todayAttendance = new PrimaryDrawerItem()
                .withIdentifier(ID_TODAY_ATTENDANCE)
                .withName(R.string.navigation_home)
                .withIconTintingEnabled(true)
                .withIcon(R.drawable.ic_calendar_material)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    OnNavigationItemClicked(ID_TODAY_ATTENDANCE);
                    return false;
                });
        PrimaryDrawerItem overallAttendance = new PrimaryDrawerItem()
                .withIdentifier(ID_OVERALL_ATTENDANCE)
                .withName(R.string.navigation_overall_attendance)
                .withIconTintingEnabled(true)
                .withIcon(R.drawable.ic_chart_filled_material)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    OnNavigationItemClicked(ID_OVERALL_ATTENDANCE);
                    return false;
                });
        PrimaryDrawerItem settings = new PrimaryDrawerItem()
                .withIdentifier(ID_SETTINGS)
                .withName(R.string.navigation_settings)
                .withIconTintingEnabled(true)
                .withIcon(R.drawable.ic_settings_filled_material)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    OnNavigationItemClicked(ID_SETTINGS);
                    return false;
                });
        PrimaryDrawerItem notifications = new PrimaryDrawerItem()
                .withIdentifier(ID_NOTIFICATIONS)
                .withName(R.string.navigation_notifications)
                .withIcon(R.drawable.ic_notification_filled_material)
                .withBadge("0")
                .withIconTintingEnabled(true)
                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    OnNavigationItemClicked(ID_NOTIFICATIONS);
                    return false;
                });
        PrimaryDrawerItem myProfile = new PrimaryDrawerItem()
                .withIdentifier(ID_MY_PROFILE)
                .withName(R.string.navigation_my_profile)
                .withIconTintingEnabled(true)
                .withIcon(R.drawable.ic_account_filled_material)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    OnNavigationItemClicked(ID_MY_PROFILE);
                    return false;
                });
        PrimaryDrawerItem shareApp = new PrimaryDrawerItem()
                .withIdentifier(ID_SHARE_APP)
                .withName(R.string.navigation_share_app)
                .withIconTintingEnabled(true)
                .withIcon(R.drawable.ic_share_filled_material)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message) + getString(R.string.feature_url) + "\n\n Download now: " + getString(R.string.download_url));
                    shareIntent.setType("text/plain");
                    startActivity(shareIntent);
                    return false;
                });
        PrimaryDrawerItem aboutApp = new PrimaryDrawerItem()
                .withIdentifier(ID_ABOUT_APP)
                .withName(R.string.navigation_about_this_app)
                .withIconTintingEnabled(true)
                .withIcon(R.drawable.ic_information_filled_material)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    OnNavigationItemClicked(ID_ABOUT_APP);
                    return false;
                });
        PrimaryDrawerItem aboutDeveloper = new PrimaryDrawerItem()
                .withIdentifier(ID_ABOUT_DEVELOPER)
                .withName(R.string.navigation_about_developer)
                .withIconTintingEnabled(true)
                .withIcon(R.drawable.ic_developer_braces_material)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    OnNavigationItemClicked(ID_ABOUT_DEVELOPER);
                    return false;
                });
        PrimaryDrawerItem signOut = new PrimaryDrawerItem()
                .withIdentifier(ID_SIGN_OUT)
                .withName(R.string.navigation_sign_out)
                .withIcon(R.drawable.ic_logout_filled_material)
                .withSelectable(false).withIconTintingEnabled(true)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    OnNavigationItemClicked(ID_SIGN_OUT);
                    return false;
                });
        PrimaryDrawerItem deleteAccount = new PrimaryDrawerItem()
                .withIdentifier(ID_DELETE_ACCOUNT)
                .withName(R.string.navigation_delete_account)
                .withIcon(R.drawable.ic_delete_filled_material)
                .withSelectable(false)
                .withIconTintingEnabled(true)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    OnNavigationItemClicked(ID_DELETE_ACCOUNT);
                    return false;
                });
        PrimaryDrawerItem tipsTricks = new PrimaryDrawerItem()
                .withIdentifier(ID_TIPS_TRICKS)
                .withName(R.string.navigation_tips_tricks)
                .withIcon(R.drawable.ic_tips_tricks_filled_material)
                .withSelectable(false)
                .withIconTintingEnabled(true)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(getString(R.string.tips_tricks_url)));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Install any browser Application", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                });

        PrimaryDrawerItem reportBug = new PrimaryDrawerItem()
                .withIdentifier(ID_REPORT_BUG)
                .withName(R.string.navigation_report_bug)
                .withIcon(R.drawable.ic_bug_report)
                .withSelectable(false)
                .withIconTintingEnabled(true)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "pranavvyas4399@gmail.com", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Bug Report For Student Companion From <PUT YOUR NAME HERE>");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Hey Pranav,\n\nI found a bug in Student Companion \n<LIST OUT BUGS YOU FOUND>\n\n\nWhat I did was : \n<LIST HOW YOU ENCOUNTERED THE BUG>");
                    startActivity(Intent.createChooser(emailIntent, "Send email using ..."));
                    Toast.makeText(this, "Thank you for reporting bug, This will make this app even better!", Toast.LENGTH_SHORT).show();
                    return false;
                });
        ProfileDrawerItem profile = new ProfileDrawerItem();
        profile.withName(userName);
        profile.withEmail(email);
        if (photoUri != null) {
            profile.withIcon(photoUri);
        } else {
            profile.withIcon(getResources().getDrawable(R.drawable.ic_account_filled_material));
        }
////
        PrimaryDrawerItem resources = new PrimaryDrawerItem()
                .withIdentifier(ID_RESOURCES)
                .withName(R.string.navigation_resources)
                .withIconTintingEnabled(true)
                .withIcon(R.drawable.ic_resources_filled_material)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    OnNavigationItemClicked(ID_RESOURCES);
                    return false;
                });

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.shape_gradient_account)
                .withSelectionListEnabledForSingleProfile(false)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        OnNavigationItemClicked(ID_MY_PROFILE);
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .addProfiles(
                        profile
                ).build();

        //noinspection UnnecessaryLocalVariable
        Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbarMainActivity)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        new SectionDrawerItem().withName("Features & Tools"),
                        todayAttendance,
                        overallAttendance,
                        resources,
                        notifications,
                        new SectionDrawerItem().withName("Information"),
                        myProfile,
                        aboutApp,
                        shareApp,
                        aboutDeveloper,
                        tipsTricks,
                        reportBug,
                        new SectionDrawerItem().withName("Settings & Other"),
                        settings,
                        signOut,
                        deleteAccount
                ).build();
        return drawer;
    }

}
