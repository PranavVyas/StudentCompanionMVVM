package com.vyas.pranav.studentcompanion.ui.fragments;

import android.app.Application;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class MyProfileViewModel extends AndroidViewModel {

    private FirebaseFirestore mFirestoreDb;
    private Query mCollectionReference;
    private FirestoreQueryLiveData mListLiveData;
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private String userName;

    public MyProfileViewModel(@NonNull Application application) {
        super(application);
        mFirestoreDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        userName = currUser.getDisplayName();
        mCollectionReference = mFirestoreDb.collection("sell").whereEqualTo("p_name", userName);
        mListLiveData = new FirestoreQueryLiveData(mCollectionReference);
    }

    public FirestoreQueryLiveData getmListLiveData() {
        return mListLiveData;
    }

    public void deleteItem(String id) {
        mFirestoreDb.collection("sell").document(id)
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
