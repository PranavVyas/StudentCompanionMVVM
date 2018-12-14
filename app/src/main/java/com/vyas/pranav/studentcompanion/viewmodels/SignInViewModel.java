package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class SignInViewModel extends AndroidViewModel {

    FirebaseAuth mAuth;
    FirebaseUser currUser;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
    }

    public FirebaseUser getCurrUser() {
        return currUser;
    }

    public void setCurrUser(FirebaseUser currUser) {
        this.currUser = currUser;
    }
}
