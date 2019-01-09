package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceDao;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlacesDatabase;
import com.vyas.pranav.studentcompanion.data.firebase.HolidaysFetcher;
import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayEntry;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.jobs.DailyJobToEditOverallAttendance;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;

public class SetUpProcessRepository {

    private static final String SHARED_PREF_ENDING_SEM = "END_SEM_DATE_STRING";
    private static final String SHARED_PREF_STARTING_SEM = "START_SEM_DATE_STRING";
    private static final String SHARED_PREF_CURRENT_STEP = "CURRENT_STEP_IN_SET_UP";
    private static final String SHARED_PREF_SUBJECTS_SET = "SUBJECT_LIST";
    private static final String SHARED_PREF_CURRENT_DAY = "CURRENT_DAY";
    private static final String SHARED_PREF_CURRENT_SEMESTER = "CURRENT_SEMESTER";
    private static final String SHARED_PREF_FIRST_RUN = "IS_FIRST_RUN";
    private static final String SHARED_PREF_NO_OF_LECTURES_PER_DAY = "NO_OF_LECTURES_PER_DAY";
    private static final String SHARED_PREF_LECTURE_START = "STARTING_TIME_OF_LECTURE";
    private static final String SHARED_PREF_LECTURE_END = "ENDING_TIME_OF_LECTURE";

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private HolidayRepository holidayRepository;
    private TimetableRepository timetableRepository;

    private List<TimetableEntry> Monday;
    private List<TimetableEntry> Tuesday;
    private List<TimetableEntry> Wednesday;
    private List<TimetableEntry> Thursday;
    private List<TimetableEntry> Friday;

