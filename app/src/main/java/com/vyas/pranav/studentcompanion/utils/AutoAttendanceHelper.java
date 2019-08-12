package com.vyas.pranav.studentcompanion.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.TimeFence;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.services.FenceAutoAttendanceIntentService;

import java.util.concurrent.TimeUnit;

public class AutoAttendanceHelper {

    public static final String KEY_PRE_SUBJECT_FENCE = "KEY_OF_SUBJECT_FENCE";
    public static final double RADIUS_OF_FENCE = 100.0;
    private final Context context;
    private final long dwellTime = TimeUnit.MINUTES.toMillis(2);
    private PendingIntent mPendingIntent;
    private AwarenessFence mLocationFence;

    public AutoAttendanceHelper(Context context) {
        this.context = context;
    }

    public void updateOrRemoveFenceForSubject(boolean isToRegister, String subject, double latitude, double longitude) {
        SharedPreferencesUtils utils = SharedPreferencesUtils.getInstance(context);
        int noOfLecturesPerDay = utils.getNoOfLecturesPerDay();
        int startTimeForLecture = utils.getStartTimeForLecture(1);
        int endTimeForLecture = utils.getEndTimeForLecture(noOfLecturesPerDay);

        mLocationFence = null;
//        mLocationFence = L
//        LocationFence.in(latitude, longitude, RADIUS_OF_FENCE, TimeUnit.MINUTES.toMillis(1));
//        if (mLocationFence != null) {
//            Logger.d("Location Fence is Created Successfully");
//        } else {
//            Logger.d("Location Fence is not Created Successfully,Error Occurred");
//            Toast.makeText(context, "Location Fence is not Created Successfully", Toast.LENGTH_SHORT).show();
//            return;
//        }

        AwarenessFence timeWindow = TimeFence.inDailyInterval(null, TimeUnit.MINUTES.toMillis(startTimeForLecture), TimeUnit.MINUTES.toMillis(endTimeForLecture));
        Toast.makeText(context, "Fence Added", Toast.LENGTH_SHORT).show();
        Logger.d("Time Fence Added For Subject: " + subject + "For Time : " + startTimeForLecture + " to " + endTimeForLecture);
//        AwarenessFence finalFence = AwarenessFence.and(timeWindow,mLocationFence);
        String key = KEY_PRE_SUBJECT_FENCE + subject;
        Awareness.getFenceClient(context)
                .updateFences(getFenceUpdateRequest(key, timeWindow, isToRegister))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (isToRegister) {
                            Toast.makeText(context, "Fence Registered successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Fence Removed successfully", Toast.LENGTH_SHORT).show();
                        }
                        Logger.d("Fence Action Successful for Subject : " + subject + " :: Action: " + (isToRegister ? "Registered" : "Removed") + "\nWith Key: " + key);
                    } else {
                        Logger.d("Registration/Removal Unsuccessful for Fence Generation for Subject : " + subject);
                        Toast.makeText(context, "Fence Registration/removal unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                });

//        LiveData<List<TimetableEntry>> fullTimetable = MainDatabase.getInstance(context).timetableDao().getTimetableForSubject(subject);
//        fullTimetable.observeForever(new Observer<List<TimetableEntry>>() {
//            @Override
//            public void onChanged(List<TimetableEntry> timetableEntries) {
//                fullTimetable.removeObserver(this);
//                Dexter.withActivity((Activity) context)
//                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                        .withListener(new PermissionListener() {
//                            @SuppressLint("MissingPermission")
//                            @Override
//                            public void onPermissionGranted(PermissionGrantedResponse response) {
//                                List<AwarenessFence> timeFences = new ArrayList<>();
//                                for (TimetableEntry x :
//                                        timetableEntries) {
////                                    long start = TimeUnit.MINUTES.toMillis(x.getTimeStart());
////                                    long end = TimeUnit.MINUTES.toMillis(x.getTimeEnd());
//                                    long start = TimeUnit.HOURS.toMillis(12+7)+TimeUnit.MINUTES.toMillis(50);
//                                    long end = TimeUnit.HOURS.toMillis(12+7)+TimeUnit.MINUTES.toMillis(55);
////                                    long timeInMiddle = (start + end) / 2;
////                                    long timeInEnd = timeInMiddle + TimeUnit.MINUTES.toMillis(2);
//                                    if ("Monday".equals(x.getDay())) {
//                                        timeFences.add(AwarenessFence.and(TimeFence.inIntervalOfDay(TimeFence.DAY_OF_WEEK_MONDAY, TimeZone.getDefault(), start, end), mLocationFence));
//                                        Logger.d("Fence Added for Constraint: Day(Monday) :: Time: " + start + " to " + end + " :: Timezone: " + TimeZone.getDefault().getDisplayName());
//                                    } else if ("Tuesday".equals(x.getDay())) {
//                                        timeFences.add(AwarenessFence.and(TimeFence.inIntervalOfDay(TimeFence.DAY_OF_WEEK_TUESDAY, TimeZone.getDefault(), start, end), mLocationFence));
//                                        Logger.d("Fence Added for Constraint: Day(Tuesday) :: Time: " + start + " to " + end + " :: Timezone: " + TimeZone.getDefault().getDisplayName());
//                                    } else if ("Wednesday".equals(x.getDay())) {
//                                        timeFences.add(AwarenessFence.and(TimeFence.inIntervalOfDay(TimeFence.DAY_OF_WEEK_WEDNESDAY, TimeZone.getDefault(), start, end), mLocationFence));
//                                        Logger.d("Fence Added for Constraint: Day(Wednesday) :: Time: " + start + " to " + end + " :: Timezone: " + TimeZone.getDefault().getDisplayName());
//                                    } else if ("Thursday".equals(x.getDay())) {
//                                        timeFences.add(AwarenessFence.and(TimeFence.inIntervalOfDay(TimeFence.DAY_OF_WEEK_THURSDAY, TimeZone.getDefault(), start, end), mLocationFence));
//                                        Logger.d("Fence Added for Constraint: Day(Thursday) :: Time: " + start + " to " + end + " :: Timezone: " + TimeZone.getDefault().getDisplayName());
//                                    } else if ("Friday".equals(x.getDay())) {
//                                        timeFences.add(AwarenessFence.and(TimeFence.inIntervalOfDay(TimeFence.DAY_OF_WEEK_FRIDAY, TimeZone.getDefault(), start, end), mLocationFence));
//                                        Logger.d("Fence Added for Constraint: Day(Friday) :: Time: " + start + " to " + end + " :: Timezone: " + TimeZone.getDefault().getDisplayName());
//                                    }
//                                }
//                                //todo improve this
//                                AwarenessFence finalTimeFence = AwarenessFence.or(timeFences);
////                                AwarenessFence finalFence = AwarenessFence.and(finalTimeFence, mLocationFence);
//                                String key = KEY_PRE_SUBJECT_FENCE + subject;
//                                Awareness.getFenceClient(context)
//                                        .updateFences(getFenceUpdateRequest(key, finalTimeFence, isToRegister))
//                                        .addOnCompleteListener(task -> {
//                                            if (task.isSuccessful()) {
//                                                if (isToRegister) {
//                                                    Toast.makeText(context, "Fence Registered successfully", Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Toast.makeText(context, "Fence Removed successfully", Toast.LENGTH_SHORT).show();
//                                                }
//                                                Logger.d("Fence Action Successful for Subject : " + subject + " :: Action: " + (isToRegister ? "Registered" : "Removed") + "\nWith Key: " + key);
//                                            } else {
//                                                Logger.d("Registration/Removal Unsuccessful for Fence Generation for Subject : " + subject);
//                                                Toast.makeText(context, "Fence Registration/removal unsuccessful", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                            }
//
//                            @Override
//                            public void onPermissionDenied(PermissionDeniedResponse response) {
//                                Toast.makeText(context, "Location Permission Denied!", Toast.LENGTH_SHORT).show();
//                                Logger.d("Location Permission Denied for creating LocationFence");
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(PermissionRequest
//                                                                                   permission, PermissionToken token) {
//                                token.continuePermissionRequest();
//                            }
//                        }).check();
//            }
//        });
    }

    private PendingIntent getPendingIntent() {
        if (mPendingIntent == null) {
            Intent intent = new Intent(context, FenceAutoAttendanceIntentService.class);
            mPendingIntent = PendingIntent.getService(context, Constants.RC_SEND_FENCE_BROADCAST, intent, 0);
        }
        return mPendingIntent;
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
