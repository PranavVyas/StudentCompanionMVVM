package com.vyas.pranav.studentcompanion.data.timetabledatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = TimetableEntry.class, version = 1, exportSchema = false)
public abstract class TimetableDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    public static final String DB_NAME = "TimetableDatabase";
    private static TimetableDatabase sInstance;

    public static TimetableDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        TimetableDatabase.class,
                        context.getExternalFilesDir(null).getPath() + DB_NAME)
                        .fallbackToDestructiveMigration().build();
            }
        }
        return sInstance;
    }

    public abstract TimetableDao timetableDao();
}
