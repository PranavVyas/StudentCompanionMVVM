package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.repositories.AttendanceDatabaseRepository;
import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;
import com.vyas.pranav.studentcompanion.repositories.TimetableRepository;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.Generators;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import static com.vyas.pranav.studentcompanion.utils.ConverterUtils.getDayFromInt;

public class SetUpViewModel extends AndroidViewModel implements SetUpProcessRepository.OnEligibleDatesCalculatedListener {

    private String startDate;
    private String endDate;
    private int currentStep;
    private List<String[]> listSubjectAndCredits;
    private int currentDay;
    private Application application;
    private int semester;
    private boolean isFirstRun;
    private int noOfLecturesPerDay;

    private SetUpProcessRepository repository;
    private TimetableRepository timetableRepository;

    private List<TimetableEntry> Monday;
    private List<TimetableEntry> Tuesday;
    private List<TimetableEntry> Wednesday;
    private List<TimetableEntry> Thursday;
    private List<TimetableEntry> Friday;

    public SetUpViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = new SetUpProcessRepository(application);
        repository.setOnEligibleDatesCalculatedListener(this);
        startDate = repository.getStartingDate() == null ? "Select Starting Date" : repository.getStartingDate();
        endDate = repository.getEndingDate() == null ? "Select Ending Date" : repository.getEndingDate();
        currentStep = repository.getSetUpCurrentStep();
        listSubjectAndCredits = repository.getSubjectAndCredits();
        currentDay = repository.getCurrentDay();
        semester = repository.getSemester();
        isFirstRun = repository.isAppFirstRun();
        noOfLecturesPerDay = repository.getNoOfLecturesPerDay();
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
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

    public List<String[]> getListSubjectAndCredits() {
        return listSubjectAndCredits;
    }

    public void setListSubjectAndCredits(List<String> listSubjects, List<String> listCredits) {
        repository.setSubjectsInSharedPrefs(listSubjects, listCredits);
        this.listSubjectAndCredits = repository.getSubjectAndCredits();
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        repository.setCurrentDay(currentDay);
        this.currentDay = currentDay;
    }

    public List<String> getSubjectList() {
        return repository.getSubjectsListOnly();
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

    public int getStartingTimeOfLecture(int lectureNo) {
        return repository.getStartTimeForLecture(lectureNo);
    }

    public int getEndingTimeOfLecture(int lectureNo) {
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

    @Override
    public void onEligibleDatesCalculated(final List<Date> eligibleDates) {
        final List<AttendanceEntry> attendanceEntries = new ArrayList<>();
        Logger.d(eligibleDates);
        final LiveData<List<TimetableEntry>> fullTimetable = timetableRepository.getFullTimetable();
        fullTimetable.observeForever(new Observer<List<TimetableEntry>>() {
            @Override
            public void onChanged(List<TimetableEntry> timetableEntries) {
                fullTimetable.removeObserver(this);
                setTimetableForDay(timetableEntries);
                for (Date date :
                        eligibleDates) {
                    List<TimetableEntry> currList = new ArrayList<>();
                    String day = ConverterUtils.getDayOfWeek(date);
                    switch (day) {
                        case "Monday":
                            currList = Monday;
                            break;

                        case "Tuesday":
                            currList = Tuesday;
                            break;

                        case "Wednesday":
                            currList = Wednesday;
                            break;

                        case "Thursday":
                            currList = Thursday;
                            break;

                        case "Friday":
                            currList = Friday;
                            break;
                    }
                    Logger.d("Size of Current List is " + currList.size());
                    for (TimetableEntry x :
                            currList) {
                        AttendanceEntry attendanceEntry = new AttendanceEntry(date, x.getLectureNo(), x.getSubName(), false);
                        attendanceEntries.add(attendanceEntry);
                    }
                    Logger.d("Till date " + date + " Size of attendance is " + attendanceEntries.size());
                }
                AttendanceDatabaseRepository repo = new AttendanceDatabaseRepository(application);
                repo.insertAllAttendanceAndOverallAttendance(attendanceEntries, application);
            }
        });
    }

    public void setTimetableForDay(List<TimetableEntry> timetableEntries) {
        Monday = new ArrayList<>();
        Tuesday = new ArrayList<>();
        Wednesday = new ArrayList<>();
        Thursday = new ArrayList<>();
        Friday = new ArrayList<>();
        for (TimetableEntry x :
                timetableEntries) {
            switch (x.getDay()) {
                case "Monday":
                    Monday.add(x);
                    break;

                case "Tuesday":
                    Tuesday.add(x);
                    break;

                case "Wednesday":
                    Wednesday.add(x);
                    break;

                case "Thursday":
                    Thursday.add(x);
                    break;

                case "Friday":
                    Friday.add(x);
                    break;
            }
        }
        Logger.d("Size of Monday is " + Monday.size());
        Logger.d("Size of Tuesday is " + Tuesday.size());
        Logger.d("Size of Wednesday is " + Wednesday.size());
        Logger.d("Size of Thursday is " + Thursday.size());
        Logger.d("Size of Friday is " + Friday.size());
    }

    public boolean isFirstRun() {
        return isFirstRun;
    }

    public void setFirstRun(boolean firstRun) {
        repository.setAppFirstRun(firstRun);
        isFirstRun = firstRun;
    }

}
