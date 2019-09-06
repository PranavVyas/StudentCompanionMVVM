package com.vyas.pranav.studentcompanion.utils;

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
public final class Constants {

    public static final String KEY_SEND_DATA_TO_NOTIFICATION_CLASS_DATE = "NotificationFragmentToAllNotificationFragment";
    public static final String DEFAULT_LECTURE = "No Lecture";

    public static final int REFRESH_GEO_FENCE_RC_OPEN_APP = 1000;
    public static final int REFRESH_GEO_FENCE_RC_SHOW_NOTIFICATION = 1001;

    public static final int SHOW_REMINDER_JOB_RC_SHOW_NOTIFICATION = 1002;
    public static final int SHOW_REMINDER_JOB_RC_OPEN_APP = 1003;
    public static final int SHOW_REMINDER_JOB_RC_MARK_ATTENDANCE = 1004;
    public static final int SHOW_REMINDER_JOB_RC_CONTENT_INTENT = 1005;

    public static final int SILENT_JOB_RC_SHOW_NOTIFICATION = 1006;
    public static final int SILENT_JOB_RC_OPEN_APP = 1007;
    public static final int SILENT_JOB_RC_UNSILENT_ACTION = 1010;

    public static final int UNSILENT_JOB_RC_SHOW_NOTIFICATION = 1008;
    public static final int UNSILENT_JOB_RC_OPEN_APP = 1009;
//    public static final int UNSILENT_JOB_RC_UNSILENT = 1011;

    public static final String INTENT_ACTION_UNSILENT_DEVICE = "ACTION_UNSILENT_DEVICE";
    public static final String INTENT_ACTION_SILENT_DEVICE = "ACTION_SILENT_DEVICE";
    public static final int RC_OPEN_MARKET_PLACE_NEW_AD = 1020;

    public static final String EXTRA_TIMETABLE_DAY_KEY = "EXTRA_TIMETABLE_DAY_KEY";
    public static final String EXTRA_TIMETABLE_DAY = "EXTRA_TIMETABLE_DAY";
    public static final int EVENTS_SHOW_NOTIFICATION = 1012;
    public static final int SHOW_NOTIFICATION_RC_CONTENT_INTENT = 1013;
    public static final int SHOW_NOTIFICATION_RC_OPEN_APP = 1014;
    public static final int RC_SETTINGS_SILENT_DEVICE = 1015;
    public static final String KEY_FENCE_LOCATION = "KEY_FENCE_LOCATION";
    public static final int RC_OPEN_PLACE_PICKER_CUSTOM = 1016;
    public static final int RC_LOCATION_RECEIVED = 1017;
    public static final double DEFAULT_LANG = 0.0;
    public static final double DEFAULT_LAT = 0.0;
    public static final String FENCE_RECEIVER_ACTION = "com.vyas.pranav.studentcompanion.ACTION_FENCE_INTENT";
    public static final int RC_SEND_FENCE_BROADCAST = 1018;
    public static final int FENCE_CALLBACK_NOTIFICATION = 1019;
    public static final int RC_SIGN_IN = 1020;

    public static final int NOTI_TYPE_EVENT = 1;
    public static final int NOTI_TYPE_LOW_ATTENDANCE = 2;

    public static final String EXTRA_MAIN_ACT_OPEN_OVERALL = "OPEN_MAIN_ACT_WITH_FRAGMENT";
    public static final int TIME_WINDOW_SILENT_DEVICE = 5;
    public static final int TIME_WINDOW_UNSILENT_DEVICE = 5;
    public static final String DATE_BTN_DEFAULT = "Select Date";
    public static final String DESC_ET_DEFAULT = "Description";
    public static final String TITLE_ET_DEFAULT = "Title";
    public static final int PAGE_SIZE_NOTES = 20;
    public static final String PATH_EVENTS_SVNIT = "/events";
    public static final String PATH_SELL_SVNIT = "/sell";
    public static final String PATH_HOLIDAYS_SVNIT = "/holidays";
    public static final String PATH_DIGITAL_LIBRARY_SVNIT = "/digitalLibrary";
    public static final String PATH_COLLAGES = "collage";
    public static final int PAGE_SIZE_DIGITAL_LIBRARY = 20;
    public static final int SHOW_NOTIFICATION_RC_OVERALL_REFRESH_ATTENDANCE = 1021;
    public static final int RC_OPEN_BACKUP_RESTORE_ACTIVITY = 1022;
    public static final String METADATA_ATTENDANCE_CRITERIA = "METADATA_ATTENDANCE_CRITERIA";

    public static final String METADATA_CURRENT_PATH = "METADATA_CURRENT_PATH";
    public static final String METADATA_SEMESTER = "METADATA_SEMESTER";
    public static final int ABSENT = -1;
    public static final int PRESENT = 1;
    public static final int CANCELLED = 0;
}
