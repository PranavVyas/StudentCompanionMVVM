package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.vyas.pranav.studentcompanion.data.autoattendanceplacesdatabase.AutoAttendancePlaceEntry;
import com.vyas.pranav.studentcompanion.repositories.AutoAttendanceRepository;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class AutoAttendanceSubjectDetailViewModel extends AndroidViewModel {

    private AutoAttendanceRepository repo;
    private Context context;
    private GoogleApiClient mClient;
    private String currAddress;
    private String currName;

    public AutoAttendanceSubjectDetailViewModel(@NonNull Application application) {
        super(application);
        repo = new AutoAttendanceRepository(application);
        this.context = application;
        this.currAddress = "Please Select";
        this.currName = "Please Select";
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

    public void setGoogleClient(FragmentActivity activity, Context context) {
        if (mClient == null) {
            mClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) context)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) context)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(activity, (GoogleApiClient.OnConnectionFailedListener) context)
                    .build();
        }
    }


}
