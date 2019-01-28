package com.vyas.pranav.studentcompanion.ui.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.utils.GlideApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutDeveloperFragment extends Fragment {

    @BindView(R.id.image_about_developer_dp)
    ImageView imageDeveloper;

    public AboutDeveloperFragment() {
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
                .load(R.drawable.developer_image)
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
}
