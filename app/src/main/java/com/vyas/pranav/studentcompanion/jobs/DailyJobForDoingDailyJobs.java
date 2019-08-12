package com.vyas.pranav.studentcompanion.jobs;
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
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.repositories.AppSettingsRepository;
import com.vyas.pranav.studentcompanion.repositories.OverallAttendanceRepository;
import com.vyas.pranav.studentcompanion.repositories.TimetableRepository;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DailyJobForDoingDailyJobs extends DailyJob {
    static final String TAG = "DailyJobForDoingDailyJobs";

    public static void scheduleJob() {
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            Logger.d("Already Job Set Skipping Setting Of Job Now...");
            return;
        }
        Logger.d("Job was not started ,Starting Now...");
        JobRequest.Builder jobBuilder = new JobRequest.Builder(TAG).setUpdateCurrent(true);
        long startTime = TimeUnit.HOURS.toMillis(0) + TimeUnit.MINUTES.toMillis(10);
        long endTime = startTime + TimeUnit.MINUTES.toMillis(30);
        DailyJob.schedule(jobBuilder, startTime, endTime);
    }

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {
        getContext();
        OverallAttendanceRepository overallAttendanceRepository = OverallAttendanceRepository.getInstance(getContext());
        overallAttendanceRepository.refreshAllOverallAttendance();
        AppSettingsRepository repo = AppSettingsRepository.getInstance(getContext());
        repo.toggleSmartSilent();
        if (repo.isAutoAttendanceEnabled()) {
            TimetableRepository timetableRepository = TimetableRepository.getInstance(getContext());
            LiveData<List<TimetableEntry>> timetableForDay =
                    timetableRepository.getTimetableForDay(ConverterUtils.getDayOfWeek(new Date()));
            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    timetableForDay.observeForever(new Observer<List<TimetableEntry>>() {
                        @Override
                        public void onChanged(List<TimetableEntry> timetableEntries) {
                            timetableForDay.removeObserver(this);
                            for (int i = 0; i < timetableEntries.size(); i++) {

                            }
                        }
                    });
                }
            });
        }
        return DailyJobResult.SUCCESS;
    }
}
