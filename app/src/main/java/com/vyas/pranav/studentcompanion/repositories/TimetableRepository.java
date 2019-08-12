package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableDao;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

import java.util.List;

public class TimetableRepository {

    private TimetableDao timetableDao;
    private AppExecutors mExecutors;
    private Context context;
    private static final Object LOCK = new Object();

    private static TimetableRepository instance;

    public static TimetableRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new TimetableRepository(context.getApplicationContext());
            }
        }
        return instance;
    }

    public TimetableRepository(Context context) {
        this.context = context;
        this.timetableDao = MainDatabase.getInstance(context).timetableDao();
        mExecutors = AppExecutors.getInstance();
    }

    public void insertTimetable(final TimetableEntry timetableEntry) {
        mExecutors.diskIO().execute(() -> timetableDao.insertTimeTableEntry(timetableEntry));
    }

    public void insertTimetable(final List<TimetableEntry> timetableEntries) {
        mExecutors.diskIO().execute(() -> timetableDao.insertAllTimeTableEntry(timetableEntries));
    }

    public LiveData<List<TimetableEntry>> getFullTimetable() {
        return timetableDao.getFullTimetable();
    }

    public LiveData<List<TimetableEntry>> getTimetableForDay(String day) {
        return timetableDao.getTimetableForDay(day);
    }
}
