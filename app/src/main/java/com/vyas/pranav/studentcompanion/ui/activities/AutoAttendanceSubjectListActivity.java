package com.vyas.pranav.studentcompanion.ui.activities;

import android.os.Bundle;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.AutoAttendanceSubjectListRecyclerAdapter;
import com.vyas.pranav.studentcompanion.repositories.SharedPreferencesRepository;
import com.vyas.pranav.studentcompanion.viewmodels.AutoAttendanceSubjectListViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
        SharedPreferencesRepository.setUserTheme(this);
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
}
