package com.vyas.pranav.studentcompanion.ui.fragments;

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

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.activities.OpenSourceInformationActivity;
import com.vyas.pranav.studentcompanion.utils.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutThisAppFragment extends Fragment {

    @BindView(R.id.image_about_app_icon)
    ImageView imageIcon;

    public AboutThisAppFragment() {
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
}
