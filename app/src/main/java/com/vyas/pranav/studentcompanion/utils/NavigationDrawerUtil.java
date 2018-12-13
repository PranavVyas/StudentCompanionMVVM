package com.vyas.pranav.studentcompanion.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import androidx.appcompat.widget.Toolbar;

public class NavigationDrawerUtil {

    public static final int ID_TODAY_ATTENDANCE = 1;
    public static final int ID_OVERALL_ATTENDANCE = 2;
    public static final int ID_HOLIDAYS = 3;
    public static final int ID_TIMETABLE = 4;
    public static final int ID_SETTINGS = 5;


    public static Drawer getMaterialDrawer(Context context, Toolbar toolbar) {

        final OnNavigationItemClickedListener mCallback = (OnNavigationItemClickedListener) context;

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

        Drawer drawer = new DrawerBuilder()
                .withActivity((Activity) context)
                .withToolbar(toolbar)
                .addDrawerItems(
                        todayAttendance,
                        overallAttendance,
                        holidays,
                        timetable,
                        settings
                ).build();

        return drawer;
    }

    public interface OnNavigationItemClickedListener {
        void OnNavigationItemClicked(int identifier);
    }
}
