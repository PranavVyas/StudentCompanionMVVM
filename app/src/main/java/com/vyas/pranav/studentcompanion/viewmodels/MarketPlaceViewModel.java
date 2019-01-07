package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class MarketPlaceViewModel extends AndroidViewModel {

    private final List<String> categories = new ArrayList<>(Arrays.asList(
            "Book",
            "Bicycle",
            "Xerox"
    ));
    private String searchStr = "";
    private int selectedCategory = 0;
    private CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("sell");
    private Query mQuery = collectionReference.whereEqualTo("category", categories.get(selectedCategory));
    private FirestoreQueryLiveData queryLiveData;


    public MarketPlaceViewModel(@NonNull Application application) {
        super(application);
        queryLiveData = new FirestoreQueryLiveData(mQuery);
    }

    public FirestoreQueryLiveData getNewQueryLiveDataIfChanged(String searchStr, String selectedCategory) {
        if (searchStr.equals(this.searchStr) && selectedCategory.equals(categories.get(this.selectedCategory))) {
            return queryLiveData;
        } else {
            setNewQueryLiveData(searchStr, selectedCategory);
            return queryLiveData;
        }
    }

    public void setNewQueryLiveData(String searchStr, String selectedCategory) {
        if (!searchStr.isEmpty()) {
            mQuery = collectionReference.whereEqualTo("name", searchStr).whereEqualTo("category", selectedCategory);
        } else {
            mQuery = collectionReference.whereEqualTo("category", selectedCategory);
        }
        this.queryLiveData = new FirestoreQueryLiveData(mQuery);
    }

    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }

    public int getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(int position) {
        this.selectedCategory = position;
    }
}
