package com.vyas.pranav.studentcompanion.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.repositories.SharedPreferencesRepository;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.viewmodels.AutoAttendanceSubjectDetailViewModel;
import com.vyas.pranav.studentcompanion.viewmodels.AutoAttendanceSubjectDetailViewModelFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
        , OnMapReadyCallback
        , LocationListener {

    private static final int RC_LOCATION_ACCESS = 3000;
    private static final int RC_CHANGE_LOCATION = 4000;
    @BindView(R.id.tv_auto_attendance_subject_detail_address)
    TextView tvAddress;
    @BindView(R.id.tv_auto_attendance_subject_detail_name)
    TextView tvName;
    @BindView(R.id.tv_auto_attendance_subject_detail_subject)
    TextView tvSubject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesRepository.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_attendance_subject_detail);
        ButterKnife.bind(this);
        AutoAttendanceSubjectDetailViewModelFactory factory = new AutoAttendanceSubjectDetailViewModelFactory(this, this);
        autoAttendanceSubjectDetailViewModel = ViewModelProviders.of(this, factory).get(AutoAttendanceSubjectDetailViewModel.class);
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        checkForPermission();
        setUpGoogleMap();
        currPlace = null;
    }

    private void setUpGoogleMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        geoFenceLimits = null;
    }

    private void setUpGoogleClient() {
        mClient = autoAttendanceSubjectDetailViewModel.getGoogleClient();
    }

    private void checkForPermission() {
        if (ActivityCompat.checkSelfPermission(AutoAttendanceSubjectDetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, RC_LOCATION_ACCESS);
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
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000000);
        mLocationRequest.setFastestInterval(1000000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mClient, mLocationRequest, this);
        }
        populateUI(getIntent());
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
        if (mClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mClient, this);
        }
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
        mClient.connect();

    }

    @Override
    public void onLocationChanged(Location location) {
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
}
