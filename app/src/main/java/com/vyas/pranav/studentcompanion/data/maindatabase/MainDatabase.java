package com.vyas.pranav.studentcompanion.data.maindatabase;
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
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceDao;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryDao;
import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryEntry;
import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayDao;
import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayEntry;
import com.vyas.pranav.studentcompanion.data.lecturedatabase.LectureDao;
import com.vyas.pranav.studentcompanion.data.lecturedatabase.LectureEntry;
import com.vyas.pranav.studentcompanion.data.metadatadatabase.MetaDataDao;
import com.vyas.pranav.studentcompanion.data.metadatadatabase.MetadataEntry;
import com.vyas.pranav.studentcompanion.data.notedatabase.NoteDao;
import com.vyas.pranav.studentcompanion.data.notedatabase.NotesEntry;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.NotificationDao;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.firestore.NotificationFirestoreModel;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableDao;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.utils.DateConverter;

@Database(entities = {AttendanceEntry.class, AutoAttendancePlaceEntry.class, DigitalLibraryEntry.class, HolidayEntry.class, OverallAttendanceEntry.class, TimetableEntry.class, NotificationFirestoreModel.class, NotesEntry.class, LectureEntry.class, MetadataEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class MainDatabase extends RoomDatabase {
    public static final String DB_NAME = "MainDatabase";
    private static final Object LOCK = new Object();
    private static MainDatabase sInstance;

    public static MainDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext()
                        , MainDatabase.class
                        , context.getExternalFilesDir(null) + "/" + DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract LectureDao lectureDao();

    public abstract AttendanceDao attendanceDao();

    public abstract AutoAttendancePlaceDao autoAttendancePlaceDao();

    public abstract DigitalLibraryDao digitalLibraryDao();

    public abstract HolidayDao holidayDao();

    public abstract OverallAttendanceDao overallAttendanceDao();

    public abstract TimetableDao timetableDao();

    public abstract NotificationDao notificationDao();

    public abstract NoteDao noteDao();

    public abstract MetaDataDao metaDataDao();
}