    public SetUpProcessRepository(Context context) {
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        if (context == null) {
            Logger.d("Context is empty in SetUpRepository");
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        this.context = context;
        holidayRepository = new HolidayRepository(context);
        timetableRepository = new TimetableRepository(context);
    }

    public boolean isAppFirstRun() {
        return preferences.getBoolean(SHARED_PREF_FIRST_RUN, true);
    }

    public void setAppFirstRun(boolean isAppFirstRun) {
        editor.putBoolean(SHARED_PREF_FIRST_RUN, isAppFirstRun);
        editor.apply();
    }

    public void setUpEndingDate(String endDateStr) {
        editor.putString(SHARED_PREF_ENDING_SEM, endDateStr);
        editor.apply();
    }

    public void setUpStartingDate(String startDateStr) {
        editor.putString(SHARED_PREF_STARTING_SEM, startDateStr);
        editor.apply();
    }

    public String getStartingDate() {
        return preferences.getString(SHARED_PREF_STARTING_SEM, null);
    }

    public String getEndingDate() {
        return preferences.getString(SHARED_PREF_ENDING_SEM, null);
    }

    public void setUpCurrentStep(int step) {
        editor.putInt(SHARED_PREF_CURRENT_STEP, step);
        editor.apply();
    }

    public int getSetUpCurrentStep() {
        return preferences.getInt(SHARED_PREF_CURRENT_STEP, 1);
    }

    public void setSubjectsInSharedPrefs(List<String> subjects, List<String> credits) {
        List<String> finalList = new ArrayList<>();
        for (int i = 0; i < subjects.size(); i++) {
            String temp = subjects.get(i) + "::" + credits.get(i);
            finalList.add(temp);
        }
        editor.putStringSet(SHARED_PREF_SUBJECTS_SET, new HashSet<String>(finalList));
        editor.apply();
    }

    public List<String[]> getSubjectAndCredits() {
        List<String[]> newTemp = new ArrayList<>();
        List<String> subjectAndCredits = new ArrayList<>(preferences.getStringSet(SHARED_PREF_SUBJECTS_SET, new HashSet<String>()));
        if (subjectAndCredits.isEmpty()) {
            return new ArrayList<>();
        } else {
            for (String x :
                    subjectAndCredits) {
                String[] entry = x.split("::");
                newTemp.add(entry);
            }
            return newTemp;
        }
    }

    public int getCurrentDay() {
        return preferences.getInt(SHARED_PREF_CURRENT_DAY, 1);
    }

    public void setCurrentDay(int currentDay) {
        editor.putInt(SHARED_PREF_CURRENT_DAY, currentDay);
        editor.apply();
    }

    public List<String> getSubjectsListOnly() {
        List<String[]> subjectAndCredits = getSubjectAndCredits();
        List<String> result = new ArrayList<>();
        for (String[] x :
                subjectAndCredits) {
            result.add(x[0]);
        }
        return result;
    }

    public void setUpSemester(int semester) {
        editor.putInt(SHARED_PREF_CURRENT_SEMESTER, semester);
        editor.apply();
    }

    public int getSemester() {
        return preferences.getInt(SHARED_PREF_CURRENT_SEMESTER, 1);
    }

    public void setLectureStartTimeInSharedPrefs(int lectureNo, int startTime) {
        editor.putInt(SHARED_PREF_LECTURE_START + lectureNo, startTime);
        editor.apply();
    }

    public void setLectureEndTimeInSharedPrefs(int lectureNo, int endTime) {
        editor.putInt(SHARED_PREF_LECTURE_END + lectureNo, endTime);
        editor.apply();
    }

    public int getStartTimeForLecture(int lectureNo) {
        return preferences.getInt(SHARED_PREF_LECTURE_START + lectureNo, 0);
    }

    public int getEndTimeForLecture(int lectureNo) {
        return preferences.getInt(SHARED_PREF_LECTURE_END + lectureNo, 60);
    }

    public int getNoOfLecturesPerDay() {
        return preferences.getInt(SHARED_PREF_NO_OF_LECTURES_PER_DAY, 4);
    }

    public void setNoOfLecturesPerDay(int noOfLecturesPerDay) {
        editor.putInt(SHARED_PREF_NO_OF_LECTURES_PER_DAY, noOfLecturesPerDay);
        editor.apply();
    }

    public void saveHolidaysAndInitAttendance() {
        HolidaysFetcher holidaysFetcher = new HolidaysFetcher();
        holidaysFetcher.setOnHolidaysReceivedListener(new HolidaysFetcher.OnHolidaysReceivedListener() {
            @Override
            public void onHolidaysReceived(List<HolidayEntry> holidayEntries) {
                List<Date> holidayDates = new ArrayList<>();
                for (HolidayEntry x :
                        holidayEntries) {
                    holidayDates.add(x.getDate());
                }
                setHolidays(holidayEntries);
                List<Date> eligibleDates = removeHolidaysAndWeekends(holidayDates);
                setUpAutoAttendanceDatabase();
                eligibleDatesCalculated(eligibleDates);
            }
        });
        holidaysFetcher.getHolidayEntries();
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
                AttendanceDatabaseRepository repo = new AttendanceDatabaseRepository(context);
                repo.insertAllAttendanceAndOverallAttendance(attendanceEntries, context);
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


    public void setHolidays(List<HolidayEntry> holidayEntries) {
        holidayRepository.setHolidays(holidayEntries);
    }

    public List<Date> removeHolidaysAndWeekends(List<Date> holidayDates) {
        List<Date> allDates = ConverterUtils.getDates(ConverterUtils.convertStringToDate(getStartingDate()), ConverterUtils.convertStringToDate(getEndingDate()));
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
        AttendanceDao attendanceDao = AttendanceDatabase.getInstance(context).attendanceDao();
        OverallAttendanceDao overallAttendanceDao = OverallAttendanceDatabase.getInstance(context).overallAttendanceDao();
        insertAllOverallAttendance(getSubjectsListOnly(), attendanceDao, overallAttendanceDao);
    }

    public void insertAllOverallAttendance(final List<String> subjectList, final AttendanceDao attendanceDao, final OverallAttendanceDao overallAttendanceDao) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < subjectList.size(); i++) {
                    String subject = subjectList.get(i);
                    int credits = getCreditsForSubject(subject);
                    OverallAttendanceEntry x = new OverallAttendanceEntry();
                    Date todayDate = new Date();
                    int presentDays = attendanceDao.getAttendedDaysForSubject(subject, ConverterUtils.convertStringToDate(getStartingDate()), todayDate);
                    int bunkedDays = attendanceDao.getBunkedDaysForSubject(subject, ConverterUtils.convertStringToDate(getStartingDate()), new Date());
                    int totalDays = attendanceDao.getTotalDaysForSubject(subject);
                    x.setTotalDays(totalDays);
                    x.setBunkedDays(bunkedDays);
                    x.setPresentDays(presentDays);
                    x.setCredits(credits);
                    x.setSubName(subject);
                    overallAttendanceDao.insertOverall(x);
                    DailyJobToEditOverallAttendance.scheduleJob();
                }
                Logger.d("Overall Attendance Database Init success");
            }
        });
    }

    private int getCreditsForSubject(String subject) {
        List<String> subjects = new ArrayList<>();
        List<String> credits = new ArrayList<>();
        List<String[]> subjectAndCredits = getSubjectAndCredits();
        for (int i = 0; i < subjectAndCredits.size(); i++) {
            String[] array = subjectAndCredits.get(i);
            String subjectFromArray = array[0];
            subjects.add(subjectFromArray);
            String credit = array[1];
            credits.add(credit);
        }
        int index = subjects.indexOf(subject);
        return Integer.parseInt(credits.get(index));
    }

    public void setUpAutoAttendanceDatabase() {
        final AutoAttendancePlaceDao autoAttendancePlaceDao = AutoAttendancePlacesDatabase.getInstance(context).autoAttendancePlaceDao();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<String> subjectsListOnly = getSubjectsListOnly();
                for (int i = 0; i < subjectsListOnly.size(); i++) {
                    String subject = subjectsListOnly.get(i);
                    AutoAttendancePlaceEntry autoAttendancePlaceEntry = new AutoAttendancePlaceEntry();
                    autoAttendancePlaceEntry.setPlaceId(Constants.DEFAULT_PLACE_ID);
                    autoAttendancePlaceEntry.setSubject(subject);
                    autoAttendancePlaceDao.insertNewPlaceId(autoAttendancePlaceEntry);
                }
            }
        });

    }
}
