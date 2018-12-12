package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import com.vyas.pranav.studentcompanion.utils.NavigationDrawerUtil;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AttendanceIndividualViewModel extends AndroidViewModel {

    private int currentFragmentId;

    public AttendanceIndividualViewModel(@NonNull Application application) {
        super(application);
        currentFragmentId = NavigationDrawerUtil.ID_TODAY_ATTENDANCE;
    }

    public int getCurrentFragmentId() {
        return currentFragmentId;
    }

    public void setCurrentFragmentId(int currentFragmentId) {
        this.currentFragmentId = currentFragmentId;
    }
}
