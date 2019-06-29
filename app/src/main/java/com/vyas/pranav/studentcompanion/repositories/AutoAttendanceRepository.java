package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceDao;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlacesDatabase;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

public class AutoAttendanceRepository {

    private Context context;
    private AutoAttendancePlaceDao autoAttendancePlaceDao;
    private AppExecutors mExecutors;

    public AutoAttendanceRepository(Context context) {
        this.context = context;
        autoAttendancePlaceDao = AutoAttendancePlacesDatabase.getInstance(context).autoAttendancePlaceDao();
        mExecutors = AppExecutors.getInstance();
    }

    public void updatePlaceEntry(AutoAttendancePlaceEntry placeEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                autoAttendancePlaceDao.updatePlaceEntry(placeEntry);
            }
        });
    }

    public void insertPlaceEntry(AutoAttendancePlaceEntry placeEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                autoAttendancePlaceDao.insertNewPlaceEntry(placeEntry);
            }
        });
    }

    public LiveData<AutoAttendancePlaceEntry> getPlaceEntryOfSubject(String subject) {
        return autoAttendancePlaceDao.getPlaceEntryOfSubject(subject);
    }

}
