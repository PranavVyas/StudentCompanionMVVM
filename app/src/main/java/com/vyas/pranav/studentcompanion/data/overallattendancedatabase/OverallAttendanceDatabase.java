package com.vyas.pranav.studentcompanion.data.overallattendancedatabase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = OverallAttendanceEntry.class, version = 1, exportSchema = false)
public abstract class OverallAttendanceDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static OverallAttendanceDatabase sInstance;
    static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            insertFakeData(sInstance);
        }
    };
    private static String DB_NAME = "OverallAttendanceDatabase";

    public static OverallAttendanceDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context
                        , OverallAttendanceDatabase.class
                        , DB_NAME)
                        .addCallback(roomCallback)
                        .fallbackToDestructiveMigration().build();
            }
        }
        return sInstance;
    }

    private static void insertFakeData(OverallAttendanceDatabase db) {
        new AddDataAsynvTask(db).execute();
    }

    public abstract OverallAttendanceDao overallAttendanceDao();

    static class AddDataAsynvTask extends AsyncTask<Void, Void, Void> {

        OverallAttendanceDao dao;

        public AddDataAsynvTask(OverallAttendanceDatabase db) {
            dao = db.overallAttendanceDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.insertOverall(new OverallAttendanceEntry(601, "Sub1", 200, 180, 12, 5));
            dao.insertOverall(new OverallAttendanceEntry(602, "Sub2", 220, 210, 7, 4));
            dao.insertOverall(new OverallAttendanceEntry(603, "Sub3", 180, 110, 20, 5));
            dao.insertOverall(new OverallAttendanceEntry(604, "Sub4", 197, 197, 0, 3));
            dao.insertOverall(new OverallAttendanceEntry(605, "Sub5", 120, 75, 15, 2));
            return null;
        }
    }
}
