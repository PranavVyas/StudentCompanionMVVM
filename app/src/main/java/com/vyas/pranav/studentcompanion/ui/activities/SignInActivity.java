package com.vyas.pranav.studentcompanion.ui.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.repositories.SharedPreferencesRepository;
import com.vyas.pranav.studentcompanion.viewmodels.SignInViewModel;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    SignInViewModel signInViewModel;

    @BindView(R.id.toolbar_sign_in_activity)
    Toolbar toolbarSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesRepository.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarSignIn);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        signInViewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
        checkForUserSignIn();
    }

    @OnClick(R.id.btn_sign_in_activity_sign_in)
    void signInClicked() {
        checkForUserSignIn();
    }

    private void checkForUserSignIn() {
        if (signInViewModel.getCurrUser() == null) {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    //new AuthUI.IdpConfig.PhoneBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build()
                    //new AuthUI.IdpConfig.FacebookBuilder().build(),
                    //new AuthUI.IdpConfig.TwitterBuilder().build()
            );
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(false)
                            .setLogo(R.drawable.ic_logo)
                            .build(),
                    RC_SIGN_IN, bundle);
        } else {
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            Intent startSetUp = new Intent(this, SetUpActivity.class);
            startActivity(startSetUp, bundle);
            finish();
        }
    }

    private void showSnackbar(String message) {
        Snackbar sbar = Snackbar.make(toolbarSignIn, message, Snackbar.LENGTH_SHORT);
        sbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                showSnackbar("User Signed In");
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
                Intent startSetUp = new Intent(this, SetUpActivity.class);
                startActivity(startSetUp, bundle);
                finish();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar("Cancelled");
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar("No network");
                    return;
                }

                Logger.d("Sign In Error Occurred : " + response.getError());
            }
        }
    }
}
