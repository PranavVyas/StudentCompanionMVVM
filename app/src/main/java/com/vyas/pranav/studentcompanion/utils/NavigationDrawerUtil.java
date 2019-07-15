package com.vyas.pranav.studentcompanion.utils;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.vyas.pranav.studentcompanion.R;

public class NavigationDrawerUtil {

    public static final int ID_TODAY_ATTENDANCE = 1;
    public static final int ID_OVERALL_ATTENDANCE = 2;
    //    public static final int ID_HOLIDAYS = 3;
//    public static final int ID_TIMETABLE = 4;
    public static final int ID_SETTINGS = 5;
    //    public static final int ID_MARKET_PLACE = 6;
    public static final int ID_NOTIFICATIONS = 7;
    public static final int ID_MY_PROFILE = 8;
    public static final int ID_ABOUT_APP = 9;
    public static final int ID_ABOUT_DEVELOPER = 10;
    public static final int ID_SIGN_OUT = 11;
    public static final int ID_DELETE_ACCOUNT = 12;
    public static final int ID_RESOURCES = 3;

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
                .withName(R.string.navigation_home)
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
                .withName(R.string.navigation_overall_attendance)
                .withIcon(R.drawable.ic_overall_attendance)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_OVERALL_ATTENDANCE);
                        return false;
                    }
                });
//        PrimaryDrawerItem holidays = new PrimaryDrawerItem()
//                .withIdentifier(ID_HOLIDAYS)
//                .withName("Holidays")
//                .withIcon(R.drawable.ic_holidays)
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        mCallback.OnNavigationItemClicked(ID_HOLIDAYS);
//                        return false;
//                    }
//                });
//        PrimaryDrawerItem timetable = new PrimaryDrawerItem()
//                .withIdentifier(ID_TIMETABLE)
//                .withName("Timetable")
//                .withIcon(R.drawable.ic_timetable)
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        mCallback.OnNavigationItemClicked(ID_TIMETABLE);
//                        return false;
//                    }
//                });
        PrimaryDrawerItem settings = new PrimaryDrawerItem()
                .withIdentifier(ID_SETTINGS)
                .withName(R.string.navigation_settings)
                .withIcon(R.drawable.ic_settings)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_SETTINGS);
                        return false;
                    }
                });
//        PrimaryDrawerItem marketplace = new PrimaryDrawerItem()
//                .withIdentifier(ID_MARKET_PLACE)
//                .withName("Buy/Sell Items")
//                .withIcon(R.drawable.ic_market_place)
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        mCallback.OnNavigationItemClicked(ID_MARKET_PLACE);
//                        return false;
//                    }
//                });
        PrimaryDrawerItem notifications = new PrimaryDrawerItem()
                .withIdentifier(ID_NOTIFICATIONS)
                .withName(R.string.navigation_notifications)
                .withIcon(R.drawable.ic_chat)
                .withBadge("0")
                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_NOTIFICATIONS);
                        return false;
                    }
                });
        SecondaryDrawerItem myProfile = new SecondaryDrawerItem()
                .withIdentifier(ID_MY_PROFILE)
                .withName(R.string.navigation_my_profile)
                .withIcon(R.drawable.ic_profile)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_MY_PROFILE);
                        return false;
                    }
                });
        SecondaryDrawerItem aboutApp = new SecondaryDrawerItem()
                .withIdentifier(ID_ABOUT_APP)
                .withName(R.string.navigation_about_this_app)
                .withIcon(R.mipmap.ic_launcher_round)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_ABOUT_APP);
                        return false;
                    }
                });
        SecondaryDrawerItem aboutDeveloper = new SecondaryDrawerItem()
                .withIdentifier(ID_ABOUT_DEVELOPER)
                .withName(R.string.navigation_about_developer)
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
                .withName(R.string.navigation_sign_out)
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
                .withName(R.string.navigation_delete_account)
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

        PrimaryDrawerItem resources = new PrimaryDrawerItem()
                .withIdentifier(ID_RESOURCES)
                .withName(R.string.navigation_resources)
                .withIcon(R.drawable.ic_resources_magic_wand)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mCallback.OnNavigationItemClicked(ID_RESOURCES);
                        return false;
                    }
                });

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

        //noinspection UnnecessaryLocalVariable
        Drawer drawer = new DrawerBuilder()
                .withActivity(context)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        todayAttendance,
                        overallAttendance,
                        resources,
                        notifications,
                        new DividerDrawerItem(),
//                        holidays,
//                        timetable,
//                        marketplace,
                        myProfile,
                        aboutApp,
                        aboutDeveloper,
                        new DividerDrawerItem(),
                        settings,
                        signOut,
                        deleteAccount
                ).build();
        return drawer;
    }

    public interface OnNavigationItemClickedListener {
        void OnNavigationItemClicked(int identifier);
    }
}
