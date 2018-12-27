package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceDao;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlacesDatabase;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

import java.util.List;

import androidx.lifecycle.LiveData;

public class AutoAttendanceRepository {

    private Context context;
    private AutoAttendancePlaceDao autoAttendancePlaceDao;
    private AppExecutors mExecutors;

    public AutoAttendanceRepository(Context context) {
        this.context = context;
        autoAttendancePlaceDao = AutoAttendancePlacesDatabase.getInstance(context).autoAttendancePlaceDao();
        mExecutors = AppExecutors.getInstance();
    }

    public LiveData<List<AutoAttendancePlaceEntry>> getPlaces() {
        return autoAttendancePlaceDao.getAllPlaceIds();
    }

    public LiveData<AutoAttendancePlaceEntry> getPlaceIdOfSubject(String subject) {
        return autoAttendancePlaceDao.getPlaceIdOfSubject(subject);
    }


    public void insertNewPlace(final AutoAttendancePlaceEntry newPlace) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                autoAttendancePlaceDao.insertNewPlaceId(newPlace);
            }
        });
    }

    public void updatePlaceId(final AutoAttendancePlaceEntry placeEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                autoAttendancePlaceDao.updatePlaceId(placeEntry);
            }
        });
    }
}
