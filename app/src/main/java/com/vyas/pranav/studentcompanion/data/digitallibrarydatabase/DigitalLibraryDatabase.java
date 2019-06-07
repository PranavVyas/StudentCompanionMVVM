package com.vyas.pranav.studentcompanion.data.digitallibrarydatabase;

import android.content.Context;

import com.vyas.pranav.studentcompanion.data.DateConverter;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = DigitalLibraryEntry.class, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class DigitalLibraryDatabase extends RoomDatabase {

    public static final String DB_NAME = "DigitalLibraryDb";
    private static final Object LOCK = new Object();
    private static DigitalLibraryDatabase sInstance;

    public static DigitalLibraryDatabase getInstance(Context context) {
        if (sInstance != null) {
            return sInstance;
        }
        synchronized (LOCK) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(),
                    DigitalLibraryDatabase.class,
                    context.getExternalFilesDir(null).getPath() + DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return sInstance;
    }

    public abstract DigitalLibraryDao digitalLibraryDao();

}
