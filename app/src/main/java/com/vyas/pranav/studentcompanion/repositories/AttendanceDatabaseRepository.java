package com.vyas.pranav.studentcompanion.repositories;

import android.app.Application;
import android.os.AsyncTask;

import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;

public class AttendanceDatabaseRepository {

    private AttendanceDao attendanceDao;

    public AttendanceDatabaseRepository(Application application) {
        AttendanceDatabase mAttendanceDb = AttendanceDatabase.getInstance(application);
        attendanceDao = mAttendanceDb.attendanceDao();
    }

    public AttendanceDatabaseRepository(AttendanceDao attendanceDao) {
        this.attendanceDao = attendanceDao;
    }

    public LiveData<List<AttendanceEntry>> getAttendanceForDate(Date date) {
        return attendanceDao.getAttendanceForDate(date);
    }

    public void insertAttendance(AttendanceEntry attendanceEntry) {
        new InsertAttendanceAsyncTask(attendanceDao).execute(attendanceEntry);
    }

    public void deleteAttendance(AttendanceEntry attendanceEntry) {
        new DeleteAttendanceAsyncTask(attendanceDao).execute(attendanceEntry);
    }

    public void deleteAllAttendance() {
        new DeleteAllAttendanceAsyncTask(attendanceDao).execute();
    }

    public void updateAttendance(AttendanceEntry attendanceEntry) {
        new UpdateAttendanceAsyncTask(attendanceDao).execute(attendanceEntry);
    }

    private static class InsertAttendanceAsyncTask extends AsyncTask<AttendanceEntry, Void, Void> {
        AttendanceDao attendanceDao;

        private InsertAttendanceAsyncTask(AttendanceDao attendanceDao) {
            this.attendanceDao = attendanceDao;
        }

        @Override
        protected Void doInBackground(AttendanceEntry... attendanceEntries) {
            attendanceDao.insertAttendance(attendanceEntries[0]);
            return null;
        }
    }

    private static class UpdateAttendanceAsyncTask extends AsyncTask<AttendanceEntry, Void, Void> {
        AttendanceDao attendanceDao;

        private UpdateAttendanceAsyncTask(AttendanceDao attendanceDao) {
            this.attendanceDao = attendanceDao;
        }

        @Override
        protected Void doInBackground(AttendanceEntry... attendanceEntries) {
            attendanceDao.updateAttendance(attendanceEntries[0]);
            return null;
        }
    }

    private static class DeleteAttendanceAsyncTask extends AsyncTask<AttendanceEntry, Void, Void> {
        AttendanceDao attendanceDao;

        private DeleteAttendanceAsyncTask(AttendanceDao attendanceDao) {
            this.attendanceDao = attendanceDao;
        }

        @Override
        protected Void doInBackground(AttendanceEntry... attendanceEntries) {
            attendanceDao.deleteAttendance(attendanceEntries[0]);
            return null;
        }
    }

    private static class DeleteAllAttendanceAsyncTask extends AsyncTask<Void, Void, Void> {
        AttendanceDao attendanceDao;

        private DeleteAllAttendanceAsyncTask(AttendanceDao attendanceDao) {
            this.attendanceDao = attendanceDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            attendanceDao.deleteAll();
            return null;
        }
    }

}
