package com.vyas.pranav.studentcompanion.ui.fragments;


import android.app.Activity;
import android.app.ActivityOptions;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.MarketPlaceSellRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.itemdatabase.firebase.ItemModel;
import com.vyas.pranav.studentcompanion.ui.activities.MarketPlaceSellItemActivity;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.AttendanceUtils;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;
import com.vyas.pranav.studentcompanion.viewmodels.MarketPlaceViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class MarketPlaceFragment extends Fragment {

    @BindView(R.id.recycler_marketplace_fragment_search_results)
    RecyclerView rvResults;
    @BindView(R.id.et_marketplace_search_query)
    TextInputEditText etSearch;
    @BindView(R.id.spinner_marketplace_fragment_category)
    Spinner spinnerCategory;
    @BindView(R.id.text_input_marketplace_search_query)
    TextInputLayout inputSearchTag;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton fab;
    private static final String TAG = "MarketPlaceFragment";

    private MarketPlaceSellRecyclerAdapter mAdapter;
    private final AppExecutors mExecutors = AppExecutors.getInstance();
    @BindView(R.id.placeholder_marketplace_no_items)
    ConstraintLayout placeHolder;

    private MarketPlaceViewModel marketPlaceViewModel;
    private FirestoreQueryLiveData firestoreQueryLiveData;

    private String selectedCategory;
    private String searchStr;
    private final List<String> categories = new ArrayList<>(Arrays.asList(
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

    @SuppressWarnings("SameParameterValue")
    void showSnackbar(String message) {
        Snackbar.make(inputSearchTag, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        marketPlaceViewModel = ViewModelProviders.of(getActivity()).get(MarketPlaceViewModel.class);
        populateUI();
        getLiveData();
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                if (!AttendanceUtils.hasInternetAccess(getContext())) {
                    showSnackbar("Internet not available. Latest sync Failed");
                }
            }
        });
        startInstruction(getActivity());
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_simple_custom_main, categories);
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
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvResults.setAdapter(mAdapter);
        rvResults.setLayoutManager(llm);
    }

    @OnClick(R.id.floatingActionButton)
    void selectImage() {
        Intent openNewItem = new Intent(getContext(), MarketPlaceSellItemActivity.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
        startActivityForResult(openNewItem, Constants.RC_OPEN_MARKET_PLACE_NEW_AD, bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.RC_OPEN_MARKET_PLACE_NEW_AD) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getContext(), "Successfully Add to Database", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED && data != null) {
//                String childPath = data.getStringExtra(MarketPlaceSellItemActivity.EXTRA_DOWNLOAD_URL);
//                Logger.d("Path of image is " + childPath);
//                if (childPath != null) {
//                    StorageReference ref = FirebaseStorage.getInstance().getReference().child(childPath);
//                    ref.delete().addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(getContext(), "Removed from database", Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(getActivity(), new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getContext(), "Error from database", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.btn_marketplace_search)
    void searchBtnClicked() {
        startFetchingData();
    }

    private void startFetchingData() {
//        mFirestore = FirebaseFirestore.getInstance();
//        mCollectionReference = mFirestore.collection("sell");
//        Query query;
//        if (!searchStr.isEmpty()) {
//            query = mCollectionReference.whereEqualTo("name", searchStr).whereEqualTo("category", selectedCategory);
//        } else {
//            query = mCollectionReference.whereEqualTo("category", selectedCategory);
//        }
//        LiveData<QuerySnapshot> firestoreQueryLiveData = new FirestoreQueryLiveData(query);
        marketPlaceViewModel.setNewQueryLiveData(searchStr, selectedCategory);
        getLiveData();
    }

    private void getLiveData() {
        firestoreQueryLiveData = marketPlaceViewModel.getQueryLiveData();
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
                    mAdapter.submitList(itemModels);
                    if (itemModels.size() == 0) {
                        showPlaceHolder(true);
                    } else {
                        showPlaceHolder(false);
                    }
                } else {
                    Toast.makeText(getContext(), "Some Error occured while fetching list", Toast.LENGTH_SHORT).show();
                    Logger.d("Error Occured while loading");
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

    private void startInstruction(Activity activity) {
        BubbleShowCaseBuilder market = new BubbleShowCaseBuilder(activity)
                .title(getContext().getString(R.string.instr_market_title))
                .description(getContext().getString(R.string.instr_market_desc))
                .showOnce(TAG + "Market");
        BubbleShowCaseBuilder searchTag = new BubbleShowCaseBuilder(activity)
                .title(getContext().getString(R.string.instr_market_search_title))
                .description(getContext().getString(R.string.instr_market_search_desc))
                .targetView(inputSearchTag)
                .showOnce(TAG + "SearchTag");
        BubbleShowCaseBuilder Fab = new BubbleShowCaseBuilder(activity)
                .title(getContext().getString(R.string.instr_market_fab_title))
                .description(getContext().getString(R.string.instr_market_fab_desc))
                .targetView(fab)
                .showOnce(TAG + "FAB");
        new BubbleShowCaseSequence()
                .addShowCase(searchTag)
                .addShowCase(Fab)
                .addShowCase(market)
                .show();
    }

    private void showPlaceHolder(boolean isShown) {
        if (isShown) {
            placeHolder.setVisibility(View.VISIBLE);
            rvResults.setVisibility(View.GONE);
        } else {
            placeHolder.setVisibility(View.GONE);
            rvResults.setVisibility(View.VISIBLE);
        }
    }
}
