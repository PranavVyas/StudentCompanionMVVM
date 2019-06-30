package com.vyas.pranav.studentcompanion.jobs;

import androidx.annotation.NonNull;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.repositories.AppSettingsRepository;
import com.vyas.pranav.studentcompanion.repositories.OverallAttendanceRepository;

import java.util.concurrent.TimeUnit;

public class DailyJobForEditOverallAttendance extends DailyJob {
    static final String TAG = "DailyJobForEditOverallAttendance";

    public static void scheduleJob() {
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
        if (repo.isSmartSilentEnabled()) {
            repo.enableAutoSilentDevice();
        }
        return DailyJobResult.SUCCESS;
    }
}
