package com.vyas.pranav.studentcompanion.repositories;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceDao;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlacesDatabase;
import com.vyas.pranav.studentcompanion.services.GeoFenceTransitionBroadcastReceiver;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;

public class GeoFencingRepository {
    private static final int RC_GEOFENCE = 600;
    private Context context;
    private PendingIntent geofencePendingIntent;
    private GeofencingClient mGeofencingClient;
    private List<Geofence> geofenceList;
    private GoogleApiClient mClient;

    public GeoFencingRepository(Context context, GoogleApiClient client) {
        this.context = context;
        geofenceList = new ArrayList<>();
        geofencePendingIntent = null;
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        mGeofencingClient = LocationServices.getGeofencingClient(context);
        this.mClient = client;
    }

    public void updateGeoFenceList(PlaceBuffer places) {
        geofenceList = new ArrayList<>();
        long EXPIRATION_PERIOD = TimeUnit.HOURS.toMillis(24);
        float RADIUS = 100.0f;
        if (places == null || places.getCount() == 0) {
            return;
        }
        for (int i = 0; i < places.getCount(); i++) {
            double lat = places.get(i).getLatLng().latitude;
            double lag = places.get(i).getLatLng().longitude;
            String placeID = places.get(i).getId();
            Geofence geo = new Geofence.Builder()
                    .setRequestId(placeID)
                    .setExpirationDuration(EXPIRATION_PERIOD)
                    .setCircularRegion(lat, lag, RADIUS)
                    .setLoiteringDelay((int) TimeUnit.MINUTES.toMillis(5))
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build();
            geofenceList.add(geo);
        }
    }

    public void refreshAllGeoFences() {
        final AutoAttendancePlaceDao autoAttendancePlaceDao = AutoAttendancePlacesDatabase.getInstance(context).autoAttendancePlaceDao();
        geofenceList = new ArrayList<>();
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                autoAttendancePlaceDao.getAllPlaceIds().observeForever(new Observer<List<AutoAttendancePlaceEntry>>() {
                    @Override
                    public void onChanged(List<AutoAttendancePlaceEntry> autoAttendancePlaceEntries) {
                        autoAttendancePlaceDao.getAllPlaceIds().removeObserver(this);
                        final List<String> requestIds = new ArrayList<>();
                        for (int i = 0; i < autoAttendancePlaceEntries.size(); i++) {
                            requestIds.add(autoAttendancePlaceEntries.get(i).getPlaceId());
                        }
                        Places.GeoDataApi.getPlaceById(mClient, requestIds.toArray(new String[requestIds.size()])).setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(@NonNull PlaceBuffer places) {
                                for (int i = 0; i < places.getCount(); i++) {
                                    Geofence geo = new Geofence.Builder()
                                            .setRequestId(requestIds.get(i))
                                            .setCircularRegion(places.get(i).getLatLng().latitude, places.get(i).getLatLng().longitude, 100.0f)
                                            .setExpirationDuration(TimeUnit.HOURS.toMillis(24))
                                            .setLoiteringDelay((int) TimeUnit.SECONDS.toMillis(30))
                                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                                            .build();
                                    geofenceList.add(geo);
                                    unregisterAllGeoFences();
                                    registerAllGeoFences();
                                }
                            }
                        });
                    }
                });
            }
        });

    }

    public void unRegisterAllGeoFenceAtOnce() {
        final AutoAttendancePlaceDao autoAttendancePlaceDao = AutoAttendancePlacesDatabase.getInstance(context).autoAttendancePlaceDao();
        geofenceList = new ArrayList<>();
        autoAttendancePlaceDao.getAllPlaceIds().observeForever(new Observer<List<AutoAttendancePlaceEntry>>() {
            @Override
            public void onChanged(List<AutoAttendancePlaceEntry> autoAttendancePlaceEntries) {
                autoAttendancePlaceDao.getAllPlaceIds().removeObserver(this);
                final List<String> requestIds = new ArrayList<>();
                for (int i = 0; i < autoAttendancePlaceEntries.size(); i++) {
                    requestIds.add(autoAttendancePlaceEntries.get(i).getPlaceId());
                }
                Places.GeoDataApi.getPlaceById(mClient, requestIds.toArray(new String[requestIds.size()])).setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceBuffer places) {
                        for (int i = 0; i < places.getCount(); i++) {
                            Geofence geo = new Geofence.Builder()
                                    .setRequestId(requestIds.get(i))
                                    .setCircularRegion(places.get(i).getLatLng().latitude, places.get(i).getLatLng().longitude, 100.0f)
                                    .setExpirationDuration(TimeUnit.HOURS.toMillis(24))
                                    .setLoiteringDelay((int) TimeUnit.SECONDS.toMillis(30))
                                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                                    .build();
                            geofenceList.add(geo);
                            unregisterAllGeoFences();
                        }
                    }
                });
            }
        });
    }

    public void updateGeoFenceList(Place places) {
        geofenceList = new ArrayList<>();
        long EXPIRATION_PERIOD = TimeUnit.HOURS.toMillis(24);
        float RADIUS = 100.0f;
        if (places == null) {
            return;
        }
        double lat = places.getLatLng().latitude;
        double lag = places.getLatLng().longitude;
        String placeID = places.getId();
        Geofence geo = new Geofence.Builder()
                .setRequestId(placeID)
                .setExpirationDuration(EXPIRATION_PERIOD)
                .setCircularRegion(lat, lag, RADIUS)
                .setLoiteringDelay((int) TimeUnit.SECONDS.toMillis(30))
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build();
        geofenceList.add(geo);

    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        //Toast.makeText(context, "Getting Pending Intent", Toast.LENGTH_SHORT).show();
        if (geofencePendingIntent != null) {
            Logger.d("Old Pending intent received");
            return geofencePendingIntent;
        }
        Logger.d("Pending Intent Received");
        Intent intent = new Intent(context, GeoFenceTransitionBroadcastReceiver.class);
        geofencePendingIntent = PendingIntent.getBroadcast(context, RC_GEOFENCE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    public void registerAllGeoFences() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Successfully Added GeoFences", Toast.LENGTH_SHORT).show();
                        Logger.d("Successfully Added GeoFences");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error Occurred while Adding GeoFences", Toast.LENGTH_SHORT).show();
                        Logger.d("Error Occurred while Adding GeoFences : " + e.getLocalizedMessage());
                    }
                });
    }

    public void unregisterAllGeoFences() {
        LocationServices.getGeofencingClient(context).removeGeofences(getGeofencePendingIntent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Successfully Removed GeoFences", Toast.LENGTH_SHORT).show();
                        Logger.d("Successfully Removed GeoFences");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error Occured while Removing GeoFences", Toast.LENGTH_SHORT).show();
                        Logger.d("Error Occured while Removing GeoFences : " + e.getMessage());
                    }
                });
    }
}
