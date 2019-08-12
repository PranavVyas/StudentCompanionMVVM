package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;
import com.vyas.pranav.studentcompanion.repositories.TimetableRepository;

import java.util.List;

public class TimetableViewModel extends AndroidViewModel {

    private LiveData<List<TimetableEntry>> timetableEntries;
    private TimetableRepository timetableRepository;
    private SetUpProcessRepository setUpProcessRepository;
    private int lecturesPerDay;
    private boolean isProductiveViewOn = false;
    private int currentPage = 0;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isProductiveViewOn() {
        return isProductiveViewOn;
    }

    public void setProductiveViewOn(boolean productiveViewOn) {
        isProductiveViewOn = productiveViewOn;
    }

    public TimetableViewModel(@NonNull Application application) {
        super(application);
        timetableRepository = TimetableRepository.getInstance(application);
        timetableEntries = timetableRepository.getFullTimetable();
        setUpProcessRepository = SetUpProcessRepository.getInstance(application);
        lecturesPerDay = setUpProcessRepository.getNoOfLecturesPerDay();
    }

    public LiveData<List<TimetableEntry>> getTimetableEntries() {
        return timetableEntries;
    }

    public int getStartingTimeOfLecture(int lectureNo) {
        return setUpProcessRepository.getStartTimeForLecture(lectureNo);
    }

    public int getEndingTimeOfLecture(int lectureNo) {
        return setUpProcessRepository.getEndTimeForLecture(lectureNo);
    }

    public int getLecturesPerDay() {
        return lecturesPerDay;
    }

    public int getSemInfo() {
        return setUpProcessRepository.getSemester();
    }

    public String getStartDate() {
        return setUpProcessRepository.getStartingDate();
    }

    public String getEndDate() {
        return setUpProcessRepository.getEndingDate();
    }

}
