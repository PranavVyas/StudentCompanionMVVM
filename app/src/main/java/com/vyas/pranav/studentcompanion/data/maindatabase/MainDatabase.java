package com.vyas.pranav.studentcompanion.data.maindatabase;

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
import com.vyas.pranav.studentcompanion.data.notedatabase.NoteDao;
import com.vyas.pranav.studentcompanion.data.notedatabase.NotesEntry;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.NotificationDao;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.firestore.NotificationFirestoreModel;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableDao;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.utils.DateConverter;

@Database(entities = {AttendanceEntry.class, AutoAttendancePlaceEntry.class, DigitalLibraryEntry.class, HolidayEntry.class, OverallAttendanceEntry.class, TimetableEntry.class, NotificationFirestoreModel.class, NotesEntry.class}, version = 1, exportSchema = false)
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
                        , context.getExternalFilesDir(null).getPath() + DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract AttendanceDao attendanceDao();

    public abstract AutoAttendancePlaceDao autoAttendancePlaceDao();

    public abstract DigitalLibraryDao digitalLibraryDao();

    public abstract HolidayDao holidayDao();

    public abstract OverallAttendanceDao overallAttendanceDao();

    public abstract TimetableDao timetableDao();

    public abstract NotificationDao notificationDao();

    public abstract NoteDao noteDao();
}
