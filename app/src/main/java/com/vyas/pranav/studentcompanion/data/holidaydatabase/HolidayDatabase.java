package com.vyas.pranav.studentcompanion.data.holidaydatabase;


import android.content.Context;

import com.vyas.pranav.studentcompanion.data.DateConverter;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = HolidayEntry.class, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)

public abstract class HolidayDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    public static final String DB_NAME = "HolidayDatabase";
    private static HolidayDatabase sInstance;

    public static HolidayDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(),
                    HolidayDatabase.class,
                    context.getExternalFilesDir(null).getPath() + DB_NAME)
                    .fallbackToDestructiveMigration().build();
        }
        return sInstance;
    }

    public abstract HolidayDao holidayDao();
}
