package com.vyas.pranav.studentcompanion.utils;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.awareness.fence.TimeFence;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableDatabase;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class AutoAttendanceHelper {

    public static final String KEY_PRE_SUBJECT_FENCE = "KEY_OF_SUBJECT_FENCE";
    private double RADIUS = 35;
    private PendingIntent mPendingIntent;
    private Context context;
    private long dwellTime = TimeUnit.MINUTES.toMillis(1);
    private AwarenessFence mLocationFence;

    public AutoAttendanceHelper(Context context) {
        this.context = context;
    }


    private PendingIntent getPendingIntent() {
        if (mPendingIntent == null) {
            Intent intent = new Intent(Constants.FENCE_RECEIVER_ACTION);
            mPendingIntent = PendingIntent.getService(context, Constants.RC_SEND_FENCE_BROADCAST, intent, 0);
        }
        return mPendingIntent;
    }

    private AwarenessFence getLocationFence(double latitude, double longitude) {
        mLocationFence = null;

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return null;
        }
        mLocationFence = LocationFence.in(latitude, longitude, RADIUS, dwellTime);
        if (mLocationFence != null) {
            Logger.d("Location Fence is Received Successfully");
        } else {
            Logger.d("Location Fence is not Received Successfully");
        }
        return mLocationFence;
    }

    public void updateOrRemoveFenceForSubject(boolean isToRegister, String subject, double longitude, double latitude) {
        LiveData<List<TimetableEntry>> fullTimetable = TimetableDatabase.getInstance(context).timetableDao().getTimetableForSubject(subject);
        fullTimetable.observeForever(new Observer<List<TimetableEntry>>() {
            @Override
            public void onChanged(List<TimetableEntry> timetableEntries) {
                fullTimetable.removeObserver(this);
                List<AwarenessFence> timeFences = new ArrayList<>();
                for (TimetableEntry x :
                        timetableEntries) {
                    long start = TimeUnit.MINUTES.toMillis(x.getTimeStart());
                    long end = TimeUnit.MINUTES.toMillis(x.getTimeEnd());

                    if ("Monday".equals(x.getDay())) {
                        timeFences.add(TimeFence.inIntervalOfDay(TimeFence.DAY_OF_WEEK_MONDAY, TimeZone.getDefault(), start, end));
                    } else if ("Tuesday".equals(x.getDay())) {
                        timeFences.add(TimeFence.inIntervalOfDay(TimeFence.DAY_OF_WEEK_TUESDAY, TimeZone.getDefault(), start, end));
                    } else if ("Wednesday".equals(x.getDay())) {
                        timeFences.add(TimeFence.inIntervalOfDay(TimeFence.DAY_OF_WEEK_WEDNESDAY, TimeZone.getDefault(), start, end));
                    } else if ("Thursday".equals(x.getDay())) {
                        timeFences.add(TimeFence.inIntervalOfDay(TimeFence.DAY_OF_WEEK_THURSDAY, TimeZone.getDefault(), start, end));
                    } else if ("Friday".equals(x.getDay())) {
                        timeFences.add(TimeFence.inIntervalOfDay(TimeFence.DAY_OF_WEEK_FRIDAY, TimeZone.getDefault(), start, end));
                    }
                }

                if (getLocationFence(latitude, longitude) == null) {
                    Logger.d("Error occurred");
                    Toast.makeText(context, "Error Occurred while getting location!", Toast.LENGTH_SHORT).show();
                    return;
                }

                AwarenessFence finalFence = AwarenessFence.and(AwarenessFence.or(timeFences), getLocationFence(latitude, longitude));

                String key = KEY_PRE_SUBJECT_FENCE + subject;
                Awareness.getFenceClient(context)
                        .updateFences(getFenceUpdateRequest(key, finalFence, isToRegister))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Logger.d("Registered Successful of fence for Subject : " + subject);
                                    Toast.makeText(context, "Fence Registered successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Logger.d("Registration Unsuccessful for Fence Generation for Subject : " + subject);
                                    Toast.makeText(context, "Fence Unregistered Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public FenceUpdateRequest getFenceUpdateRequest(String key, AwarenessFence finalFence, boolean isToRegister) {
        FenceUpdateRequest.Builder builder = new FenceUpdateRequest.Builder();
        if (isToRegister) {
            builder.addFence(key, finalFence, getPendingIntent());
        } else {
            builder.removeFence(key);
        }
        return builder.build();
    }
}
