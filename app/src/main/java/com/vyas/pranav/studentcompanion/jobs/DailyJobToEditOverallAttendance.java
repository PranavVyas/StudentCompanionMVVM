package com.vyas.pranav.studentcompanion.jobs;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.repositories.AppSettingsRepository;
import com.vyas.pranav.studentcompanion.repositories.OverallAttendanceRepository;
import com.vyas.pranav.studentcompanion.repositories.TimetableRepository;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

public class DailyJobToEditOverallAttendance extends DailyJob {
    static final String TAG = "DailyJobToEditOverallAt";
    private static int RC_OPEN_APP = 100;
    private static int RC_SHOW_NOTIFICATION = 1100;

    public static void scheduleJob() {
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            Logger.d("Already Job Set Skipping Setting Of Job Now...");
            return;
        }
        Logger.d("Job was not started ,Starting Now...");
        JobRequest.Builder jobBuilder = new JobRequest.Builder(TAG).setUpdateCurrent(true);
        long startTime = TimeUnit.HOURS.toMillis(0) + TimeUnit.MINUTES.toMillis(10);
        long endTime = startTime + TimeUnit.MINUTES.toMillis(1);
        DailyJob.schedule(jobBuilder, startTime, endTime);
    }

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {
        getContext();
        OverallAttendanceRepository overallAttendanceRepository = new OverallAttendanceRepository(getContext());
        overallAttendanceRepository.refreshAllOverallAttendance();
        AppSettingsRepository repo = new AppSettingsRepository(getContext());
        TimetableRepository timetableRepository = new TimetableRepository(getContext());
        boolean isAutoAttendanceEnabled = repo.isAutoAttendanceEnabled();
        if (isAutoAttendanceEnabled) {
            repo.enableAutoAttendanceForToday();
        }
        return DailyJobResult.SUCCESS;
    }
}
