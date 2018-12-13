package com.vyas.pranav.studentcompanion.jobs;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.repositories.TimetableRepository;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class DailyJobForAttendance extends DailyJob {

    public static final String TAG = "DailyJobForAttendance";

    public static void scheduleJob(boolean isToBeScheduled, String day) {
        if (!isToBeScheduled) {
            if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
                JobManager.instance().cancelAllForTag(TAG);
            }
        } else {
            if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
                return;
            }
            JobRequest.Builder jobBuilder = new JobRequest.Builder(TAG).setUpdateCurrent(false);
            DailyJob.schedule(jobBuilder, TimeUnit.HOURS.toMillis(11) + TimeUnit.MINUTES.toMillis(30), TimeUnit.HOURS.toMillis(11) + TimeUnit.MINUTES.toMillis(45));
            DailyJob.schedule(jobBuilder, TimeUnit.HOURS.toMillis(11) + TimeUnit.MINUTES.toMillis(50), TimeUnit.HOURS.toMillis(12) + TimeUnit.MINUTES.toMillis(0));
        }
    }

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {
        TimetableRepository timetableRepository = new TimetableRepository(getContext());
        final LiveData<List<TimetableEntry>> timetableForDay = timetableRepository.getTimetableForDay(ConverterUtils.getDayOfWeek(new Date()));
//        timetableForDay.observeForever(new Observer<List<TimetableEntry>>() {
//            @Override
//            public void onChanged(List<TimetableEntry> timetableEntries) {
//                timetableForDay.removeObserver(this);
//
//            }
//        });
        return DailyJobResult.SUCCESS;
    }
}
