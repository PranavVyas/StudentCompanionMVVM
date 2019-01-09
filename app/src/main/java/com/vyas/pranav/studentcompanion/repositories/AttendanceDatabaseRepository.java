package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;

public class AttendanceDatabaseRepository {

    private AttendanceDao attendanceDao;
    private AppExecutors mExecutors;

    public AttendanceDatabaseRepository(Context context) {
        AttendanceDatabase mAttendanceDb = AttendanceDatabase.getInstance(context);
        attendanceDao = mAttendanceDb.attendanceDao();
        mExecutors = AppExecutors.getInstance();
    }

    public AttendanceDatabaseRepository(AttendanceDao attendanceDao) {
        this.attendanceDao = attendanceDao;
        mExecutors = AppExecutors.getInstance();
    }

    public LiveData<List<AttendanceEntry>> getAttendanceForDate(Date date) {
        return attendanceDao.getAttendanceForDate(date);
    }

    public void deleteAttendance(final AttendanceEntry attendanceEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                attendanceDao.deleteAttendance(attendanceEntry);
            }
        });
    }

    public void deleteAllAttendance() {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                attendanceDao.deleteAll();
            }
        });
    }

    public void updateAttendance(final AttendanceEntry attendanceEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                attendanceDao.updateAttendance(attendanceEntry);
            }
        });
    }

    public void insertAllAttendanceAndOverallAttendance(final List<AttendanceEntry> attendanceEntries, final Context context) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < attendanceEntries.size(); i++) {
                    attendanceDao.insertAttendance(attendanceEntries.get(i));
                }
                SetUpProcessRepository repo = new SetUpProcessRepository(context);
                repo.initializeOverallAttendance();
            }
        });
    }
}
