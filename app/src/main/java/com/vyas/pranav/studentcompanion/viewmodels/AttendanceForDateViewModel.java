package com.vyas.pranav.studentcompanion.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.repositories.AttendanceDatabaseRepository;

import java.util.Date;
import java.util.List;

public class AttendanceForDateViewModel extends ViewModel {

    private final LiveData<List<AttendanceEntry>> attendanceForDate;
    private final AttendanceDatabaseRepository repository;
    private final Date date;
    private String lastChangedSubject;

    public AttendanceForDateViewModel(MainDatabase mDb, Date date) {
        repository = new AttendanceDatabaseRepository(mDb.attendanceDao());
        this.date = date;
        attendanceForDate = repository.getAttendanceForDate(date);
    }

    public LiveData<List<AttendanceEntry>> getAttendanceForDate() {
        return attendanceForDate;
    }

    public void updateAttendance(AttendanceEntry attendanceEntry) {
        repository.updateAttendance(attendanceEntry);
    }

    public String getLastChangedSubject() {
        return lastChangedSubject;
    }

    public void setLastChangedSubject(String lastChangedSubject) {
        this.lastChangedSubject = lastChangedSubject;
    }

}
