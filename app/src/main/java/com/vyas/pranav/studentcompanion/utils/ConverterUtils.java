package com.vyas.pranav.studentcompanion.utils;
/*
Student Companion - An Android App that has features like attendance manager, note manager etc
Copyright (C) 2019  Pranav Vyas

This file is a part of Student Companion.

Student Companion is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Student Companion is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.
*/
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ConverterUtils {

    private static final String TAG = "ConverterUtils";

    /**
     * Convert string to date.
     *
     * @param dateString the date string
     * @return the date
     */
    public static Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Date date = new Date();
        try {
            date = dateFormatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Convert date to string string.
     *
     * @param date the date
     * @return the string
     */
    public static String convertDateToString(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return dateFormatter.format(date);
    }

    /**
     * Format date string from calender string.
     * Sometimes when the strings is in format d/M/yyyy to convert it in standard dd/MM/yyyy format
     *
     * @param date  the date
     * @param month the month
     * @param year  the year
     * @return the string
     */
    public static String formatDateStringFromCalender(int date, int month, int year) {
        String dateS, monthS;
        if ((date <= 9)) {
            dateS = 0 + "" + date;
        } else {
            dateS = "" + date;
        }
        if ((month <= 9)) {
            monthS = 0 + "" + month;
        } else {
            monthS = "" + month;
        }
        return dateS + "/" + monthS + "/" + year;
    }

    public static Date formatDateFromCalender(int date, int month, int year) {
        String dateS, monthS;
        if ((date <= 9)) {
            dateS = 0 + "" + date;
        } else {
            dateS = "" + date;
        }
        if ((month <= 9)) {
            monthS = 0 + "" + month;
        } else {
            monthS = "" + month;
        }
        String dateStr = dateS + "/" + monthS + "/" + year;
        return convertStringToDate(dateStr);
    }

    /**
     * Gets day of week.
     *
     * @param dateString the date string
     * @return the day of week (like Monday,Tuesday etc..)
     */
    public static String getDayOfWeek(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.US);
        Date d = convertStringToDate(dateString);
        return sdf.format(d);
    }

    /**
     * Gets day of week.
     *
     * @param date the date
     * @return the day of week (like Monday,Tuesday etc..)
     */
    public static String getDayOfWeek(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.US);
        return sdf.format(date);
    }

    /**
     * Extract elements from date custom date.
     *
     * @param date the date
     * @return the custom date (contains various helper variables)
     */
    public static CustomDate extractElementsFromDate(Date date) {
        int day = Integer.parseInt((String) DateFormat.format("dd", date));
        int monthNumber = Integer.parseInt((String) DateFormat.format("MM", date));
        int year = Integer.parseInt((String) DateFormat.format("yyyy", date));
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(GregorianCalendar.DAY_OF_MONTH, day);
        calendar.set(GregorianCalendar.MONTH, monthNumber - 1);
        calendar.set(GregorianCalendar.YEAR, year);
        int dayOfYear = calendar.get(GregorianCalendar.DAY_OF_YEAR);
        return new CustomDate(day, monthNumber, year, dayOfYear);
    }

    /**
     * Convert time in int int.
     *
     * @param timeStr the time str
     * @return the int
     */
    public static int convertTimeInInt(String timeStr) {
        String[] newStr = timeStr.split(":");
        int min = Integer.valueOf(newStr[1]);
        int hour = Integer.valueOf(newStr[0]);
        return ((hour * 60) + min);
    }

    /**
     * Convert time int in string string.
     * Used to perform various operations using the time preference obtained from custom TimePreference
     *
     * @param time the time
     * @return the string
     */
    public static String convertTimeIntInString(int time) {
        CustomTime customTime = extractHourAndMinFromTime(time);
        int hour = customTime.getHour();
        int min = customTime.getMin();
        String hourStr = ((hour < 10) ? "0" + hour : "" + hour);
        String minStr = ((min < 10) ? "0" + min : "" + min);

        return hourStr + ":" + minStr;
    }

    /**
     * Extract hour and min from int time.
     *
     * @param time the time
     * @return the custom time (Object that contains various helper variables)
     */
    public static CustomTime extractHourAndMinFromTime(int time) {
        int hour = time / 60;
        int min = time % 60;
        return new CustomTime(min, hour);
    }

    /**
     * Extract hour and min from time custom time.
     *
     * @param timeStr the time str
     * @return the custom time
     */
    public static CustomTime extractHourAndMinFromTime(String timeStr) {
        int time = convertTimeInInt(timeStr);
        int hour = time / 60;
        int min = time % 60;
        return new CustomTime(min, hour);
    }

    public static String getDayFromInt(int day) {
        switch (day) {
            case 1:
                return "Monday";

            case 2:
                return "Tuesday";

            case 3:
                return "Wednesday";

            case 4:
                return "Thursday";

            case 5:
                return "Friday";

            default:
                return "Error";
        }
    }

    /**
     * The type Custom date.
     */
    public static class CustomDate {
        private int date;
        private int month;
        private int year;
        private int dayOfYear;

        public CustomDate(int date, int month, int year) {
            this.date = date;
            this.month = month;
            this.year = year;
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(GregorianCalendar.DAY_OF_MONTH, date);
            calendar.set(GregorianCalendar.MONTH, month - 1);
            calendar.set(GregorianCalendar.YEAR, year);
            this.dayOfYear = calendar.get(GregorianCalendar.DAY_OF_YEAR);
        }

        /**
         * Instantiates a new Custom date.
         *
         * @param date      the date
         * @param month     the month
         * @param year      the year
         * @param dayOfYear the day of year(like 12,65,etc...)
         */
        CustomDate(int date, int month, int year, int dayOfYear) {
            this.date = date;
            this.month = month;
            this.year = year;
            this.dayOfYear = dayOfYear;
        }

        /**
         * Gets date.
         *
         * @return the date
         */
        public int getDate() {
            return date;
        }

        /**
         * Sets date.
         *
         * @param date the date
         */
        public void setDate(int date) {
            this.date = date;
        }

        /**
         * Gets month.
         *
         * @return the month
         */
        public int getMonth() {
            return month;
        }

        /**
         * Sets month.
         *
         * @param month the month
         */
        public void setMonth(int month) {
            this.month = month;
        }

        /**
         * Gets year.
         *
         * @return the year
         */
        public int getYear() {
            return year;
        }


        /**
         * Sets year.
         *
         * @param year the year
         */
        public void setYear(int year) {
            this.year = year;
        }

        /**
         * Gets day of year.
         *
         * @return the day of year
         */
        public int getDayOfYear() {
            return dayOfYear;
        }

        /**
         * Sets day of year.
         *
         * @param dayOfYear the day of year
         */
        public void setDayOfYear(int dayOfYear) {
            this.dayOfYear = dayOfYear;
        }
    }

    /**
     * The type Custom time.
     */
    public static class CustomTime {
        private int min;
        private int hour;

        /**
         * Instantiates a new Custom time.
         *
         * @param min  the min
         * @param hour the hour
         */
        CustomTime(int min, int hour) {
            this.min = min;
            this.hour = hour;
        }

        /**
         * Gets min.
         *
         * @return the min
         */
        int getMin() {
            return min;
        }

        /**
         * Sets min.
         *
         * @param min the min
         */
        public void setMin(int min) {
            this.min = min;
        }

        /**
         * Gets hour.
         *
         * @return the hour
         */
        int getHour() {
            return hour;
        }

        /**
         * Sets hour.
         *
         * @param hour the hour
         */
        public void setHour(int hour) {
            this.hour = hour;
        }
    }

    public static long convertTimeInInt(int hour, int min) {
        return TimeUnit.HOURS.toMillis(hour) + TimeUnit.MINUTES.toMillis(min);
    }

//    public static long convertTimeStringInMillis(String timeStr) {
//        String[] newStr = timeStr.split(":");
//        int min = Integer.valueOf(newStr[1]);
//        int hour = Integer.valueOf(newStr[0]);
//        return (TimeUnit.HOURS.toMillis(hour) + TimeUnit.MINUTES.toMillis(min));
//    }

    public static long getCurrentTimeInMillis() {
        Calendar now = GregorianCalendar.getInstance();
        return TimeUnit.HOURS.toMillis(now.get(Calendar.HOUR_OF_DAY)) + TimeUnit.MINUTES.toMillis(now.get(Calendar.MINUTE)) + TimeUnit.SECONDS.toMillis(now.get(Calendar.SECOND));
    }
}
