package com.vyas.pranav.studentcompanion.viewmodels;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.repositories.AutoAttendanceRepository;
import com.vyas.pranav.studentcompanion.repositories.GeoFencingRepository;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AutoAttendanceSubjectDetailViewModel extends ViewModel {

    private AutoAttendanceRepository repo;
    private Context context;
    private GoogleApiClient mClient;
    private String currAddress;
    private String currName;
    private GeoFencingRepository mRepo;
    private FragmentActivity fragmentActivity;

    public AutoAttendanceSubjectDetailViewModel(Context context, FragmentActivity fragmentActivity) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        repo = new AutoAttendanceRepository(context);
        this.currAddress = "Please Select";
        this.currName = "Please Select";
        if (mClient == null) {
            mClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) context)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) context)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(fragmentActivity, (GoogleApiClient.OnConnectionFailedListener) context)
                    .build();
        }
        mRepo = new GeoFencingRepository(context, mClient);
    }

    public String getCurrAddress() {
        return currAddress;
    }

    public void setCurrAddress(String currAddress) {
        this.currAddress = currAddress;
    }

    public String getCurrName() {
        return currName;
    }

    public void setCurrName(String currName) {
        this.currName = currName;
    }

    public void insertNewPlace(AutoAttendancePlaceEntry newPlace) {
        repo.insertNewPlace(newPlace);
    }

    public LiveData<AutoAttendancePlaceEntry> getPlaceIdOFSubject(String subject) {
        return repo.getPlaceIdOfSubject(subject);
    }

    public void updatePlaceId(AutoAttendancePlaceEntry placeEntry) {
        repo.updatePlaceId(placeEntry);
    }

    public GoogleApiClient getGoogleClient() {
        return mClient;
    }

    public void refreshAllGeoFences() {
        mRepo.refreshAllGeoFences();
    }

    public void refreshGeoFences(PlaceBuffer places) {
        mRepo.updateGeoFenceList(places);
        mRepo.unregisterAllGeoFences();
        mRepo.registerAllGeoFences();
    }

    public void refreshGeoFences(Place place) {
        mRepo.updateGeoFenceList(place);
        mRepo.unregisterAllGeoFences();
        mRepo.registerAllGeoFences();
    }
}
