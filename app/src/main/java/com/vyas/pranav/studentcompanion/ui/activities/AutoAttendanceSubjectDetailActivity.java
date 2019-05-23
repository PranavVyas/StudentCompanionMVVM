package com.vyas.pranav.studentcompanion.ui.activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.repositories.SharedPreferencesRepository;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.viewmodels.AutoAttendanceSubjectDetailViewModel;
import com.vyas.pranav.studentcompanion.viewmodels.AutoAttendanceSubjectDetailViewModelFactory;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.adapters.AutoAttendanceSubjectListRecyclerAdapter.EXTRA_SUBJECT_NAME;

public class AutoAttendanceSubjectDetailActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener
        , OnMapReadyCallback {

    private static final int RC_LOCATION_ACCESS = 3000;
    private static final int RC_CHANGE_LOCATION = 4000;
    private static final int RC_LOCATION_RECEIVED = 120;
    @BindView(R.id.tv_auto_attendance_subject_detail_address)
    TextView tvAddress;
    @BindView(R.id.tv_auto_attendance_subject_detail_name)
    TextView tvName;
    @BindView(R.id.tv_auto_attendance_subject_detail_subject)
    TextView tvSubject;
    @BindView(R.id.toolbar_auto_attendance_subjet_detail)
    Toolbar toolbar;

    @BindView(R.id.card_auto_attendance_subject_detail_current_detail)
    CardView cardCurrentDetails;
    @BindView(R.id.frame_auto_attendance_subject_detail_placeholder)
    ConstraintLayout framePlaceHolder;
    @BindView(R.id.btn_auto_attendance_subject_detail_edit)
    Button btnEditPlace;
    @BindView(R.id.btn_frame_auto_attendance_subject_detail_placeholder_retry)
    Button btnRetry;

    private GoogleApiClient mClient;
    private AutoAttendanceSubjectDetailViewModel autoAttendanceSubjectDetailViewModel;
    private LiveData<AutoAttendancePlaceEntry> placeIdOFSubject;
    private String currSubject;
    private AutoAttendancePlaceEntry currPlace;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Marker mCurrentLocationMarker;
    private Circle geoFenceLimits;
    private PendingIntent locationUpdatePendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesRepository.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_attendance_subject_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        checkForPermissionAndContinue();
        currPlace = null;
    }

    private void setUpGoogleMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        geoFenceLimits = null;
    }

    private void setUpGoogleClient() {
//        mClient = autoAttendanceSubjectDetailViewModel.getGoogleClient();
        mClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, this)
                .build();
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
                        } else {
                            finish();
                        }
