package com.vyas.pranav.studentcompanion.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.MarketPlaceSellRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.bookdatabase.firebase.ItemModel;
import com.vyas.pranav.studentcompanion.ui.activities.MarketPlaceSellItemActivity;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;
import com.vyas.pranav.studentcompanion.viewmodels.MarketPlaceViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarketPlaceFragment extends Fragment {

    @BindView(R.id.recycler_marketplace_fragment_search_results)
    RecyclerView rvResults;
    @BindView(R.id.et_marketplace_search_query)
    TextInputEditText etSearch;
    @BindView(R.id.spinner_marketplace_fragment_category)
    Spinner spinnerCategory;
    @BindView(R.id.text_input_marketplace_search_query)
    TextInputLayout inputSearchTag;

    private MarketPlaceSellRecyclerAdapter mAdapter;
    private FirebaseFirestore mFirestore;
    private CollectionReference mCollectionReference;
    private AppExecutors mExecutors = AppExecutors.getInstance();

    private MarketPlaceViewModel marketPlaceViewModel;

    private String selectedCategory;
    private String searchStr;
    private List<String> categories = new ArrayList<>(Arrays.asList(
            "Book",
            "Bicycle",
            "Xerox"
    ));


    public MarketPlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market_place, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        marketPlaceViewModel = ViewModelProviders.of(getActivity()).get(MarketPlaceViewModel.class);
        populateUI();
    }

    private void populateUI() {
        setUpRecyclerView();
        setUpSpinner();

        searchStr = marketPlaceViewModel.getSearchStr();
        selectedCategory = categories.get(marketPlaceViewModel.getSelectedCategory());

        etSearch.setText(searchStr);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchStr = s.toString();
                marketPlaceViewModel.setSearchStr(searchStr);
            }
        });
    }

    private void setUpSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setSelection(marketPlaceViewModel.getSelectedCategory());
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories.get(position);
                marketPlaceViewModel.setSelectedCategory(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = categories.get(0);
                marketPlaceViewModel.setSelectedCategory(0);
            }
        });
    }

    private void setUpRecyclerView() {
        mAdapter = new MarketPlaceSellRecyclerAdapter();
        mAdapter.setHasStableIds(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvResults.setAdapter(mAdapter);
        rvResults.setLayoutManager(llm);
    }

    @OnClick(R.id.floatingActionButton)
    void selectImage() {
        Intent openNewItem = new Intent(getContext(), MarketPlaceSellItemActivity.class);
        startActivity(openNewItem);
    }

    @OnClick(R.id.btn_marketplace_search)
    void searchBtnClicked() {
        startFetchingData();
    }

    private void startFetchingData() {
        mFirestore = FirebaseFirestore.getInstance();
        mCollectionReference = mFirestore.collection("sell");
        Query query;
        if (!searchStr.isEmpty()) {
            query = mCollectionReference.whereEqualTo("name", searchStr).whereEqualTo("category", selectedCategory);
        } else {
            query = mCollectionReference.whereEqualTo("category", selectedCategory);
        }
        LiveData<QuerySnapshot> firestoreQueryLiveData = new FirestoreQueryLiveData(query);
        MediatorLiveData<List<ItemModel>> listLiveData = new MediatorLiveData<>();
        listLiveData.addSource(firestoreQueryLiveData, new Observer<QuerySnapshot>() {
            @Override
            public void onChanged(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    mExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            listLiveData.postValue(queryDocumentSnapshots.toObjects(ItemModel.class));
                        }
                    });
                } else {
                    listLiveData.setValue(null);
                }
            }
        });
//                Transformations.map(firestoreQueryLiveData,new Deserializer());
        listLiveData.observe(this, new Observer<List<ItemModel>>() {
            @Override
            public void onChanged(List<ItemModel> itemModels) {
                if (itemModels != null) {
                    mAdapter.setItems(itemModels);
                }
            }
        });
    }

    private class Deserializer implements Function<QuerySnapshot, List<ItemModel>> {

        @Override
        public List<ItemModel> apply(QuerySnapshot queryDocumentSnapshots) {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            List<ItemModel> items = new ArrayList<>();
            for (int i = 0; i < documents.size(); i++) {
                ItemModel item = documents.get(i).toObject(ItemModel.class);
                Logger.json(new Gson().toJson(item));
                items.add(item);
            }
            return items;
        }
    }
}
