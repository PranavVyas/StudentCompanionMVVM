package com.vyas.pranav.studentcompanion.repositories;

import android.app.Application;

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

    public AttendanceDatabaseRepository(Application application) {
        AttendanceDatabase mAttendanceDb = AttendanceDatabase.getInstance(application);
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

    public void insertAttendance(final AttendanceEntry attendanceEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                attendanceDao.insertAttendance(attendanceEntry);
            }
        });
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
}
