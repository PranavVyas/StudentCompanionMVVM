package com.vyas.pranav.studentcompanion.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;
import com.vyas.pranav.studentcompanion.R;

import butterknife.ButterKnife;

public class AutoAttendanceFenceActivity extends AppCompatActivity {

    GoogleApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_attendance_fence);
        ButterKnife.bind(this);
        connectApiClient();
        createFence();
    }

    private void createFence() {
//        LocationFence locationFence = LocationFence.
    }

    private void connectApiClient() {
        if (apiClient == null) {
            apiClient = new GoogleApiClient.Builder(this)
                    .addApi(Awareness.API)
                    .build();
            apiClient.connect();
        }
    }


}
