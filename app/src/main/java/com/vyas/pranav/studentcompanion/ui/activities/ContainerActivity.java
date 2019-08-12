package com.vyas.pranav.studentcompanion.ui.activities;
/*
Student Companion - An Android App that has features like attendance manager, note manager etc
Copyright (C) 2019  Pranav Vyas

This file is a part of Student Companion.

Student Companion is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Student Companion is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.
*/
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.fragments.HolidayFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.MarketPlaceFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.TimetableFragment;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.viewmodels.ContainerViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContainerActivity extends AppCompatActivity {

    public static final int DIGITAL_LIBRARY = 1;
    public static final int TIME_TABLE = 2;
    public static final int HOLIDAYS = 3;
    public static final int MARKETPLACE_ACTIVITY = 4;
    public static final String KEY_EXTRA_RESOURCE_TO_CONTAINER_ACTIVITY = "KEY_CONTAINER_RECEIVED_EXTRA";
    @BindView(R.id.toolbar_container)
    Toolbar toolbar;
    @BindView(R.id.tv_toolbar_container)
    TextView tvToolbar;

    private ContainerViewModel containerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesUtils.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        containerViewModel = ViewModelProviders.of(this).get(ContainerViewModel.class);
        initLogic();
    }

    private void initLogic() {
        int receivedData = getIntent().getIntExtra(KEY_EXTRA_RESOURCE_TO_CONTAINER_ACTIVITY, 0);
        if (containerViewModel.getCurrentFragmentId() != 0) {
            containerViewModel.setCurrentFragmentId(receivedData);
        }
        containerViewModel.setCurrentFragmentId(containerViewModel.getCurrentFragmentId());
        setFragmentTitle(receivedData);
        switch (receivedData) {
            case TIME_TABLE:
                TimetableFragment timetableFragment = new TimetableFragment();
//                timetableFragment.setEnterTransition();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container_activity_container, timetableFragment, "TIME_TABLE")
                        .commit();
                break;

            case HOLIDAYS:
                HolidayFragment holidayFragment = new HolidayFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container_activity_container, holidayFragment, "HOLIDAY")
                        .commit();
                break;

            case MARKETPLACE_ACTIVITY:
                MarketPlaceFragment marketPlaceFragment = new MarketPlaceFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container_activity_container, marketPlaceFragment, "MARKET_PLACE")
                        .commit();
                break;
        }
    }

    private void setFragmentTitle(int receivedData) {
        switch (receivedData) {
            case TIME_TABLE:
                tvToolbar.setText(R.string.time_table);
                break;

            case HOLIDAYS:
                tvToolbar.setText(R.string.holidays);
                break;

            case MARKETPLACE_ACTIVITY:
                tvToolbar.setText(R.string.market_place);
                break;
        }
    }

}
