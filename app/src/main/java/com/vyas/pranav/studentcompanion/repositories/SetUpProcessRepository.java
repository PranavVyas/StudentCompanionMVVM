package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceDao;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayEntry;
import com.vyas.pranav.studentcompanion.data.holidaydatabase.firebase.model.HolidayModel;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.AttendanceUtils;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SetUpProcessRepository {

    private Context context;
    //    private SharedPreferences preferences;
//    private SharedPreferences.Editor editor;
    private HolidayRepository holidayRepository;
    private TimetableRepository timetableRepository;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private MainDatabase mDb;
    private static final Object LOCK = new Object();

    private List<TimetableEntry> Monday;
    private List<TimetableEntry> Tuesday;
    private List<TimetableEntry> Wednesday;
    private List<TimetableEntry> Thursday;
    private List<TimetableEntry> Friday;

    private static SetUpProcessRepository instance;

    public SetUpProcessRepository(Context context) {
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(context.getApplicationContext());
        this.context = context;
        holidayRepository = HolidayRepository.getInstance(context);
        timetableRepository = TimetableRepository.getInstance(context);
        mDb = MainDatabase.getInstance(context);
    }

    public static SetUpProcessRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new SetUpProcessRepository(context);
            }
        }
        return instance;
    }

    public FirestoreQueryLiveData getCollagesLiveData(Query query) {
        return new FirestoreQueryLiveData(query);
    }


    public boolean isAppFirstRun() {
        return sharedPreferencesUtils.isAppFirstRun();
    }

    public void setAppFirstRun(boolean isAppFirstRun) {
        sharedPreferencesUtils.setAppFirstRun(isAppFirstRun);
    }

    public void setUpEndingDate(String endDateStr) {
        sharedPreferencesUtils.setUpEndingDate(endDateStr);
    }

    public void setUpStartingDate(String startDateStr) {
        sharedPreferencesUtils.setUpStartingDate(startDateStr);
    }

    public String getStartingDate() {
        return sharedPreferencesUtils.getStartingDate();
    }

    public String getEndingDate() {
        return sharedPreferencesUtils.getEndingDate();
    }

    public void setUpCurrentStep(int step) {
        sharedPreferencesUtils.setUpCurrentStep(step);
    }

    public int getSetUpCurrentStep() {
        return sharedPreferencesUtils.getSetUpCurrentStep();
    }

    public void setSubjectListInSharedPrefrences(List<String> subjects) {
        sharedPreferencesUtils.setSubjectListInSharedPrefrences(subjects);
    }

    public List<String> getSubjectList() {
        return sharedPreferencesUtils.getSubjectList();
    }

    public int getCurrentDay() {
        return sharedPreferencesUtils.getCurrentDay();
    }

    public void setCurrentDay(int currentDay) {
        sharedPreferencesUtils.setCurrentDay(currentDay);
    }

    public void setUpSemester(int semester) {
        sharedPreferencesUtils.setUpSemester(semester);
    }

    public int getSemester() {
        return sharedPreferencesUtils.getSemester();
    }

    public void setLectureStartTimeInSharedPrefs(int lectureNo, int startTime) {
        sharedPreferencesUtils.setLectureStartTimeInSharedPrefs(lectureNo, startTime);
    }

    public void setLectureEndTimeInSharedPrefs(int lectureNo, int endTime) {
        sharedPreferencesUtils.setLectureEndTimeInSharedPrefs(lectureNo, endTime);
    }

    public int getStartTimeForLecture(int lectureNo) {
        return sharedPreferencesUtils.getStartTimeForLecture(lectureNo);
    }

    public int getEndTimeForLecture(int lectureNo) {
        return sharedPreferencesUtils.getEndTimeForLecture(lectureNo);
    }

    public int getNoOfLecturesPerDay() {
        return sharedPreferencesUtils.getNoOfLecturesPerDay();
    }

    public void setNoOfLecturesPerDay(int noOfLecturesPerDay) {
        sharedPreferencesUtils.setNoOfLecturesPerDay(noOfLecturesPerDay);
    }

    public void saveHolidaysAndInitAttendance() {
//        HolidaysFetcher holidaysFetcher = new HolidaysFetcher();
        FirestoreQueryLiveData holidayLiveData = new FirestoreQueryLiveData(FirebaseFirestore.getInstance().collection(getCurrentPath() + Constants.PATH_HOLIDAYS_SVNIT));
        holidayLiveData.observeForever(queryDocumentSnapshots -> {
            List<HolidayModel> holidayModels = queryDocumentSnapshots.toObjects(HolidayModel.class);
            List<Date> holidayDates = new ArrayList<>();
            List<HolidayEntry> holidayEntries = new ArrayList<>();
            for (HolidayModel x :
                    holidayModels) {
                HolidayEntry holiday = new HolidayEntry();
                holiday.setName(x.getName());
                holiday.setDate(ConverterUtils.convertStringToDate(x.getDate()));
                holiday.setDay(ConverterUtils.getDayOfWeek(x.getDate()));
                holidayDates.add(holiday.getDate());
                holidayEntries.add(holiday);
            }
            setHolidays(holidayEntries);
            List<Date> eligibleDates = removeHolidaysAndWeekends(holidayDates);
            setUpAutoAttendanceDatabase();
            eligibleDatesCalculated(eligibleDates);
        });

//        holidaysFetcher.setOnHolidaysReceivedListener(new HolidaysFetcher.OnHolidaysReceivedListener() {
//            @Override
//            public void onHolidaysReceived(List<HolidayEntry> holidayEntries) {
//
//            }
//        });
//        holidaysFetcher.getHolidayEntries();


    }

    private void eligibleDatesCalculated(List<Date> eligibleDates) {
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
                AttendanceDatabaseRepository repo = AttendanceDatabaseRepository.getInstance(context);
                repo.insertAllAttendanceAndOverallAttendance(attendanceEntries, context);
            }
        });
    }

    private void setTimetableForDay(List<TimetableEntry> timetableEntries) {
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


    private void setHolidays(List<HolidayEntry> holidayEntries) {
        holidayRepository.setHolidays(holidayEntries);
    }

    private List<Date> removeHolidaysAndWeekends(List<Date> holidayDates) {
        List<Date> allDates = AttendanceUtils.getDates(ConverterUtils.convertStringToDate(getStartingDate()), ConverterUtils.convertStringToDate(getEndingDate()));
        List<Date> resultDates = new ArrayList<>();
        for (int i = 0; i < allDates.size(); i++) {
            Date date = allDates.get(i);
            String day = ConverterUtils.getDayOfWeek(date);
            if (!holidayDates.contains(date) && !day.equals("Saturday") && !day.equals("Sunday")) {
                resultDates.add(date);
            }
        }
        return resultDates;
    }

    public void initializeOverallAttendance() {
        AttendanceDao attendanceDao = mDb.attendanceDao();
        OverallAttendanceDao overallAttendanceDao = mDb.overallAttendanceDao();
        insertAllOverallAttendance(getSubjectList(), attendanceDao, overallAttendanceDao);
    }

    private void insertAllOverallAttendance(final List<String> subjectList, final AttendanceDao attendanceDao, final OverallAttendanceDao overallAttendanceDao) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            for (int i = 0; i < subjectList.size(); i++) {
                String subject = subjectList.get(i);
                OverallAttendanceEntry x = new OverallAttendanceEntry();
                Date todayDate = new Date();
                int presentDays = attendanceDao.getAttendedDaysForSubject(subject, ConverterUtils.convertStringToDate(getStartingDate()), todayDate);
                int bunkedDays = attendanceDao.getBunkedDaysForSubject(subject, ConverterUtils.convertStringToDate(getStartingDate()), new Date());
                int totalDays = attendanceDao.getTotalDaysForSubject(subject);
                x.setTotalDays(totalDays);
                x.setBunkedDays(bunkedDays);
                x.setPresentDays(presentDays);
                x.setSubName(subject);
                overallAttendanceDao.insertOverall(x);
//                    DailyJobForDoingDailyJobs.scheduleJob();
            }
            Logger.d("Overall Attendance Database Init success");
        });
    }

    private void setUpAutoAttendanceDatabase() {
        final AutoAttendancePlaceDao autoAttendancePlaceDao = mDb.autoAttendancePlaceDao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<String> subjectsListOnly = getSubjectList();
            for (int i = 0; i < subjectsListOnly.size(); i++) {
                String subject = subjectsListOnly.get(i);
                AutoAttendancePlaceEntry autoAttendancePlaceEntry = new AutoAttendancePlaceEntry();
                autoAttendancePlaceEntry.setSubject(subject);
                autoAttendancePlaceEntry.setLat(Constants.DEFAULT_LAT);
                autoAttendancePlaceEntry.setLang(Constants.DEFAULT_LANG);
                autoAttendancePlaceDao.insertNewPlaceEntry(autoAttendancePlaceEntry);
            }
        });
    }

    public boolean isTutorialDone() {
        return sharedPreferencesUtils.isTutorialDone();
    }

    public void setTutorialDone(boolean isDone) {
        sharedPreferencesUtils.setTutorialDone(isDone);
    }

    public int getCurrentAttendanceCriteria() {
        return sharedPreferencesUtils.getCurrentAttendanceCriteria();
    }

    public void setCurrentAttendanceCriteria(int progress) {
        sharedPreferencesUtils.setCurrentAttendanceCriteria(progress);
    }

    public String getCurrentPath() {
        return sharedPreferencesUtils.getCurrentPath();
    }

    public void setCurrentPath(String currentPath) {
        sharedPreferencesUtils.setCurrentPath(currentPath);
    }
}
