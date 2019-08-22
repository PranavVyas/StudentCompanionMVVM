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

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.NotificationsRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.firestore.NotificationFirestoreModel;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;
import com.vyas.pranav.studentcompanion.viewmodels.NotificationsViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllNotificationFragment extends Fragment {


    @BindView(R.id.recycler_all_notifications)
    RecyclerView rvMain;
    @BindView(R.id.placeholder_all_attendance)
    ConstraintLayout placeHolder;
    @BindView(R.id.constraint_all_notification_main)
    ConstraintLayout mainView;

    private NotificationsRecyclerAdapter mAdapter;
    private NotificationsViewModel notificationsViewModel;
    private Date date;
//    private NotificationsViewModelForDate notificationsViewModelForDate;

    public AllNotificationFragment() {
    }

    public static AllNotificationFragment newInstance(String dateStr) {
        AllNotificationFragment allNotificationFragment = new AllNotificationFragment();
        if (dateStr != null) {
            Bundle args = new Bundle();
            args.putString(Constants.KEY_SEND_DATA_TO_NOTIFICATION_CLASS_DATE, dateStr);
            allNotificationFragment.setArguments(args);
        }
        return allNotificationFragment;
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

    private void setUpRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvMain.setLayoutManager(llm);
        mAdapter = new NotificationsRecyclerAdapter();
        rvMain.setAdapter(mAdapter);
    }

    private void initData() {
        LiveData<List<NotificationFirestoreModel>> allNotisFromDb = notificationsViewModel.getAllNotisFromDb();
        allNotisFromDb.observe(getActivity(), notificationFirestoreModels -> {
            List<NotificationFirestoreModel> finalNotis;
            finalNotis = notificationFirestoreModels;
            if (date != null) {
                finalNotis = new ArrayList<>();
                for (NotificationFirestoreModel x : notificationFirestoreModels) {
                    if (Long.parseLong(x.getDateInMillis()) - (new Date().getTime()) > -1) {
                        finalNotis.add(x);
                    }
                }
                if (finalNotis.size() == 0) {
                    showPlaceHolder(true);
                } else {
                    showPlaceHolder(false);
                }
                mAdapter.submitList(finalNotis);
                return;
            }
            if (finalNotis.size() == 0) {
                showPlaceHolder(true);
            } else {
                showPlaceHolder(false);
            }
            mAdapter.submitList(finalNotis);
        });

        FirestoreQueryLiveData firestoreLiveData = notificationsViewModel.getFirestoreLiveData();
        firestoreLiveData.observe(getActivity(), queryDocumentSnapshots -> {
            List<NotificationFirestoreModel> notificationFirestoreModels = new ArrayList<>();
            List<String> listOfIds = new ArrayList<>();
            for (DocumentSnapshot x :
                    queryDocumentSnapshots.getDocuments()) {
                listOfIds.add(x.getId());
                notificationFirestoreModels.add(x.toObject(NotificationFirestoreModel.class));
            }
            notificationsViewModel.syncNotificationDatabase(listOfIds, notificationFirestoreModels);
        });
    }

    private void showPlaceHolder(boolean isShown) {
        if (isShown) {
            placeHolder.setVisibility(View.VISIBLE);
            mainView.setVisibility(View.GONE);
        } else {
            placeHolder.setVisibility(View.GONE);
            mainView.setVisibility(View.VISIBLE);
        }
    }

}
