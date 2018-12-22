package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vyas.pranav.studentcompanion.utils.NavigationDrawerUtil;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AttendanceIndividualViewModel extends AndroidViewModel {

    private int currentFragmentId;
    private FirebaseUser currUser;
    private FirebaseAuth mAuth;
    private Application application;

    public AttendanceIndividualViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        currentFragmentId = NavigationDrawerUtil.ID_TODAY_ATTENDANCE;
        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
    }

    public int getCurrentFragmentId() {
        return currentFragmentId;
    }

    public void setCurrentFragmentId(int currentFragmentId) {
        this.currentFragmentId = currentFragmentId;
    }

    public FirebaseUser getCurrUser() {
        return currUser;
    }

    public void setCurrUser(FirebaseUser currUser) {
        this.currUser = currUser;
    }

}
