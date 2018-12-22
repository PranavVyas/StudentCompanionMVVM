package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class SignInViewModel extends AndroidViewModel {

    private FirebaseAuth mAuth;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getCurrUser() {
        return mAuth.getCurrentUser();
    }

}
