package com.vyas.pranav.studentcompanion.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.awareness.fence.TimeFence;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableDatabase;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.services.FenceAutoAttendanceIntentService;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class AutoAttendanceHelper {

    public static final String KEY_PRE_SUBJECT_FENCE = "KEY_OF_SUBJECT_FENCE";
    public static final double RADIUS_OF_FENCE = 100.0;
    private PendingIntent mPendingIntent;
    private Context context;
    private long dwellTime = TimeUnit.MINUTES.toMillis(2);
    private AwarenessFence mLocationFence;

    public AutoAttendanceHelper(Context context) {
        this.context = context;
    }


    private PendingIntent getPendingIntent() {
        if (mPendingIntent == null) {
            Intent intent = new Intent(context, FenceAutoAttendanceIntentService.class);
            mPendingIntent = PendingIntent.getService(context, Constants.RC_SEND_FENCE_BROADCAST, intent, 0);
        }
        return mPendingIntent;
    }

//    private AwarenessFence getLocationFence(double latitude, double longitude) {
//        mLocationFence = null;
//        mLocationFence = LocationFence.in(latitude, longitude, RADIUS_OF_FENCE, dwellTime);
//        if (mLocationFence != null) {
//            Logger.d("Location Fence is Created Successfully");
//        } else {
//            Logger.d("Location Fence is not Created Successfully");
//        }
//        return mLocationFence;
//    }

    public void updateOrRemoveFenceForSubject(boolean isToRegister, String subject, double longitude, double latitude) {
        LiveData<List<TimetableEntry>> fullTimetable = TimetableDatabase.getInstance(context).timetableDao().getTimetableForSubject(subject);
        fullTimetable.observeForever(new Observer<List<TimetableEntry>>() {
            @Override
            public void onChanged(List<TimetableEntry> timetableEntries) {
                fullTimetable.removeObserver(this);
                Dexter.withActivity((Activity) context)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                List<AwarenessFence> timeFences = new ArrayList<>();
                                for (TimetableEntry x :
                                        timetableEntries) {
                                    long start = TimeUnit.MINUTES.toMillis(x.getTimeStart());
                                    long end = start + TimeUnit.MINUTES.toMillis(5);
                                    if ("Monday".equals(x.getDay())) {
                                        timeFences.add(TimeFence.inIntervalOfDay(TimeFence.DAY_OF_WEEK_MONDAY, TimeZone.getDefault(), start, end));
                                        Logger.d("Fence Added for Constraint: Day(Monday) :: Time: " + start + " to " + end + " :: Timezone: " + TimeZone.getDefault().getDisplayName());
                                    } else if ("Tuesday".equals(x.getDay())) {
                                        timeFences.add(TimeFence.inIntervalOfDay(TimeFence.DAY_OF_WEEK_TUESDAY, TimeZone.getDefault(), start, end));
                                        Logger.d("Fence Added for Constraint: Day(Tuesday) :: Time: " + start + " to " + end + " :: Timezone: " + TimeZone.getDefault().getDisplayName());
                                    } else if ("Wednesday".equals(x.getDay())) {
                                        timeFences.add(TimeFence.inIntervalOfDay(TimeFence.DAY_OF_WEEK_WEDNESDAY, TimeZone.getDefault(), start, end));
                                        Logger.d("Fence Added for Constraint: Day(Wednesday) :: Time: " + start + " to " + end + " :: Timezone: " + TimeZone.getDefault().getDisplayName());
                                    } else if ("Thursday".equals(x.getDay())) {
                                        timeFences.add(TimeFence.inIntervalOfDay(TimeFence.DAY_OF_WEEK_THURSDAY, TimeZone.getDefault(), start, end));
                                        Logger.d("Fence Added for Constraint: Day(Thursday) :: Time: " + start + " to " + end + " :: Timezone: " + TimeZone.getDefault().getDisplayName());
                                    } else if ("Friday".equals(x.getDay())) {
                                        timeFences.add(TimeFence.inIntervalOfDay(TimeFence.DAY_OF_WEEK_FRIDAY, TimeZone.getDefault(), start, end));
                                        Logger.d("Fence Added for Constraint: Day(Friday) :: Time: " + start + " to " + end + " :: Timezone: " + TimeZone.getDefault().getDisplayName());
                                    }
                                }
                                mLocationFence = null;
                                mLocationFence = LocationFence.in(latitude, longitude, RADIUS_OF_FENCE, dwellTime);
                                if (mLocationFence != null) {
                                    Logger.d("Location Fence is Created Successfully");
                                } else {
                                    Logger.d("Location Fence is not Created Successfully,Error Occurred");
                                    Toast.makeText(context, "Location Fence is not Created Successfully", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                AwarenessFence finalFence = AwarenessFence.and(AwarenessFence.or(timeFences), mLocationFence);
                                String key = KEY_PRE_SUBJECT_FENCE + subject;
                                Awareness.getFenceClient(context)
                                        .updateFences(getFenceUpdateRequest(key, finalFence, isToRegister))
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    if (isToRegister) {
                                                        Toast.makeText(context, "Fence Registered successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(context, "Fence Removed successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                    Logger.d("Fence Action Successful for Subject : " + subject + " :: Action: " + (isToRegister ? "Registered" : "Removed"));
                                                } else {
                                                    Logger.d("Registration/Removal Unsuccessful for Fence Generation for Subject : " + subject);
                                                    Toast.makeText(context, "Fence Registration/removal unsuccessful", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                Toast.makeText(context, "Location Permission Denied!", Toast.LENGTH_SHORT).show();
                                Logger.d("Location Permission Denied for creating LocationFence");
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest
                                                                                   permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
    }

    private FenceUpdateRequest getFenceUpdateRequest(String key, AwarenessFence finalFence, boolean isToRegister) {
        FenceUpdateRequest.Builder builder = new FenceUpdateRequest.Builder();
        if (isToRegister) {
            builder.addFence(key, finalFence, getPendingIntent());
        } else {
            builder.removeFence(key);
        }
        return builder.build();
    }
}
