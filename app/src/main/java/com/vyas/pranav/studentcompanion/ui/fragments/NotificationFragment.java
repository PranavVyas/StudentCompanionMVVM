package com.vyas.pranav.studentcompanion.ui.fragments;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.viewmodels.NotificationsViewModel;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class NotificationFragment extends Fragment implements ViewPager.OnPageChangeListener {

    @BindView(R.id.viewpager_notifications_frag)
    ViewPager viewPager;
    @BindView(R.id.tabs_notifications_frag_viewpager_tab)
    TabLayout tabs;
    int selectedPage = 0;
    private NotificationsViewModel notificationsViewModel;


    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance() {

        Bundle args = new Bundle();

        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        notificationsViewModel = ViewModelProviders.of(getActivity()).get(NotificationsViewModel.class);
        NotificationViewPagerAdapter mAdapter = new NotificationViewPagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(mAdapter);
        selectedPage = notificationsViewModel.getSelectedPage();
        viewPager.setCurrentItem(selectedPage);
        viewPager.addOnPageChangeListener(this);
        tabs.setupWithViewPager(viewPager);
    }

//    @OnClick(R.id.fab_notification_add_event)
//    void addEventClicked(){
//        Intent intent = new Intent(getContext(), AddEventActivity.class);
//        startActivity(intent);
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        Toast.makeText(getContext(), "Page Selected "+position, Toast.LENGTH_SHORT).show();
        selectedPage = position;
        notificationsViewModel.setSelectedPage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class NotificationViewPagerAdapter extends FragmentStatePagerAdapter {

        NotificationViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AllNotificationFragment.newInstance(ConverterUtils.convertDateToString(new Date()));

                case 1:
                    return AllNotificationFragment.newInstance(null);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Current Notifications";

                case 1:
                    return "All Notifications";
            }
            return super.getPageTitle(position);
        }
    }
}
