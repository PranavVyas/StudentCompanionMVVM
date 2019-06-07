package com.vyas.pranav.studentcompanion.data.notificationdatabase;

import android.content.Context;

import com.vyas.pranav.studentcompanion.data.DateConverter;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = NotificationEntry.class, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class NotificationDatabase extends RoomDatabase {

    public static final String DB_NAME = "NotificationDatabase";
    private static final Object LOCK = new Object();
    private static NotificationDatabase sInstance;

    public static NotificationDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        NotificationDatabase.class,
                        context.getExternalFilesDir(null).getPath() + DB_NAME)
                        .fallbackToDestructiveMigration().build();
            }
        }
        return sInstance;
    }

    public abstract NotificationDao notificationDao();
}
