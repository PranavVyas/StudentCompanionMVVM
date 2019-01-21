package com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = AutoAttendancePlaceEntry.class, version = 1, exportSchema = false)
public abstract class AutoAttendancePlacesDatabase extends RoomDatabase {
    public static final String DB_NAME = "PlacesDatabase";
    private static final Object LOCK = new Object();
    private static AutoAttendancePlacesDatabase sInstance;

    public static AutoAttendancePlacesDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AutoAttendancePlacesDatabase.class
                        , DB_NAME)
                        .fallbackToDestructiveMigration().build();
            }
        }
        return sInstance;
    }

    public abstract AutoAttendancePlaceDao autoAttendancePlaceDao();
}
