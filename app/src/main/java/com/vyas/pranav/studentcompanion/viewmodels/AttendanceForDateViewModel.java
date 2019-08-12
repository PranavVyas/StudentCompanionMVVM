package com.vyas.pranav.studentcompanion.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.repositories.AttendanceDatabaseRepository;
import com.vyas.pranav.studentcompanion.repositories.OverallAttendanceRepository;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.util.Date;
import java.util.List;

public class AttendanceForDateViewModel extends ViewModel {

    private final LiveData<List<AttendanceEntry>> attendanceForDate;
    private final AttendanceDatabaseRepository repository;
    private final Date date;
    private String lastChangedSubject;
    private OverallAttendanceRepository overallRepo;
    private AppExecutors mExecutors;
    private Context context;
    private SharedPreferencesUtils utils;

    public AttendanceForDateViewModel(Context context, Date date) {
        repository = new AttendanceDatabaseRepository(MainDatabase.getInstance(context).attendanceDao());
        this.date = date;
        attendanceForDate = repository.getAttendanceForDate(date);
        mExecutors = AppExecutors.getInstance();
        overallRepo = OverallAttendanceRepository.getInstance(context);
        utils = SharedPreferencesUtils.getInstance(context);
    }


    public LiveData<List<AttendanceEntry>> getAttendanceForDate() {
        return attendanceForDate;
    }

    public void updateAttendanceInSequence(AttendanceEntry attendanceEntry) {
        mExecutors.diskIO().execute(() -> {
            repository.getAttendanceDao().updateAttendance(attendanceEntry);
            setUpOverallAttendance(attendanceEntry.getSubjectName());
        });
    }

    public String getLastChangedSubject() {
        return lastChangedSubject;
    }

    public void setLastChangedSubject(String lastChangedSubject) {
        this.lastChangedSubject = lastChangedSubject;
    }

    private void setUpOverallAttendance(String subName) {
        overallRepo.refreshOverallAttendanceForSubject(subName);
    }

    public Date getEndingDate() {
        String endingDate = utils.getEndingDate();
        return ConverterUtils.convertStringToDate(endingDate);
    }

    public Date getStartingDate() {
        return ConverterUtils.convertStringToDate(utils.getStartingDate());
    }
}
