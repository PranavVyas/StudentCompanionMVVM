package com.vyas.pranav.studentcompanion.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QuerySnapshot;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.NotificationsRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.firestore.NotificationFirestoreModel;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;
import com.vyas.pranav.studentcompanion.viewmodels.NotificationsViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllNotificationFragment extends Fragment {


    @BindView(R.id.recycler_all_notifications)
    RecyclerView rvMain;

    private NotificationsRecyclerAdapter mAdapter;
    private NotificationsViewModel notificationsViewModel;
    private Date date;
//    private NotificationsViewModelForDate notificationsViewModelForDate;

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
        setUpRecyclerView();
        notificationsViewModel = ViewModelProviders.of(getActivity()).get(NotificationsViewModel.class);
        if (receivedData != null && receivedData.containsKey(Constants.KEY_SEND_DATA_TO_NOTIFICATION_CLASS_DATE)) {
            date = ConverterUtils.convertStringToDate(receivedData.getString(Constants.KEY_SEND_DATA_TO_NOTIFICATION_CLASS_DATE));
//            NotificationsForDateViewModelFactory factory = new NotificationsForDateViewModelFactory(date, getContext());
//            notificationsViewModelForDate = ViewModelProviders.of(getActivity(), factory).get(NotificationsViewModelForDate.class);
//            initDataForDate();
        }
        initData();
    }

//    private void initDataForDate() {
//        LiveData<List<NotificationEntry>> notificationTillDate = notificationsViewModelForDate.getNotificationFromDate();
//        notificationTillDate.observe(this, new Observer<List<NotificationEntry>>() {
//            @Override
//            public void onChanged(List<NotificationEntry> notificationEntries) {
//                mAdapter.submitList(notificationEntries);
//            }
//        });
//    }

    private void setUpRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvMain.setLayoutManager(llm);
        mAdapter = new NotificationsRecyclerAdapter();
        rvMain.setAdapter(mAdapter);
    }

    private void initData() {
        FirestoreQueryLiveData firestoreLiveData = notificationsViewModel.getFirestoreLiveData();
        firestoreLiveData.observe(this, new Observer<QuerySnapshot>() {
            @Override
            public void onChanged(QuerySnapshot queryDocumentSnapshots) {
                List<NotificationFirestoreModel> notificationFirestoreModels = queryDocumentSnapshots.toObjects(NotificationFirestoreModel.class);
                List<NotificationFirestoreModel> finalNotis = notificationFirestoreModels;
                if (date != null) {
                    finalNotis = new ArrayList<>();
                    Calendar calNow = Calendar.getInstance();
                    //TODO set Today's notifications in curent notis
                    calNow.setTime(new Date());
                    Calendar calInNoti = Calendar.getInstance();
                    for (NotificationFirestoreModel x : notificationFirestoreModels) {
                        calInNoti.setTime(ConverterUtils.convertStringToDate(x.getDate()));
                        if (calInNoti.after(calNow)) {
                            finalNotis.add(x);
                        }
                    }
                    mAdapter.submitList(finalNotis);
                }
                mAdapter.submitList(finalNotis);
            }
        });
//        LiveData<List<NotificationEntry>> allNotifications = notificationsViewModel.getAllNotifications();
//        allNotifications.observe(this, new Observer<List<NotificationEntry>>() {
//            @Override
//            public void onChanged(List<NotificationEntry> notificationEntries) {
//            }
//        });

    }

}
