package com.vyas.pranav.studentcompanion.jobs;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

import static com.vyas.pranav.studentcompanion.utils.ConverterUtils.getCurrentTimeInMillis;

public class DailyJobForAttendance extends Job {

    public static final String TAG = "DailyJobForAttendance";
    private static final int RC_SHOW_NOTIFICATION = 54654654;
    private static final int RC_OPEN_APP = 4645;

    public static void scheduleAutoAttendanceJobAtTime(long time) {
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        long currentTimeInMillis = getCurrentTimeInMillis();
        long timeInMillis = time - currentTimeInMillis;
        Logger.d("Time - current time = " + time + " - " + currentTimeInMillis + " = " + timeInMillis);
        if (timeInMillis < 0) {
            timeInMillis = (TimeUnit.HOURS.toMillis(24) + time) - currentTimeInMillis;
            Logger.d("New ()()() Time - current time = " + time + " - " + currentTimeInMillis + " + " + TimeUnit.HOURS.toMillis(24) + "= " + timeInMillis);
        }
        new JobRequest.Builder(TAG).setUpdateCurrent(false).setExact(timeInMillis).build().schedule();
    }

    public static void cancelAllAutoAttendanceJobs() {
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            JobManager.instance().cancelAllForTag(TAG);
        }
    }

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        return Result.SUCCESS;
    }

}