//                        checkForPermissionAndContinue();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
//        if (ActivityCompat.checkSelfPermission(AutoAttendanceSubjectDetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION};
//            ActivityCompat.requestPermissions(this, permissions, RC_LOCATION_ACCESS);
//        }
    }

    private void openSettingsDialog() {
        new AlertDialog.Builder(this)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AutoAttendanceSubjectDetailActivity.this, "Permission is denied...\nPlease Allow Permission first...", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        AutoAttendanceSubjectDetailActivity.this.finish();
                    }
                }).setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent openSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                openSettings.setData(uri);
                startActivityForResult(openSettings, 101);
                dialog.dismiss();
                AutoAttendanceSubjectDetailActivity.this.finish();
            }
        }).setMessage("Location Permission is needed for setting up GeoFences\nPlease give permission through settings")
                .setTitle("Permission Denied")
                .setCancelable(false)
                .setIcon(R.drawable.ic_info_black)
                .show();
    }

    private boolean isLocationProviderAvailable() {
        //TODO Implement this method
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showNoLocationPlaceHolder(boolean isShown) {
        //TODO Implement Later
        if (isShown) {
            cardCurrentDetails.setVisibility(View.GONE);
            btnEditPlace.setVisibility(View.GONE);
            framePlaceHolder.setVisibility(View.VISIBLE);
            Toast.makeText(AutoAttendanceSubjectDetailActivity.this, "Location Provider is not available\nPlease enable Location from Quick Settings", Toast.LENGTH_SHORT).show();
        } else {
            framePlaceHolder.setVisibility(View.GONE);
            cardCurrentDetails.setVisibility(View.VISIBLE);
            btnEditPlace.setVisibility(View.VISIBLE);
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
                currSubject = intent.getStringExtra(EXTRA_SUBJECT_NAME);
                tvSubject.setText("Subject : " + currSubject);
                placeIdOFSubject = autoAttendanceSubjectDetailViewModel.getPlaceIdOFSubject(currSubject);
                placeIdOFSubject.observe(this, new Observer<AutoAttendancePlaceEntry>() {
                    @Override
                    public void onChanged(AutoAttendancePlaceEntry placeEntry) {
                        if (placeEntry != null) {
                            currPlace = placeEntry;
                            String id = placeEntry.getPlaceId();
                            if (!id.equals(Constants.DEFAULT_PLACE_ID)) {
                                final PendingResult<PlaceBuffer> placeById = Places.GeoDataApi.getPlaceById(mClient, id);
                                placeById.setResultCallback(new ResultCallback<PlaceBuffer>() {
                                    @Override
                                    public void onResult(@NonNull PlaceBuffer places) {
                                        if (places.getStatus().isSuccess()) {
                                            if (places.getStatus().isCanceled()) {
                                                Toast.makeText(AutoAttendanceSubjectDetailActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                                                return;
                                            } else if (places.getStatus().isInterrupted()) {
                                                Toast.makeText(AutoAttendanceSubjectDetailActivity.this, "Interrupted", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            Logger.d("Data Changed New data is \nAddress is :" + places.get(0).getAddress() + "\nName is :" + places.get(0).getName());
                                            autoAttendanceSubjectDetailViewModel.setCurrAddress(places.get(0).getAddress() + "");
                                            autoAttendanceSubjectDetailViewModel.setCurrName(places.get(0).getName() + "");
                                            refreshTextViewData();
                                            showGeoFenceCircleInMap(places.get(0).getLatLng());
                                            places.release();
                                        } else {
                                            Logger.d("Error Occurred");
                                            Toast.makeText(AutoAttendanceSubjectDetailActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }
    }

    private void refreshTextViewData() {
        tvAddress.setText(autoAttendanceSubjectDetailViewModel.getCurrAddress());
        tvName.setText(autoAttendanceSubjectDetailViewModel.getCurrName());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Logger.d("Google Client Connected");
        AutoAttendanceSubjectDetailViewModelFactory factory = new AutoAttendanceSubjectDetailViewModelFactory(this, mClient);
        autoAttendanceSubjectDetailViewModel = ViewModelProviders.of(this, factory).get(AutoAttendanceSubjectDetailViewModel.class);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(getLocationRequest(), getLocationUpdatePendingIntent()).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AutoAttendanceSubjectDetailActivity.this, "Failed due to : " + e.toString(), Toast.LENGTH_SHORT).show();
                    Logger.d("Failed due to : " + e.toString());
                }
            });
        }
        if (mClient != null) {
            populateUI(getIntent());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Logger.d("Google Client Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Logger.d("Client Connection Failed due to " + connectionResult.getErrorMessage());
    }

    @OnClick(R.id.btn_auto_attendance_subject_detail_edit)
    void editBtnClicked() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        try {
            Intent changeData = intentBuilder.build(this);
            startActivityForResult(changeData, RC_CHANGE_LOCATION);
        } catch (GooglePlayServicesRepairableException e) {
            Logger.d("Please Update Google Play services");
            Toast.makeText(this, "Please Update Google Play services", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            Logger.d("Please Install Google Play Services");
            Toast.makeText(this, "Please Install Google Play Services", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_CHANGE_LOCATION && resultCode == RESULT_OK) {
            if (data != null) {
                Place place = PlacePicker.getPlace(this, data);
                currPlace.setPlaceId(place.getId());
                autoAttendanceSubjectDetailViewModel.updatePlaceId(currPlace);
                autoAttendanceSubjectDetailViewModel.refreshAllGeoFences();
                //autoAttendanceSubjectDetailViewModel.refreshGeoFences(place);
            } else {
                Toast.makeText(this, "Error Occurred While Retrieving Data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(getLocationUpdatePendingIntent());
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                setUpGoogleClient();
                map.setMyLocationEnabled(true);
            } else {
                //TODO Request Location Permission
                Toast.makeText(this, "Please provide location services...", Toast.LENGTH_SHORT).show();
            }
        } else {
            setUpGoogleClient();
            map.setMyLocationEnabled(true);
        }
    }

    private void showGeoFenceCircleInMap(LatLng centerPosition) {
        if (geoFenceLimits != null) {
            geoFenceLimits.remove();
        }
        CircleOptions circleOptions = new CircleOptions()
                .center(centerPosition)
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(100);
        geoFenceLimits = map.addCircle(circleOptions);
    }

    private PendingIntent getLocationUpdatePendingIntent() {
        if (locationUpdatePendingIntent != null) {
            return locationUpdatePendingIntent;
        }
        Intent locationIntent = new Intent(this, LocationUpdateReceiver.class);
        locationUpdatePendingIntent = PendingIntent.getBroadcast(this, RC_LOCATION_RECEIVED, locationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return locationUpdatePendingIntent;
    }

    private LocationRequest getLocationRequest() {
        if (mLocationRequest != null) {
            return mLocationRequest;
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(TimeUnit.SECONDS.toMillis(30));
        mLocationRequest.setFastestInterval(TimeUnit.SECONDS.toMillis(29));
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return mLocationRequest;
    }

    class LocationUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (LocationResult.hasResult(intent)) {
                LocationResult locationResult = LocationResult.extractResult(intent);
                Location location = locationResult.getLastLocation();
                mLastLocation = location;
                if (mCurrentLocationMarker != null) {
                    mCurrentLocationMarker.remove();
                }
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrentLocationMarker = map.addMarker(markerOptions);

                //move map camera
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 40.0f));
            }
//            if(LocationAvailability.hasLocationAvailability(intent)){
//                LocationAvailability locationAvailability = LocationAvailability.extractLocationAvailability(intent);
//                locationAvailability.
//            }
        }
    }
}
