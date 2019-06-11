package com.vyas.pranav.studentcompanion.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.NotificationsRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.NotificationEntry;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.viewmodels.NotificationsForDateViewModelFactory;
import com.vyas.pranav.studentcompanion.viewmodels.NotificationsViewModel;
import com.vyas.pranav.studentcompanion.viewmodels.NotificationsViewModelForDate;

import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AllNotificationFragment extends Fragment {


    @BindView(R.id.recycler_all_notifications)
    RecyclerView rvMain;

    private NotificationsRecyclerAdapter mAdapter;
    private NotificationsViewModel notificationsViewModel;
    private NotificationsViewModelForDate notificationsViewModelForDate;

    public AllNotificationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_notification, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle receivedData = getArguments();
        Date date;
        setUpRecyclerView();
        if (receivedData != null && receivedData.containsKey(Constants.KEY_SEND_DATA_TO_NOTIFICATION_CLASS_DATE)) {
            date = ConverterUtils.convertStringToDate(receivedData.getString(Constants.KEY_SEND_DATA_TO_NOTIFICATION_CLASS_DATE));
            NotificationsForDateViewModelFactory factory = new NotificationsForDateViewModelFactory(date, getContext());
            notificationsViewModelForDate = ViewModelProviders.of(getActivity(), factory).get(NotificationsViewModelForDate.class);
            initDataForDate();
        } else {
            notificationsViewModel = ViewModelProviders.of(getActivity()).get(NotificationsViewModel.class);
            initData();
        }

    }

    private void initDataForDate() {
        LiveData<List<NotificationEntry>> notificationTillDate = notificationsViewModelForDate.getNotificationFromDate();
        notificationTillDate.observe(this, new Observer<List<NotificationEntry>>() {
            @Override
            public void onChanged(List<NotificationEntry> notificationEntries) {
                mAdapter.submitList(notificationEntries);
            }
        });
    }

    private void setUpRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvMain.setLayoutManager(llm);
        mAdapter = new NotificationsRecyclerAdapter();
        rvMain.setAdapter(mAdapter);
    }

    private void initData() {
        LiveData<List<NotificationEntry>> allNotifications = notificationsViewModel.getAllNotifications();
        allNotifications.observe(this, new Observer<List<NotificationEntry>>() {
            @Override
            public void onChanged(List<NotificationEntry> notificationEntries) {
                mAdapter.submitList(notificationEntries);
            }
        });
    }
}
