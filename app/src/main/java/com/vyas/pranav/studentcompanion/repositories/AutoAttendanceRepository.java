package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceDao;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

public class AutoAttendanceRepository {

    private final Context context;
    private final AutoAttendancePlaceDao autoAttendancePlaceDao;
    private final AppExecutors mExecutors;
    private static final Object LOCK = new Object();
    private static AutoAttendanceRepository instance;

    public static AutoAttendanceRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new AutoAttendanceRepository(context.getApplicationContext());
            }
        }
        return instance;
    }
    public AutoAttendanceRepository(Context context) {
        this.context = context.getApplicationContext();
        autoAttendancePlaceDao = MainDatabase.getInstance(context).autoAttendancePlaceDao();
        mExecutors = AppExecutors.getInstance();
    }

    public void updatePlaceEntry(AutoAttendancePlaceEntry placeEntry) {
        mExecutors.diskIO().execute(() -> autoAttendancePlaceDao.updatePlaceEntry(placeEntry));
    }

    public void insertPlaceEntry(AutoAttendancePlaceEntry placeEntry) {
        mExecutors.diskIO().execute(() -> autoAttendancePlaceDao.insertNewPlaceEntry(placeEntry));
    }

    public LiveData<AutoAttendancePlaceEntry> getPlaceEntryOfSubject(String subject) {
        return autoAttendancePlaceDao.getPlaceEntryOfSubject(subject);
    }

}
