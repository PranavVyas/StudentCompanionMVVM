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

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.utils.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutDeveloperFragment extends Fragment {

    @BindView(R.id.image_about_developer_dp)
    ImageView imageDeveloper;

    public AboutDeveloperFragment() {
    }

    public static AboutDeveloperFragment newInstance() {
        return new AboutDeveloperFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_developer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GlideApp.with(this)
                .load(R.drawable.image_developer)
                .circleCrop()
                .into(imageDeveloper);
    }

    @OnClick(R.id.image_about_developer_github)
    void githubClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.profile_url_github)));
        startActivity(intent);
    }

    @OnClick(R.id.image_about_developer_facebook)
    void facebookClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.profile_url_facebook)));
        startActivity(intent);
    }

    @OnClick(R.id.image_about_developer_twitter)
    void twitterClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.profile_url_twitter)));
        startActivity(intent);
    }

    @OnClick(R.id.image_about_developer_instagram)
    void instagramClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.profile_url_instagram)));
        startActivity(intent);
    }

    @OnClick(R.id.tv_about_developer_about_me)
    void aboutMeClicked() {
        Intent openWebsite = new Intent(Intent.ACTION_VIEW);
        openWebsite.setData(Uri.parse(getString(R.string.developer_website)));
        if (openWebsite.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(openWebsite);
        } else {
            Toast.makeText(getContext(), "Install any browser application to view this", Toast.LENGTH_SHORT).show();
        }
    }
}
