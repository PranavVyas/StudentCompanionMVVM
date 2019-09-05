package com.vyas.pranav.studentcompanion.ui.activities;
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

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.viewmodels.SignInViewModel;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.utils.Constants.RC_SIGN_IN;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_sign_in_activity)
    Toolbar toolbarSignIn;
    private SignInViewModel signInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesUtils.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarSignIn);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        signInViewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
        checkForUserSignIn();
    }

    private List<AuthUI.IdpConfig> getAvailableProviders() {
        return Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                //new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
                //new AuthUI.IdpConfig.FacebookBuilder().build(),
                //new AuthUI.IdpConfig.TwitterBuilder().build()
        );
    }

    @OnClick(R.id.btn_sign_in_activity_sign_in)
    void signInClicked() {
        checkForUserSignIn();
    }

    private void checkForUserSignIn() {
        if (signInViewModel.getCurrUser() == null) {
            AuthMethodPickerLayout mLayout = new AuthMethodPickerLayout.Builder(R.layout.item_holder_login_screen)
                    .setGoogleButtonId(R.id.btn_holder_sign_in_google)
                    .setEmailButtonId(R.id.btn_holder_sign_in_email)
                    .setTosAndPrivacyPolicyId(R.id.tv_holder_sign_in_tos)
                    .build();
            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(getAvailableProviders())
                            .setIsSmartLockEnabled(false, true)
                            .setTosAndPrivacyPolicyUrls(getString(R.string.terms_url), getString(R.string.policy_url))
                            .setLogo(R.drawable.ic_logo)
                            .setAuthMethodPickerLayout(mLayout)
                            .build(),
                    RC_SIGN_IN);
        } else {
            Intent backUpActivity = new Intent(this, ImportExportActivity.class);
            startActivity(backUpActivity);
//            Intent startSetUp = new Intent(this, SetUpActivity.class);
//            startActivity(startSetUp);
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
//                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
                Intent startImportExport = new Intent(this, ImportExportActivity.class);
                startActivity(startImportExport);
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
