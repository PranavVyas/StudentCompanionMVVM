package com.vyas.pranav.studentcompanion.ui.activities;
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

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.orhanobut.logger.Logger;
import com.schibstedspain.leku.LocationPickerActivity;
import com.schibstedspain.leku.LocationPickerActivityKt;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.repositories.AutoAttendanceRepository;
import com.vyas.pranav.studentcompanion.utils.AutoAttendanceHelper;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.viewmodels.AutoAttendanceSubjectDetailViewModel;
import com.vyas.pranav.studentcompanion.viewmodels.AutoAttendanceSubjectDetailViewModelFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.adapters.AutoAttendanceSubjectListRecyclerAdapter.EXTRA_SUBJECT_NAME;

public class AutoAttendanceSubjectDetailActivity extends AppCompatActivity implements
//        GoogleApiClient.ConnectionCallbacks
//        , GoogleApiClient.OnConnectionFailedListener
        OnMapReadyCallback {

    private final AutoAttendanceHelper helper = new AutoAttendanceHelper(this);
    @BindView(R.id.tv_auto_attendance_subject_detail_name)
    TextView tvName;
    @BindView(R.id.tv_auto_attendance_subject_detail_subject)
    TextView tvSubject;
    @BindView(R.id.constraint_auto_attendance_main)
    ConstraintLayout constraintMain;

    @BindView(R.id.card_auto_attendance_subject_detail_current_detail)
    MaterialCardView cardCurrentDetails;
    @BindView(R.id.placeholder_auto_attendance_subject_detail)
    ConstraintLayout framePlaceHolder;
    @BindView(R.id.btn_auto_attendance_subject_detail_edit)
    Button btnEditPlace;
    @BindView(R.id.btn_frame_auto_attendance_subject_detail_placeholder_retry)
    Button btnRetry;
    @BindView(R.id.toolbar_auto_attendance_subject_detail)
    Toolbar toolbar;
    //    private GoogleApiClient mClient;
    private AutoAttendanceSubjectDetailViewModel autoAttendanceSubjectDetailViewModel;
    private String currSubject;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    //    private LocationRequest mLocationRequest;
//    private Location mLastLocation;
//    private Marker mCurrentLocationMarker;
    private Circle geoFenceLimits;
    //    private PendingIntent locationUpdatePendingIntent;
    private LiveData<AutoAttendancePlaceEntry> placeEntry;
    private AutoAttendancePlaceEntry currPlaceEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesUtils.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_attendance_subject_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        currPlaceEntry = null;
        currSubject = getIntent().getStringExtra(EXTRA_SUBJECT_NAME);
//        Dexter.withActivity(this)
        AutoAttendanceSubjectDetailViewModelFactory factory = new AutoAttendanceSubjectDetailViewModelFactory(new AutoAttendanceRepository(this), currSubject);
        autoAttendanceSubjectDetailViewModel = ViewModelProviders.of(this, factory).get(AutoAttendanceSubjectDetailViewModel.class);
        checkForPermissionAndContinue();
    }

    private void checkForPermissionAndContinue() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (isLocationProviderAvailable()) {
                            showNoLocationPlaceHolder(false);
                            setUpGoogleMap();
                        } else {
                            showNoLocationPlaceHolder(true);
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void setUpGoogleMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            geoFenceLimits = null;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
//        setUpGoogleClient();
        checkForPermissionAndLoadUi();
//                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                .withListener(new PermissionListener() {
//                    @SuppressLint("MissingPermission")
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        Awareness.getSnapshotClient(AutoAttendanceSubjectDetailActivity.this)
//                                .getLocation().addOnCompleteListener(new OnCompleteListener<LocationResponse>() {
//                            @Override
//                            public void onComplete(@NonNull Task<LocationResponse> task) {
//                                if(task.isSuccessful()){
//                                    //Location is retrieved
//                                    task.getResult().getLocation();
//
//
//                                }
//                            }
//                        })
////                        LocationServices.getFusedLocationProviderClient(AutoAttendanceSubjectDetailActivity.this)
////                                .requestLocationUpdates(getLocationRequest(), getLocationUpdatePendingIntent())
////                                .addOnSuccessListener(AutoAttendanceSubjectDetailActivity.this, new OnSuccessListener<Void>() {
////                                    @Override
////                                    public void onSuccess(Void aVoid) {
////                                        Toast.makeText(AutoAttendanceSubjectDetailActivity.this, "Successful Location available", Toast.LENGTH_SHORT).show();
////                                    }
////                                })
////                                .addOnFailureListener(AutoAttendanceSubjectDetailActivity.this, new OnFailureListener() {
////                                    @Override
////                                    public void onFailure(@NonNull Exception e) {
////                                        Toast.makeText(AutoAttendanceSubjectDetailActivity.this, "Failed due to : " + e.toString(), Toast.LENGTH_SHORT).show();
////                                        Logger.d("Failed due to : " + e.toString());
////                                    }
////                                });
//
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//                        Toast.makeText(AutoAttendanceSubjectDetailActivity.this, "Location Access is needed!", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).check();
//        if (mClient != null) {
//        }
    }

    private void checkForPermissionAndLoadUi() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        map.setMyLocationEnabled(true);
                        populateUI(getIntent());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(AutoAttendanceSubjectDetailActivity.this, "Please provide location services...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

//    private void setUpGoogleClient() {
////        mClient = autoAttendanceSubjectDetailViewModel.getGoogleClient();
//        mClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .enableAutoManage(this, this)
//                .build();
//    }

    @OnClick(R.id.btn_frame_auto_attendance_subject_detail_placeholder_show_location)
    void clickedShowLocation() {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    private void openSettingsDialog() {
        new MaterialAlertDialogBuilder(this)
                .setNegativeButton("CANCEL", (dialog, which) -> {
                    Toast.makeText(AutoAttendanceSubjectDetailActivity.this, "Permission is denied...\nPlease Allow Permission first...", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    AutoAttendanceSubjectDetailActivity.this.finish();
                }).setPositiveButton("GO TO SETTINGS", (dialog, which) -> {
            Intent openSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            openSettings.setData(uri);
            startActivityForResult(openSettings, 101);
            dialog.dismiss();
            AutoAttendanceSubjectDetailActivity.this.finish();
        }).setMessage("\u25CF Location Permission is needed for setting up GeoFences\n\u25CF Please give permission through settings")
                .setTitle("Permission Denied")
                .setCancelable(false)
                .setIcon(R.drawable.ic_info_black)
                .show();
    }

    private boolean isLocationProviderAvailable() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showNoLocationPlaceHolder(boolean isShown) {
        if (isShown) {
            constraintMain.setVisibility(View.GONE);
            framePlaceHolder.setVisibility(View.VISIBLE);
            Toast.makeText(AutoAttendanceSubjectDetailActivity.this, "Location Provider is not available\nPlease enable Location from Quick Settings", Toast.LENGTH_SHORT).show();
        } else {
            framePlaceHolder.setVisibility(View.GONE);
            constraintMain.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_frame_auto_attendance_subject_detail_placeholder_retry)
    void retryClicked() {
        if (isLocationProviderAvailable()) {
            showNoLocationPlaceHolder(false);
            setUpGoogleMap();
        } else {
            showNoLocationPlaceHolder(true);
        }
    }

    private void populateUI(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(EXTRA_SUBJECT_NAME)) {
                tvSubject.setText("Subject : " + currSubject);
                placeEntry = autoAttendanceSubjectDetailViewModel.getPlaceEntryOfSubject();
                placeEntry.observe(this, new Observer<AutoAttendancePlaceEntry>() {
                    @Override
                    public void onChanged(AutoAttendancePlaceEntry placeEntry) {
                        if (placeEntry != null) {
                            currPlaceEntry = placeEntry;
                            if ((placeEntry.getLang() == Constants.DEFAULT_LANG) &&
                                    (placeEntry.getLat() == Constants.DEFAULT_LAT)) {
                                autoAttendanceSubjectDetailViewModel.setCurrName("\u25CF No Place Selected!\n\u25CF You will not be able to use Auto Attendance Feature if no place is selected!\n\u25CF Select place now!");
                                refreshTextView();
                                Toast.makeText(AutoAttendanceSubjectDetailActivity.this, "First Add Subject Places to the subject location", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            showGeoFenceCircleInMap(placeEntry.getLat(), placeEntry.getLang());
                            autoAttendanceSubjectDetailViewModel.setCurrName("\u25CF Successfully set Location of Subject : " + currSubject + "\n\u25CF You will be able to use Auto Attendance Feature!\n\u25CF Please Note that Auto Attendance Feature is still in beta phase so it might be inaccurate sometimes");
                            refreshTextView();
                        }
                    }
                });
            }
        }
    }

    private void refreshTextView() {
        tvName.setText(autoAttendanceSubjectDetailViewModel.getCurrName());
    }


//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        Logger.d("Google Client Connected");
//    }

//    @Override
//    public void onConnectionSuspended(int i) {
//        Logger.d("Google Client Suspended");
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Logger.d("Client Connection Failed due to " + connectionResult.getErrorMessage());
//    }

    @OnClick(R.id.btn_auto_attendance_subject_detail_edit)
    void editBtnClicked() {
        BottomSheetDialog mDialog = new BottomSheetDialog(this);
        mDialog.setContentView(R.layout.item_holder_bottom_sheet_auto_atten_sub_detail_edit_place);
        mDialog.show();

        TextView tv = mDialog.findViewById(R.id.tv_holder_bottom_sheet_auto_att_sub_detail);
        Button btnEditPlaceDialog = mDialog.findViewById(R.id.btn_holder_bottom_sheet_auto_att_sub_detail);

        tv.setText("Do you want to Change Place for Subject: " + currSubject + " ?");
        btnEditPlaceDialog.setOnClickListener(view -> {
            mDialog.dismiss();
            Intent locationPickerIntent = new LocationPickerActivity.Builder()
                    .withGeolocApiKey(getString(R.string.api_key))
                    .withDefaultLocaleSearchZone()
                    .shouldReturnOkOnBackPressed()
                    .withStreetHidden()
                    .withCityHidden()
                    .withZipCodeHidden()
                    .withGoogleTimeZoneEnabled()
                    .withUnnamedRoadHidden()
                    .build(AutoAttendanceSubjectDetailActivity.this);
            startActivityForResult(locationPickerIntent, Constants.RC_OPEN_PLACE_PICKER_CUSTOM);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_OPEN_PLACE_PICKER_CUSTOM && resultCode == RESULT_OK) {
            if (data != null) {
                double lat = data.getDoubleExtra(LocationPickerActivityKt.LATITUDE, 0.0);
                double lang = data.getDoubleExtra(LocationPickerActivityKt.LONGITUDE, 0.0);
                Logger.d("Lat: " + lat + "\nLang: " + lang);
                currPlaceEntry.setLang(lang);
                currPlaceEntry.setLat(lat);
                autoAttendanceSubjectDetailViewModel.refreshFenceInDb(currPlaceEntry);
                //Updating Fence in Real Life
                helper.updateOrRemoveFenceForSubject(true, currSubject, lat, lang);
            } else {
                Toast.makeText(this, "Place was either not changed or result has error in it!", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(getLocationUpdatePendingIntent());
//    }

    private void showGeoFenceCircleInMap(double lat, double lang) {
        if (geoFenceLimits != null) {
            geoFenceLimits.remove();
        }
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(lat, lang))
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(AutoAttendanceHelper.RADIUS_OF_FENCE);
        geoFenceLimits = map.addCircle(circleOptions);
    }

//    private PendingIntent getLocationUpdatePendingIntent() {
//        if (locationUpdatePendingIntent != null) {
//            return locationUpdatePendingIntent;
//        }
//        Intent locationIntent = new Intent(this, LocationUpdateReceiver.class);
//        locationUpdatePendingIntent = PendingIntent.getBroadcast(this, Constants.RC_LOCATION_RECEIVED, locationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        return locationUpdatePendingIntent;
//    }

//    private LocationRequest getLocationRequest() {
//        if (mLocationRequest != null) {
//            return mLocationRequest;
//        }
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(TimeUnit.SECONDS.toMillis(30));
//        mLocationRequest.setFastestInterval(TimeUnit.SECONDS.toMillis(29));
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        return mLocationRequest;
//    }

//    class LocationUpdateReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (LocationResult.hasResult(intent)) {
//                LocationResult locationResult = LocationResult.extractResult(intent);
//                Location location = locationResult.getLastLocation();
//                mLastLocation = location;
//                if (mCurrentLocationMarker != null) {
//                    mCurrentLocationMarker.remove();
//                }
//                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//                markerOptions.title("Current Position");
//                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//                mCurrentLocationMarker = map.addMarker(markerOptions);
//
//                //move map camera
//                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 40.0f));
//                Logger.d("Received Location Update Now!");
//            }
//
//        }
//    }
}