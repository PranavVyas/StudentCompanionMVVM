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

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.activities.OpenSourceInformationActivity;
import com.vyas.pranav.studentcompanion.utils.GlideApp;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class AboutThisAppFragment extends Fragment {

    @BindView(R.id.image_about_app_icon)
    ImageView imageIcon;

    public AboutThisAppFragment() {
    }

    public static AboutThisAppFragment newInstance() {
        return new AboutThisAppFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_this_app, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GlideApp.with(this)
                .load(R.mipmap.ic_launcher)
                .circleCrop()
                .into(imageIcon);
    }

    @OnClick(R.id.tv_about_app_tos)
    void termsOfServicesClicked() {
        Intent openTos = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.terms_url)));
        if (openTos.resolveActivity(getContext().getPackageManager()) != null) {
            getContext().startActivity(openTos);
        } else {
            Toast.makeText(getContext(), "Please Install any browser application", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.tv_about_app_website)
    void websiteClicked() {
        Intent openWebsite = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.website_url)));
        if (openWebsite.resolveActivity(getContext().getPackageManager()) != null) {
            getContext().startActivity(openWebsite);
        } else {
            Toast.makeText(getContext(), "Please Install any browser application", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.tv_about_app_privacy)
    void privacyClicked() {
        Intent openPrivacy = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.policy_url)));
        if (openPrivacy.resolveActivity(getContext().getPackageManager()) != null) {
            getContext().startActivity(openPrivacy);
        } else {
            Toast.makeText(getContext(), "Please Install any browser application", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.tv_about_app_open_source_license)
    void openSourceLicensesClicked() {
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
        Intent intent = new Intent(getContext(), OpenSourceInformationActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_about_app_make_better)
    void helpBetterClicked() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "pranavvyas4399@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Mail From Student Companion From <PUT YOUR NAME HERE>");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hey Pranav,\n\nI recently installed Student Companion and have some doubts about it. They are\n<LIST OUT YOUR CONCERNS/DOUBTS/SUGGESTIONS/REQUESTS HERE>");
        startActivity(Intent.createChooser(emailIntent, "Send email using ..."));
    }

    private void showSnackbar(String message) {
        Snackbar.make(imageIcon, message, Snackbar.LENGTH_SHORT).show();
    }

    @OnLongClick(R.id.textView34)
    void openDeveloperTools() {
        if (SharedPreferencesUtils.getInstance(getContext()).isDeveloperEnabled()) {
            SharedPreferencesUtils.getInstance(getContext()).setDeveloperEnabled(false);
            showSnackbar("Developer Tools Disabled");
        } else {
            SharedPreferencesUtils.getInstance(getContext()).setDeveloperEnabled(true);
            showSnackbar("Developer Tools Enabled");
        }
    }
}
