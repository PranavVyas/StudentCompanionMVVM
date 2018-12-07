package com.vyas.pranav.studentcompanion.repositories;

import android.app.Application;
import android.os.AsyncTask;

import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;

import java.util.List;

import androidx.lifecycle.LiveData;

public class OverallAttendanceRepository {

    private OverallAttendanceDao overallAttendanceDao;

    public OverallAttendanceRepository(Application application) {
        OverallAttendanceDatabase mOverallDb = OverallAttendanceDatabase.getInstance(application);
        overallAttendanceDao = mOverallDb.overallAttendanceDao();
    }

    public OverallAttendanceRepository(OverallAttendanceDatabase mOverallDb) {
        overallAttendanceDao = mOverallDb.overallAttendanceDao();
    }

    public LiveData<List<OverallAttendanceEntry>> getAllOverallAttendance() {
        return overallAttendanceDao.getAllOverallAttendance();
    }

    public void insertOverallAttendance(OverallAttendanceEntry overallAttendanceEntry) {
        new InsertAttendance(overallAttendanceDao).execute(overallAttendanceEntry);
    }

    public void updateOverallAttendance(OverallAttendanceEntry overallAttendanceEntry) {
        new UpdateAttendance(overallAttendanceDao).execute(overallAttendanceEntry);
    }

    public void deleteOverallAttendance(OverallAttendanceEntry overallAttendanceEntry) {
        new DeleteAttendance(overallAttendanceDao).execute(overallAttendanceEntry);
    }

    public void deleteAllOverallAttendance() {
        new DeleteAllAttendance(overallAttendanceDao).execute();
    }

    private static class InsertAttendance extends AsyncTask<OverallAttendanceEntry, Void, Void> {
        OverallAttendanceDao dao;

        public InsertAttendance(OverallAttendanceDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(OverallAttendanceEntry... overallAttendanceEntries) {
            dao.insertOverall(overallAttendanceEntries[0]);
            return null;
        }
    }

    private static class UpdateAttendance extends AsyncTask<OverallAttendanceEntry, Void, Void> {
        OverallAttendanceDao dao;

        public UpdateAttendance(OverallAttendanceDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(OverallAttendanceEntry... overallAttendanceEntries) {
            dao.updateOverall(overallAttendanceEntries[0]);
            return null;
        }
    }

    private static class DeleteAttendance extends AsyncTask<OverallAttendanceEntry, Void, Void> {
        OverallAttendanceDao dao;

        public DeleteAttendance(OverallAttendanceDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(OverallAttendanceEntry... overallAttendanceEntries) {
            dao.deleteOverall(overallAttendanceEntries[0]);
            return null;
        }
    }

    private static class DeleteAllAttendance extends AsyncTask<Void, Void, Void> {
        OverallAttendanceDao dao;

        public DeleteAllAttendance(OverallAttendanceDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAllOverall();
            return null;
        }
    }

}
