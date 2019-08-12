package com.vyas.pranav.studentcompanion.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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

class AllNotificationFragment extends Fragment {


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
                    if (x.getDateInMillis() - (new Date().getTime()) > -1) {
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
        firestoreLiveData.observe(getActivity(), new Observer<QuerySnapshot>() {
            @Override
            public void onChanged(QuerySnapshot queryDocumentSnapshots) {
                List<NotificationFirestoreModel> notificationFirestoreModels = new ArrayList<>();
                List<String> listOfIds = new ArrayList<>();
                for (DocumentSnapshot x :
                        queryDocumentSnapshots.getDocuments()) {
                    listOfIds.add(x.getId());
                    notificationFirestoreModels.add(x.toObject(NotificationFirestoreModel.class));
                }
                notificationsViewModel.syncNotificationDatabase(listOfIds, notificationFirestoreModels);
            }
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
