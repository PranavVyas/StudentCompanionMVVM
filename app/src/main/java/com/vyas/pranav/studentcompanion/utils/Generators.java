package com.vyas.pranav.studentcompanion.utils;

import java.util.Date;

public class Generators {

    /**
     * Generate id for individual attendance string.
     *
     * @param date      the date
     * @param lectureNo the lecture no
     * @return the string
     */
    public static String generateIdForIndividualAttendance(Date date, int lectureNo) {
        return ConverterUtils.convertDateToString(date) + lectureNo;
    }

    public static String generateIdForIndividualAttendance(String dateStr, int lectureNo) {
        return dateStr + lectureNo;
    }

    public static String generateIdForTimetableEntry(int lectureNo, int semester, int dayNo) {
        return semester + "" + dayNo + "" + lectureNo;
    }
}
