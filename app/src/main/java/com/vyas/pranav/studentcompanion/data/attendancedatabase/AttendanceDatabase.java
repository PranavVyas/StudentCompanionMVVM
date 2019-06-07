package com.vyas.pranav.studentcompanion.data.attendancedatabase;

import android.content.Context;

import com.vyas.pranav.studentcompanion.data.DateConverter;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = AttendanceEntry.class, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AttendanceDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    public static final String DB_NAME = "AttendanceDatabase";
    private static AttendanceDatabase sInstance;
//    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            populateFakeAttendance(sInstance);
//        }
//    };

    public static AttendanceDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext()
                        , AttendanceDatabase.class
                        , context.getExternalFilesDir(null).getPath() + DB_NAME)
                        .fallbackToDestructiveMigration()
                        //.addCallback(callback)
                        .build();
            }
        }
        return sInstance;
    }

//    private static void populateFakeAttendance(AttendanceDatabase db) {
//        new addEntries(db).execute();
//    }

    public abstract AttendanceDao attendanceDao();

//    private static class addEntries extends AsyncTask<Void, Void, Void> {
//        AttendanceDao attendanceDao;
//
//        private addEntries(AttendanceDatabase db) {
//            attendanceDao = db.attendanceDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            AttendanceEntry attendanceEntry = new AttendanceEntry("0612201801", ConverterUtils.convertStringToDate(Constants.TEST_DATE_1), 1, "Sub1", "F1", true);
//            AttendanceEntry attendanceEntry2 = new AttendanceEntry("0612201802", ConverterUtils.convertStringToDate(Constants.TEST_DATE_1), 2, "Sub2", "F2", true);
//            AttendanceEntry attendanceEntry3 = new AttendanceEntry("0612201803", ConverterUtils.convertStringToDate(Constants.TEST_DATE_1), 3, "Sub3", "F3", false);
//            AttendanceEntry attendanceEntry4 = new AttendanceEntry("0612201804", ConverterUtils.convertStringToDate(Constants.TEST_DATE_1), 4, "Sub4", "F4", true);
//            AttendanceEntry attendanceEntry5 = new AttendanceEntry("0712201801", ConverterUtils.convertStringToDate(Constants.TEST_DATE_2), 1, "Sub1", "F1", false);
//            List<AttendanceEntry> entries = new ArrayList<>();
//            entries.add(attendanceEntry);
//            entries.add(attendanceEntry2);
//            entries.add(attendanceEntry3);
//            entries.add(attendanceEntry4);
//            entries.add(attendanceEntry5);
//            attendanceDao.insertAllAttendance(entries);
//            return null;
//        }
//    }

}
