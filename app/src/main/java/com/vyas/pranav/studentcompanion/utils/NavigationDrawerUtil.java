package com.vyas.pranav.studentcompanion.utils;

import android.app.Activity;
import android.net.Uri;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.vyas.pranav.studentcompanion.R;

import androidx.appcompat.widget.Toolbar;

public class NavigationDrawerUtil {

    public static final int ID_TODAY_ATTENDANCE = 1;
    public static final int ID_OVERALL_ATTENDANCE = 2;
    public static final int ID_HOLIDAYS = 3;
    public static final int ID_TIMETABLE = 4;
    public static final int ID_SETTINGS = 5;
    public static final int ID_MARKET_PLACE = 6;
    public static final int ID_MY_PROFILE = 7;
    public static final int ID_ABOUT_APP = 8;
    public static final int ID_ABOUT_DEVELOPER = 9;
    public static final int ID_SIGN_OUT = 10;
    public static final int ID_DELETE_ACCOUNT = 11;

    public static Drawer getMaterialDrawer(Activity context, Toolbar toolbar, FirebaseUser currUser) {

        String userName = "ANONYMOUS", email = "ANONYMOUS";
        Uri photoUri = null;
        final OnNavigationItemClickedListener mCallback = (OnNavigationItemClickedListener) context;
        if (currUser != null) {
            userName = currUser.getDisplayName();
            email = currUser.getEmail();
            photoUri = currUser.getPhotoUrl();
        }
        PrimaryDrawerItem todayAttendance = new PrimaryDrawerItem()
                .withIdentifier(ID_TODAY_ATTENDANCE)
                .withName("Today's Attendance")
                .withIcon(R.drawable.ic_today_attendance)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_TODAY_ATTENDANCE);
                        return false;
                    }
                });
        PrimaryDrawerItem overallAttendance = new PrimaryDrawerItem()
                .withIdentifier(ID_OVERALL_ATTENDANCE)
                .withName("Overall Attendance")
                .withIcon(R.drawable.ic_overall_attendance)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_OVERALL_ATTENDANCE);
                        return false;
                    }
                });
        PrimaryDrawerItem holidays = new PrimaryDrawerItem()
                .withIdentifier(ID_HOLIDAYS)
                .withName("Holidays")
                .withIcon(R.drawable.ic_holidays)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_HOLIDAYS);
                        return false;
                    }
                });
        PrimaryDrawerItem timetable = new PrimaryDrawerItem()
                .withIdentifier(ID_TIMETABLE)
                .withName("Timetable")
                .withIcon(R.drawable.ic_timetable)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_TIMETABLE);
                        return false;
                    }
                });
        PrimaryDrawerItem settings = new PrimaryDrawerItem()
                .withIdentifier(ID_SETTINGS)
                .withName("Settings")
                .withIcon(R.drawable.ic_settings)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_SETTINGS);
                        return false;
                    }
                });
        PrimaryDrawerItem marketplace = new PrimaryDrawerItem()
                .withIdentifier(ID_MARKET_PLACE)
                .withName("Buy/Sell Items")
                .withIcon(R.drawable.ic_market_place)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_MARKET_PLACE);
                        return false;
                    }
                });
        PrimaryDrawerItem myProfile = new PrimaryDrawerItem()
                .withIdentifier(ID_MY_PROFILE)
                .withName("My Profile")
                .withIcon(R.drawable.ic_profile)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_MY_PROFILE);
                        return false;
                    }
                });
        PrimaryDrawerItem aboutApp = new PrimaryDrawerItem()
                .withIdentifier(ID_ABOUT_APP)
                .withName("About this app")
                .withIcon(R.mipmap.ic_launcher_round)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_ABOUT_APP);
                        return false;
                    }
                });
        PrimaryDrawerItem aboutDeveloper = new PrimaryDrawerItem()
                .withIdentifier(ID_ABOUT_DEVELOPER)
                .withName("About Developer")
                .withIcon(R.drawable.ic_developer)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_ABOUT_DEVELOPER);
                        return false;
                    }
                });
        PrimaryDrawerItem signOut = new PrimaryDrawerItem()
                .withIdentifier(ID_SIGN_OUT)
                .withName("Sign Out")
                .withIcon(R.drawable.ic_log_in_out)
                .withSelectable(false)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_SIGN_OUT);
                        return false;
                    }
                });
        PrimaryDrawerItem deleteAccount = new PrimaryDrawerItem()
                .withIdentifier(ID_DELETE_ACCOUNT)
                .withName("Delete Account")
                .withIcon(R.drawable.ic_delete)
                .withSelectable(false)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_DELETE_ACCOUNT);
                        return false;
                    }
                });

        ProfileDrawerItem profile = new ProfileDrawerItem();
        profile.withName(userName);
        profile.withEmail(email);
        if (photoUri != null) {
            profile.withIcon(photoUri);
        } else {
            profile.withIcon(context.getResources().getDrawable(R.drawable.ic_profile));
        }

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(context)
                .withHeaderBackground(R.drawable.account_background_new)
                .withSelectionListEnabledForSingleProfile(false)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        mCallback.OnNavigationItemClicked(ID_MY_PROFILE);
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

        Drawer drawer = new DrawerBuilder()
                .withActivity(context)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        todayAttendance,
                        overallAttendance,
                        holidays,
                        timetable,
                        marketplace,
                        myProfile,
                        settings,
                        aboutApp,
                        aboutDeveloper,
                        signOut,
                        deleteAccount
                ).build();
        return drawer;
    }

    public interface OnNavigationItemClickedListener {
        void OnNavigationItemClicked(int identifier);
    }
}
