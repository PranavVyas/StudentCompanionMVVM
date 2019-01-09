package com.vyas.pranav.studentcompanion.services;

import android.app.IntentService;
import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.jobs.DailyJobForShowingReminder;
import com.vyas.pranav.studentcompanion.repositories.AttendanceDatabaseRepository;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class AttendanceEditIntentService extends IntentService {

    private static final String TAG = "AttendanceEditIntentSer";

    public AttendanceEditIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.cancel(DailyJobForShowingReminder.RC_SHOW_NOTIFICATION);
        AttendanceDatabaseRepository repository = new AttendanceDatabaseRepository(getApplicationContext());
        Date date = new Date();
        ConverterUtils.CustomDate customDate = ConverterUtils.extractElementsFromDate(date);
        Date date1 = ConverterUtils.formatDateFromCalender(customDate.getDate(), customDate.getMonth(), customDate.getYear());
        LiveData<List<AttendanceEntry>> attendanceForDate = repository.getAttendanceForDate(date1);
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                attendanceForDate.observeForever(new Observer<List<AttendanceEntry>>() {
                    @Override
                    public void onChanged(List<AttendanceEntry> attendanceEntries) {
                        attendanceForDate.removeObserver(this);
                        Logger.d("Marking all present now");
                        for (int i = 0; i < attendanceEntries.size(); i++) {
                            AttendanceEntry attendanceEntry = attendanceEntries.get(i);
                            if (!attendanceEntry.isPresent()) {
                                attendanceEntry.setPresent(true);
                            }
                            repository.updateAttendance(attendanceEntry);
                        }
                    }
                });
            }
        });
    }
}
