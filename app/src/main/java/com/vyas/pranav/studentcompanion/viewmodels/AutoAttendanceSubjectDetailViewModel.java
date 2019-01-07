package com.vyas.pranav.studentcompanion.viewmodels;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.repositories.AutoAttendanceRepository;
import com.vyas.pranav.studentcompanion.repositories.GeoFencingRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AutoAttendanceSubjectDetailViewModel extends ViewModel {

    private AutoAttendanceRepository repo;
    private Context context;
    private String currAddress;
    private String currName;
    private GeoFencingRepository mRepo;

    public AutoAttendanceSubjectDetailViewModel(Context context, GoogleApiClient googleApiClient) {
        this.context = context;
        repo = new AutoAttendanceRepository(context);
        this.currAddress = "Please Select";
        this.currName = "Please Select";
        mRepo = new GeoFencingRepository(context, googleApiClient);
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
