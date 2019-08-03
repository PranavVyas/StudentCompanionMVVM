package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;
import com.vyas.pranav.studentcompanion.repositories.TimetableRepository;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;
import com.vyas.pranav.studentcompanion.utils.Generators;

import java.util.ArrayList;
import java.util.List;

import static com.vyas.pranav.studentcompanion.utils.ConverterUtils.getDayFromInt;

public class SetUpViewModel extends AndroidViewModel {

    private String startDate;
    private String endDate;
    private int currentStep;
    private List<String> subjectList;
    private int currentDay;
    private Application application;
    private int semester;
    private boolean isFirstRun;
    private int noOfLecturesPerDay;
    private String currentPath;

    private SetUpProcessRepository repository;
    private TimetableRepository timetableRepository;
    private int attendanceCriteria = 0;
    private FirestoreQueryLiveData collagesLiveData;

    public SetUpViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = new SetUpProcessRepository(application);
        isFirstRun = repository.isAppFirstRun();
        FirebaseFirestore mDb = FirebaseFirestore.getInstance();
        CollectionReference mRef = mDb.collection(Constants.PATH_COLLAGES);
        collagesLiveData = repository.getCollagesLiveData(mRef);
    }

    public void init() {
        startDate = repository.getStartingDate() == null ? "Select Starting Date" : repository.getStartingDate();
        endDate = repository.getEndingDate() == null ? "Select Ending Date" : repository.getEndingDate();
        currentStep = repository.getSetUpCurrentStep();
        subjectList = repository.getSubjectList();
        currentDay = repository.getCurrentDay();
        semester = repository.getSemester();
        noOfLecturesPerDay = repository.getNoOfLecturesPerDay();
        attendanceCriteria = repository.getCurrentAttendanceCriteria();
        currentPath = repository.getCurrentPath();
    }

    public FirestoreQueryLiveData getCollagesLiveData() {
        return collagesLiveData;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        repository.setUpStartingDate(startDate);
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        repository.setUpEndingDate(endDate);
        this.endDate = endDate;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        repository.setUpCurrentStep(currentStep);
        this.currentStep = currentStep;
    }

    public List<String> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<String> listSubjects) {
        repository.setSubjectListInSharedPrefrences(listSubjects);
        this.subjectList = repository.getSubjectList();
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        repository.setCurrentDay(currentDay);
        this.currentDay = currentDay;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        repository.setUpSemester(semester);
        this.semester = semester;
    }

    public int getNoOfLecturesPerDay() {
        return noOfLecturesPerDay;
    }

    public void setNoOfLecturesPerDay(int noOfLecturesPerDay) {
        repository.setNoOfLecturesPerDay(noOfLecturesPerDay);
        this.noOfLecturesPerDay = noOfLecturesPerDay;
    }

    public void setLectureStartTimeInSharedPrefs(int lectureNo, int timeStart) {
        repository.setLectureStartTimeInSharedPrefs(lectureNo, timeStart);
    }

    public void setLectureEndTimeInSharedPrefs(int lectureNo, int timeEnd) {
        repository.setLectureEndTimeInSharedPrefs(lectureNo, timeEnd);
    }

    private int getStartingTimeOfLecture(int lectureNo) {
        return repository.getStartTimeForLecture(lectureNo);
    }

    private int getEndingTimeOfLecture(int lectureNo) {
        return repository.getEndTimeForLecture(lectureNo);
    }

    public void initTimetableAttendance() {
        timetableRepository = new TimetableRepository(application);
    }

    public void setTimetableAttendanceForDay(int day, List<String> schedule) {
        List<TimetableEntry> timetableEntries = new ArrayList<>();
        for (int i = 0; i < schedule.size(); i++) {
            int lectureNo = i + 1;
            String id = Generators.generateIdForTimetableEntry(lectureNo, semester, day);
            TimetableEntry temp = new TimetableEntry();
            temp.set_ID(id);
            temp.setDay(getDayFromInt(day));
            temp.setLectureNo(lectureNo);
            temp.setSubName(schedule.get(i));
            temp.setTimeStart(getStartingTimeOfLecture(i));
            temp.setTimeEnd(getEndingTimeOfLecture(i));
            timetableEntries.add(temp);
        }
        timetableRepository.insertTimetable(timetableEntries);
    }

    public LiveData<List<TimetableEntry>> getTimetableAttendanceForDay(int day) {
        return timetableRepository.getTimetableForDay(getDayFromInt(day));
    }

    public void saveHolidaysAndInitAttendance() {
        repository.saveHolidaysAndInitAttendance();
    }

    public boolean isFirstRun() {
        return isFirstRun;
    }

    public void setFirstRun(boolean firstRun) {
        repository.setAppFirstRun(firstRun);
        isFirstRun = firstRun;
    }

    public boolean isTutorialDone() {
        return repository.isTutorialDone();
    }

    public void setTutorialDone(boolean isDone) {
        repository.setTutorialDone(isDone);
    }

    public int getCurrentAttendanceCriteria() {
        return attendanceCriteria;
    }

    public void setCurrentAttendanceCriteria(int progress) {
        this.attendanceCriteria = progress;
        repository.setCurrentAttendanceCriteria(progress);
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
        repository.setCurrentPath(currentPath);
    }
}
