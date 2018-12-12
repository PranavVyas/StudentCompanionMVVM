package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;
import com.vyas.pranav.studentcompanion.repositories.TimetableRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TimetableViewModel extends AndroidViewModel {

    private LiveData<List<TimetableEntry>> timetableEntries;
    private TimetableRepository timetableRepository;
    private SetUpProcessRepository setUpProcessRepository;
    private int lecturesPerDay;

    public TimetableViewModel(@NonNull Application application) {
        super(application);
        timetableRepository = new TimetableRepository(application);
        timetableEntries = timetableRepository.getFullTimetable();
        setUpProcessRepository = new SetUpProcessRepository(application);
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
}
