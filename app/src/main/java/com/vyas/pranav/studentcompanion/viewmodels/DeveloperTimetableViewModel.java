package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;
import com.vyas.pranav.studentcompanion.repositories.TimetableRepository;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeveloperTimetableViewModel extends AndroidViewModel {

    private TimetableRepository timetableRepository;
    private SetUpProcessRepository setUpProcessRepository;
    private int lecturesPerDay;
    private List<List<String>> daysLectures;
    private MutableLiveData<List<List<String>>> daysLectureLiveData;


    public DeveloperTimetableViewModel(@NonNull Application application) {
        super(application);
        timetableRepository = TimetableRepository.getInstance(application);
        setUpProcessRepository = SetUpProcessRepository.getInstance(application);
        lecturesPerDay = setUpProcessRepository.getNoOfLecturesPerDay();
        daysLectureLiveData = new MutableLiveData<>();

        AppExecutors.getInstance().diskIO().execute(() -> {
            List<TimetableEntry> fullTimetableMainThread = timetableRepository.getFullTimetableMainThread();
            List<String> Monday = new ArrayList<>();
            List<String> Tuesday = new ArrayList<>();
            List<String> Wednesday = new ArrayList<>();
            List<String> Thursday = new ArrayList<>();
            List<String> Friday = new ArrayList<>();

            for (int i = 0; i < fullTimetableMainThread.size(); i++) {
                int day = i / lecturesPerDay;
                int lecture = i % lecturesPerDay;
                switch (day) {
                    case 0:
                        //Monday
                        Monday.add(lecture, fullTimetableMainThread.get(i).getSubName());
                        break;

                    case 1:
                        //Tuesday
                        Tuesday.add(lecture, fullTimetableMainThread.get(i).getSubName());
                        break;

                    case 2:
                        //Wednesday
                        Wednesday.add(lecture, fullTimetableMainThread.get(i).getSubName());
                        break;

                    case 3:
                        //Thursday
                        Thursday.add(lecture, fullTimetableMainThread.get(i).getSubName());
                        break;

                    case 4:
                        //Friday
                        Friday.add(lecture, fullTimetableMainThread.get(i).getSubName());
                        break;
                }
            }

            daysLectures = new ArrayList<>(Arrays.asList(
                    Monday, Tuesday, Wednesday, Thursday, Friday
            ));

            daysLectureLiveData.postValue(daysLectures);
        });
    }

    public MutableLiveData<List<List<String>>> getDaysLectureLiveData() {
        return daysLectureLiveData;
    }

    public void setDaysLectures(int raw, int column, String newSub) {
        this.daysLectures.get(raw).set(column, newSub);
        daysLectureLiveData.postValue(daysLectures);
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

    public List<String> getSubjectList() {
        return setUpProcessRepository.getSubjectList();
    }

}
