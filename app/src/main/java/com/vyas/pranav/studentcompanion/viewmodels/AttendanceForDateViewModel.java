package com.vyas.pranav.studentcompanion.viewmodels;

import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.repositories.AttendanceDatabaseRepository;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AttendanceForDateViewModel extends ViewModel {

    private LiveData<List<AttendanceEntry>> attendanceForDate;
    private AttendanceDatabaseRepository repository;

    public AttendanceForDateViewModel(AttendanceDatabase mAttendanceDb, Date date) {
        repository = new AttendanceDatabaseRepository(mAttendanceDb.attendanceDao());
        attendanceForDate = repository.getAttendanceForDate(date);
    }

    public LiveData<List<AttendanceEntry>> getAttendanceForDate() {
        return attendanceForDate;
    }

    public void setAttendanceForDate(LiveData<List<AttendanceEntry>> attendanceForDate) {
        this.attendanceForDate = attendanceForDate;
    }

    public void updateAttendance(AttendanceEntry attendanceEntry) {
        repository.updateAttendance(attendanceEntry);
    }

}
