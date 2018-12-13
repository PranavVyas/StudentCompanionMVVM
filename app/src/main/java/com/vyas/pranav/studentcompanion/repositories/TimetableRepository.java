package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableDao;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableDatabase;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TimetableRepository {

    private TimetableDao timetableDao;
    private AppExecutors mExecutors;
    private Context context;

//    public TimetableRepository(TimetableDatabase mTimetableDb) {
//        this.timetableDao = mTimetableDb.timetableDao();
//        mExecutors = AppExecutors.getInstance();
//    }

    public TimetableRepository(Context context) {
        this.context = context;
        this.timetableDao = TimetableDatabase.getInstance(context).timetableDao();
        mExecutors = AppExecutors.getInstance();
    }

    public void insertTimetable(final TimetableEntry timetableEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                timetableDao.insertTimeTableEntry(timetableEntry);
            }
        });
    }

    public void insertTimetable(final List<TimetableEntry> timetableEntries) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                timetableDao.insertAllTimeTableEntry(timetableEntries);
            }
        });
    }

    public LiveData<List<TimetableEntry>> getFullTimetable() {
        return timetableDao.getFullTimetable();
    }

    public LiveData<List<TimetableEntry>> getTimetableForDay(String day) {
        return timetableDao.getTimetableForDay(day);
    }
}
