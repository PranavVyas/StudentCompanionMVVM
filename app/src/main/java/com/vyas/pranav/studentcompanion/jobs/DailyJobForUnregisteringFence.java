package com.vyas.pranav.studentcompanion.jobs;

import androidx.annotation.NonNull;

import com.evernote.android.job.Job;

public class DailyJobForUnregisteringFence extends Job {

    private static final String TAG = "DailyJobForUnregisteringFence";

    public static void cancelAllJobs() {

    }

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        return null;
    }
}
