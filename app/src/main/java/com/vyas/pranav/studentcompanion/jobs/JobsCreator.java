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
