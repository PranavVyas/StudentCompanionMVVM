package com.vyas.pranav.studentcompanion.ui.fragments;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.MyProfileItemsRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.models.ItemModel;
import com.vyas.pranav.studentcompanion.utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProfileFragment extends Fragment {

    @BindView(R.id.recycler_my_profile_ad_items)
    RecyclerView rvMyAds;
    @BindView(R.id.tv_my_profile_email)
    TextView tvEmail;
    @BindView(R.id.tv_my_profile_pname)
    TextView tvName;
    @BindView(R.id.image_my_profile_dp)
    ImageView imageDp;

    private MyProfileViewModel myProfileViewModel;
    private MyProfileItemsRecyclerAdapter mAdapter;

    private List<ItemModel> items;
    private List<String> ids;

    public MyProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myProfileViewModel = ViewModelProviders.of(getActivity()).get(MyProfileViewModel.class);
        populateUI();
    }

    private void populateUI() {
        setUpRecyclerView();
        myProfileViewModel.getmListLiveData().observe(this, new Observer<QuerySnapshot>() {
            @Override
            public void onChanged(QuerySnapshot queryDocumentSnapshots) {
//                List<ItemModel> items = queryDocumentSnapshots.toObjects(ItemModel.class);
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                items = queryDocumentSnapshots.toObjects(ItemModel.class);
                List<Pair> listOfPairs = new ArrayList<>();
                for (int i = 0; i < documents.size(); i++) {
                    listOfPairs.add(Pair.create(
                            documents.get(i).getId(),
                            items.get(i)
                    ));
                }
                mAdapter.submitList(listOfPairs);
            }
        });
        FirebaseUser currUser = myProfileViewModel.getCurrUser();
        tvName.setText(currUser.getDisplayName());
        tvEmail.setText(currUser.getEmail());
        GlideApp.with(this)
                .load(currUser.getPhotoUrl())
                .circleCrop()
                .into(imageDp);
    }

    private void setUpRecyclerView() {
        mAdapter = new MyProfileItemsRecyclerAdapter();
        mAdapter.setOnItemSoldButtonClickListener(id -> myProfileViewModel.deleteItem(id));
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvMyAds.setLayoutManager(llm);
        rvMyAds.setAdapter(mAdapter);
    }
}
