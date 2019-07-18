package com.vyas.pranav.studentcompanion.ui.activities;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
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
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.holder.StringHolder;
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
import com.vyas.pranav.studentcompanion.utils.NavigationDrawerUtil;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.viewmodels.MainViewModel;

import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationDrawerUtil.OnNavigationItemClickedListener {

    private static final String TAG = "MainActivity";
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
            mainViewModel.setCurrentFragmentId(getIntent().getIntExtra(Constants.EXTRA_MAIN_ACT_OPEN_OVERALL, NavigationDrawerUtil.ID_TODAY_ATTENDANCE));
            OnNavigationItemClicked(getIntent().getIntExtra(Constants.EXTRA_MAIN_ACT_OPEN_OVERALL, NavigationDrawerUtil.ID_TODAY_ATTENDANCE));
        }
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
                tvTitle.setText(R.string.title_home);
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
                tvTitle.setText(R.string.navigation_home);
                AttendanceIndividualFragment attendanceFragment = new AttendanceIndividualFragment();
                swapFragment(attendanceFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_OVERALL_ATTENDANCE:
                tvTitle.setText(R.string.navigation_overall_attendance);

                OverallAttendanceFragment overallAttendanceFragment = new OverallAttendanceFragment();
                swapFragment(overallAttendanceFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_SETTINGS:
                tvTitle.setText(R.string.navigation_settings);
                AppSettingsFragment appSettingsFragment = new AppSettingsFragment();
                swapFragment(appSettingsFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_NOTIFICATIONS:
                tvTitle.setText(R.string.navigation_notifications);
                NotificationFragment notificationFragment = new NotificationFragment();
                swapFragment(notificationFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_MY_PROFILE:
                MyProfileFragment myProfileFragment = new MyProfileFragment();
                tvTitle.setText(R.string.navigation_my_profile);
                swapFragment(myProfileFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_ABOUT_APP:
                AboutThisAppFragment aboutThisAppFragment = new AboutThisAppFragment();
                tvTitle.setText(R.string.navigation_about_this_app);
                swapFragment(aboutThisAppFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_ABOUT_DEVELOPER:
                AboutDeveloperFragment aboutDeveloperFragment = new AboutDeveloperFragment();
                tvTitle.setText(R.string.navigation_about_developer);
                swapFragment(aboutDeveloperFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;


            case NavigationDrawerUtil.ID_RESOURCES:
                ResourcesFragment resourcesFragment = new ResourcesFragment();
                tvTitle.setText(R.string.navigation_resources);
                swapFragment(resourcesFragment);
                mainViewModel.setCurrentFragmentId(identifier);
                break;

            case NavigationDrawerUtil.ID_SIGN_OUT:
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

            case NavigationDrawerUtil.ID_DELETE_ACCOUNT:
                BottomSheetDialog mDeleteSheet = new BottomSheetDialog(this);
                mDeleteSheet.setContentView(R.layout.item_holder_bottom_sheet_delete_account);
                mDeleteSheet.show();
                Button btnDelete = mDeleteSheet.findViewById(R.id.btn_holder_bottom_sheet_delete_ok);
                Button btnCancel = mDeleteSheet.findViewById(R.id.btn_holder_bottom_sheet_delete_cancel);
                Button btnSignOut = mDeleteSheet.findViewById(R.id.btn_holder_bottom_sheet_delete_sign_out);

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                                ((ActivityManager) MainActivity.this.getSystemService(Context.ACTIVITY_SERVICE)).clearApplicationUserData();
                                MainActivity.this.finish();
                            }
                        });
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDeleteSheet.dismiss();
                    }
                });

                btnSignOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        signOutUser();
                        mDeleteSheet.dismiss();
                    }
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
            mDrawer.updateBadge(NavigationDrawerUtil.ID_NOTIFICATIONS, new StringHolder(integer.toString()));
        });
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
}
