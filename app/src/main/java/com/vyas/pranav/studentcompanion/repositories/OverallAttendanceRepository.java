package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

import java.util.List;

import androidx.lifecycle.LiveData;

public class OverallAttendanceRepository {

    private OverallAttendanceDao overallAttendanceDao;
    private AttendanceDao attendanceDao;
    private AppExecutors mExecutors;
    private Context application;

    public OverallAttendanceRepository(Context application) {
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        if (application == null) {
            Logger.d("Application context is null in OverallAttendanceRepository");
        }
        OverallAttendanceDatabase mOverallDb = OverallAttendanceDatabase.getInstance(application);
        this.overallAttendanceDao = mOverallDb.overallAttendanceDao();
        AttendanceDatabase mAttendanceDb = AttendanceDatabase.getInstance(application);
        this.attendanceDao = mAttendanceDb.attendanceDao();
        this.mExecutors = AppExecutors.getInstance();
        this.application = application;
    }

    public LiveData<List<OverallAttendanceEntry>> getAllOverallAttendance() {
        return overallAttendanceDao.getAllOverallAttendance();
    }

    public void insertOverallAttendance(final OverallAttendanceEntry overallAttendanceEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                overallAttendanceDao.insertOverall(overallAttendanceEntry);
            }
        });
    }

    public void updateOverallAttendance(final OverallAttendanceEntry overallAttendanceEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                overallAttendanceDao.updateOverall(overallAttendanceEntry);
            }
        });
    }

    public void deleteOverallAttendance(final OverallAttendanceEntry overallAttendanceEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                overallAttendanceDao.deleteOverall(overallAttendanceEntry);
            }
        });
    }

    public void deleteAllOverallAttendance() {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                overallAttendanceDao.deleteAllOverall();
            }
        });
    }

    public void refreshAllOverallAttendance() {
        if (application == null) {
            Logger.d("Context empty in refreshOverallAttendance");
        }
        SetUpProcessRepository setUpProcessRepository = new SetUpProcessRepository(application);
        List<String> subList = setUpProcessRepository.getSubjectsListOnly();
        for (int i = 0; i < subList.size(); i++) {
            String subject = subList.get(i);
            OverallAttendanceForSubjectRepository repository = new OverallAttendanceForSubjectRepository(application, subject);
            repository.refreshOverallAttendanceForSubject(subject);
        }
//        mExecutors.diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                overallAttendanceDao.getAllOverallAttendance().observeForever(new Observer<List<OverallAttendanceEntry>>() {
//                    @Override
//                    public void onChanged(List<OverallAttendanceEntry> overallAttendanceEntries) {
//                        overallAttendanceDao.getAllOverallAttendance().removeObserver(this);
//                        SetUpProcessRepository repo = new SetUpProcessRepository(application);
//                        for (int i = 0; i < overallAttendanceEntries.size(); i++) {
//                            OverallAttendanceEntry overallAttendanceEntry = overallAttendanceEntries.get(i);
//                            String subName = overallAttendanceEntry.getSubName();
//                            overallAttendanceEntry.setPresentDays(attendanceDao.getAttendedDaysForSubject(subName,ConverterUtils.convertStringToDate(repo.getStartingDate()),new Date()));
//                            overallAttendanceEntry.setBunkedDays(attendanceDao.getBunkedDaysForSubject(subName,ConverterUtils.convertStringToDate(repo.getStartingDate()),new Date()));
//                            updateOverallAttendance(overallAttendanceEntry);
//                        }
//                    }
//                });
//            }
//        });
    }
}
