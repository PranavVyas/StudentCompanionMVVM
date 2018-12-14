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
import com.vyas.pranav.studentcompanion.R;

import androidx.appcompat.widget.Toolbar;

public class NavigationDrawerUtil {

    public static final int ID_TODAY_ATTENDANCE = 1;
    public static final int ID_OVERALL_ATTENDANCE = 2;
    public static final int ID_HOLIDAYS = 3;
    public static final int ID_TIMETABLE = 4;
    public static final int ID_SETTINGS = 5;
    public static final int ID_SIGN_OUT = 10;


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
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_SETTINGS);
                        return false;
                    }
                });
        PrimaryDrawerItem signOut = new PrimaryDrawerItem()
                .withIdentifier(ID_SIGN_OUT)
                .withName("Sign Out")
                .withSelectable(false)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_SIGN_OUT);
                        return false;
                    }
                });

        ProfileDrawerItem profile = new ProfileDrawerItem();
        profile.withName(userName);
        profile.withEmail(email);
        if (photoUri != null) {
            profile.withIcon(photoUri);
        } else {
            profile.withIcon(context.getResources().getDrawable(R.drawable.ic_launcher_foreground));
        }

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(context)
                .withHeaderBackground(R.drawable.ic_launcher_background)
                .withSelectionListEnabledForSingleProfile(false)
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
                        settings,
                        signOut
                ).build();
        return drawer;
    }

    public interface OnNavigationItemClickedListener {
        void OnNavigationItemClicked(int identifier);
    }
}
