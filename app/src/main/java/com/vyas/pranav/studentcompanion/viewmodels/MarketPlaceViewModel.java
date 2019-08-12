package com.vyas.pranav.studentcompanion.viewmodels;
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
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.util.Arrays;
import java.util.List;

public class MarketPlaceViewModel extends AndroidViewModel {

    private List<String> categories;
    private String searchStr = "";
    private int selectedCategory = 0;
    private final CollectionReference collectionReference;
    private Query mQuery;
    private FirestoreQueryLiveData queryLiveData;
    private String childSting;

    public MarketPlaceViewModel(@NonNull Application application) {
        super(application);
        categories = Arrays.asList(application.getResources().getStringArray(R.array.categories_buy_sell));
        collectionReference = FirebaseFirestore.getInstance().collection(SharedPreferencesUtils.getInstance(application).getCurrentPath() + Constants.PATH_SELL_SVNIT);
        mQuery = collectionReference.whereEqualTo("category", categories.get(selectedCategory));
        queryLiveData = new FirestoreQueryLiveData(mQuery);
    }

    public String getChildSting() {
        return childSting;
    }

    public void setChildSting(String childSting) {
        this.childSting = childSting;
    }

    public FirestoreQueryLiveData getNewQueryLiveDataIfChanged(String searchStr, String selectedCategory) {
        if (searchStr.equals(this.searchStr) && selectedCategory.equals(categories.get(this.selectedCategory))) {
            return queryLiveData;
        } else {
            setNewQueryLiveData(searchStr, selectedCategory);
            return queryLiveData;
        }
    }

    public FirestoreQueryLiveData getQueryLiveData() {
        return queryLiveData;
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
