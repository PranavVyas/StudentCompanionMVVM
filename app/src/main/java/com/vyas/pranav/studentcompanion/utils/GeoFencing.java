package com.vyas.pranav.studentcompanion.utils;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.services.GeoFenceIntentService;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class GeoFencing {
    private static final int RC_GEOFENCE = 600;
    private Context context;
    private GoogleApiClient mClient;
    private PendingIntent geofencePendingIntent;
    private GeofencingClient mGeofencingClient;
    private List<Geofence> geofenceList;

    public GeoFencing(Context context, GoogleApiClient mClient) {
        this.context = context;
        this.mClient = mClient;
        geofenceList = new ArrayList<>();
        geofencePendingIntent = null;
        mGeofencingClient = LocationServices.getGeofencingClient(context);
    }

    public void updateGeoFenceList(PlaceBuffer places) {
        long EXPIRATION_PERIOD = 24 * 3600 * 1000;
        float RADIUS = 50;
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
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build();
            geofenceList.add(geo);
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.addGeofences(geofenceList);
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(context, GeoFenceIntentService.class);
        geofencePendingIntent = PendingIntent.getService(context, RC_GEOFENCE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
//        LocationServices.GeofencingApi.addGeofences(mClient, getGeofencingRequest(), getGeofencePendingIntent()).setResultCallback(this);
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
//        if (mClient != null || !mClient.isConnected()) {
//            Toast.makeText(context, "Failed GeoFences", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        LocationServices.GeofencingApi.removeGeofences(mClient,getGeofencePendingIntent()).setResultCallback(this);
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
