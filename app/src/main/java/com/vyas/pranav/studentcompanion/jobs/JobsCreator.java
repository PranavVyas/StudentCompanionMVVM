package com.vyas.pranav.studentcompanion.jobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class JobsCreator implements JobCreator {
    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case DailyJobForDoingDailyJobs.TAG:
                return new DailyJobForDoingDailyJobs();

            case DailyJobForSilentAction.TAG:
                return new DailyJobForSilentAction();

            case DailyJobForShowingReminder.TAG:
                return new DailyJobForShowingReminder();

            case DailyJobForRefreshGeoFence.TAG:
                return new DailyJobForRefreshGeoFence();

            case DailyJobForUnsilentAction.TAG:
                return new DailyJobForUnsilentAction();
        }
        return null;
    }
}
