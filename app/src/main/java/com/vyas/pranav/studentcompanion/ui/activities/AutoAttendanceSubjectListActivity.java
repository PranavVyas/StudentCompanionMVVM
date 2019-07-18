package com.vyas.pranav.studentcompanion.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.schibstedspain.leku.LocationPickerActivityKt;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.AutoAttendanceSubjectListRecyclerAdapter;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.viewmodels.AutoAttendanceSubjectListViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AutoAttendanceSubjectListActivity extends AppCompatActivity {

    @BindView(R.id.recycler_auto_attendance_main)
    RecyclerView rvMain;
    @BindView(R.id.toolbar_auto_attendance_subject_list)
    Toolbar toolbar;

    private AutoAttendanceSubjectListRecyclerAdapter mAdapter;

    private AutoAttendanceSubjectListViewModel autoAttendanceSubjectListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesUtils.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_attendance_subject_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        autoAttendanceSubjectListViewModel = ViewModelProviders.of(this).get(AutoAttendanceSubjectListViewModel.class);
        populateUI();
    }

    private void populateUI() {
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mAdapter = new AutoAttendanceSubjectListRecyclerAdapter();
        rvMain.setAdapter(mAdapter);
        rvMain.setLayoutManager(llm);
        mAdapter.setSubjectList(autoAttendanceSubjectListViewModel.getSubjectList());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RC_OPEN_PLACE_PICKER_CUSTOM && resultCode == RESULT_OK) {
            if (data != null) {
//                Place place = PlacePicker.getPlace(this, data);
//                currPlace.setPlaceId(place.getId());
//                autoAttendanceSubjectDetailViewModel.updatePlaceId(currPlace);
//                autoAttendanceSubjectDetailViewModel.refreshAllGeoFences();
                double lat = data.getDoubleExtra(LocationPickerActivityKt.LATITUDE, 0.0);
                double lang = data.getDoubleExtra(LocationPickerActivityKt.LONGITUDE, 0.0);
                Logger.d("Location is : " + lat + " , " + lang);
                //autoAttendanceSubjectDetailViewModel.refreshGeoFences(place);
            } else {
                Toast.makeText(this, "Error Occurred While Retrieving Data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
