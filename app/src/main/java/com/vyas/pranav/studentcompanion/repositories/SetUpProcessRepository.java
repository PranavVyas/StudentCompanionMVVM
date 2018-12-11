package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDao;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.firebase.HolidaysFetcher;
import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayEntry;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDao;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceDatabase;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class SetUpProcessRepository {

    private static final String SHARED_PREF_ENDING_SEM = "END_SEM_DATE_STRING";
    private static final String SHARED_PREF_STARTING_SEM = "START_SEM_DATE_STRING";
    private static final String SHARED_PREF_CURRENT_STEP = "CURRENT_STEP_IN_SET_UP";
    private static final String SHARED_PREF_SUBJECTS_SET = "SUBJECT_LIST";
    private static final String SHARED_PREF_CURRENT_DAY = "CURRENT_DAY";
    private static final String SHARED_PREF_CURRENT_SEMESTER = "CURRENT_SEMESTER";
    private static final String SHARED_PREF_FIRST_RUN = "IS_FIRST_RUN";


    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private HolidayRepository holidayRepository;
    private OnEligibleDatesCalculatedListener listener;
    private AppExecutors mExecutors;

    public SetUpProcessRepository(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        this.context = context;
        mExecutors = AppExecutors.getInstance();
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

    public void saveHolidaysAndInitAttendance() {
        HolidaysFetcher holidaysFetcher = new HolidaysFetcher();
        holidaysFetcher.setOnHolidaysReceivedListener(new HolidaysFetcher.OnHolidaysReceivedListener() {
            @Override
            public void onHolidaysReceived(List<HolidayEntry> holidayEntries) {
                initHolidayDatabase();
                List<Date> holidayDates = new ArrayList<>();
                for (HolidayEntry x :
                        holidayEntries) {
                    holidayDates.add(x.getDate());
                }
                setHolidays(holidayEntries);
                List<Date> eligibleDates = removeHolidaysAndWeekends(holidayDates);
                if (listener != null) {
                    listener.onEligibleDatesCalculated(eligibleDates);
                }
            }
        });
        holidaysFetcher.getHolidayEntries();
    }

    public void initHolidayDatabase() {
        holidayRepository = new HolidayRepository(context);
    }

    public void setHolidays(List<HolidayEntry> holidayEntries) {
        holidayRepository.setHolidays(holidayEntries);
    }

    public List<Date> removeHolidaysAndWeekends(List<Date> holidayDates) {
        List<Date> allDates = ConverterUtils.getDates(ConverterUtils.convertStringToDate(getStartingDate()), ConverterUtils.convertStringToDate(getEndingDate()));
        for (int i = 0; i < allDates.size(); i++) {
            Date date = allDates.get(i);
            String day = ConverterUtils.getDayOfWeek(date);
            if (holidayDates.contains(date) || day.equals("Saturday") || day.equals("Sunday")) {
                allDates.remove(date);
            }
        }
        return allDates;
    }

    public List<Date> removeHolidays(List<Date> originalDates, List<Date> holidays) {
        List<Date> result = new ArrayList<>();
        for (Date x :
                originalDates) {
            if (!holidays.contains(x)) {
                result.add(x);
            }
        }
        return result;
    }

    public void setOnEligibleDatesCalculatedListener(OnEligibleDatesCalculatedListener listener) {
        this.listener = listener;
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
                    int presentDays = attendanceDao.getAttendedDaysForSubject(subject, ConverterUtils.convertStringToDate(Constants.SEM_START_DATE_STR), todayDate);
                    int bunkedDays = attendanceDao.getBunkedDaysForSubject(subject, ConverterUtils.convertStringToDate(Constants.SEM_START_DATE_STR), new Date());
                    int totalDays = attendanceDao.getTotalDaysForSubject(subject);
                    x.setTotalDays(totalDays);
                    x.setBunkedDays(bunkedDays);
                    x.setPresentDays(presentDays);
                    x.setCredits(credits);
                    x.setSubName(subject);
                    overallAttendanceDao.insertOverall(x);
                }
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

    public boolean isAppFirstRun() {
        return preferences.getBoolean(SHARED_PREF_FIRST_RUN, true);
    }

    public void setAppFirstRun(boolean isAppFirstRun) {
        editor.putBoolean(SHARED_PREF_FIRST_RUN, isAppFirstRun);
        editor.apply();
    }

    public interface OnEligibleDatesCalculatedListener {
        void onEligibleDatesCalculated(List<Date> eligibleDates);
    }
}
