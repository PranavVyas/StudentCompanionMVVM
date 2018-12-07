package com.vyas.pranav.studentcompanion.utils;

import android.app.Activity;
import android.content.Context;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

import androidx.appcompat.widget.Toolbar;

public class NavigationDrawerUtil {

    public static final long ID_TODAY_ATTENDACE = 1;
    public static final long ID_OVERALL_ATTENDANCE = 2;

    public static Drawer getMaterialDrawer(Context context, Toolbar toolbar) {
        PrimaryDrawerItem todayAttendance = new PrimaryDrawerItem()
                .withIdentifier(ID_TODAY_ATTENDACE)
                .withName("Today's Attendance");
        PrimaryDrawerItem overallAttendance = new PrimaryDrawerItem()
                .withIdentifier(ID_OVERALL_ATTENDANCE)
                .withName("Overall Attendance");

        Drawer drawer = new DrawerBuilder()
                .withActivity((Activity) context)
                .withToolbar(toolbar)
                .addDrawerItems(
                        todayAttendance,
                        overallAttendance
                ).build();

        return drawer;
    }
}
