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
import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

public class MyProfileViewModel extends AndroidViewModel {

    private final FirebaseFirestore mFirestoreDb;
    private final Query mCollectionReference;
    private final FirestoreQueryLiveData mListLiveData;
    private final FirebaseAuth mAuth;
    private final FirebaseUser currUser;
    private final String userName;

    public MyProfileViewModel(@NonNull Application application) {
        super(application);
        mFirestoreDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        userName = currUser.getDisplayName();
        mCollectionReference = mFirestoreDb.collection(SharedPreferencesUtils.getInstance(application).getCurrentPath() + Constants.PATH_SELL_SVNIT).whereEqualTo("p_name", userName);
        mListLiveData = new FirestoreQueryLiveData(mCollectionReference);
    }

    public FirestoreQueryLiveData getmListLiveData() {
        return mListLiveData;
    }

    public void deleteItem(String id) {
        mFirestoreDb.collection(SharedPreferencesUtils.getInstance(getApplication()).getCurrentPath() + Constants.PATH_SELL_SVNIT).document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Logger.d("Successfully deleted item...");
                        Toast.makeText(getApplication(), "Successfully deleted item from list", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Logger.d("Error Occurred while deleting document from cloud : " + e.toString());
                        Toast.makeText(getApplication(), "Error Occurred while deleting document from cloud...", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public FirebaseUser getCurrUser() {
        return currUser;
    }
}
