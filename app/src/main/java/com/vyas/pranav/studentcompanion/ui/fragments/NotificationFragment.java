package com.vyas.pranav.studentcompanion.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.viewmodels.NotificationsViewModel;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationFragment extends Fragment implements ViewPager.OnPageChangeListener {

    @BindView(R.id.viewpager_notifications_frag)
    ViewPager viewPager;
    @BindView(R.id.tabs_notifiations_frag_viewpager_tab)
    TabLayout tabs;
    int selectedPage = 0;
    private NotificationsViewModel notificationsViewModel;


    public NotificationFragment() {
        // Required empty public constructor
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
        NotificationViewPagerAdapter mAdapter = new NotificationViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(mAdapter);
        selectedPage = notificationsViewModel.getSelectedPage();
        viewPager.setCurrentItem(selectedPage);
        viewPager.addOnPageChangeListener(this);
        tabs.setupWithViewPager(viewPager);
    }

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

        public NotificationViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    AllNotificationFragment fragment = new AllNotificationFragment();
                    Bundle sendData = new Bundle();
                    sendData.putString(Constants.KEY_SEND_DATA_TO_NOTIFICATION_CLASS_DATE, ConverterUtils.convertDateToString(new Date()));
                    fragment.setArguments(sendData);
                    return fragment;

                case 1:
                    return new AllNotificationFragment();
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
