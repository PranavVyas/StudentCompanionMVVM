package com.vyas.pranav.studentcompanion.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.activities.DigitalLibraryActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResourcesFragment extends Fragment {


    public ResourcesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resources, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick(R.id.card_resources_fragment_digital_library)
    void digitalLibraryClicked() {
        Logger.d("OnDigitalLibraryClicked");
        Intent openDigitalLibrary = new Intent(getContext(), DigitalLibraryActivity.class);
        startActivity(openDigitalLibrary);
        //TODO Create Logic for Digital Library starting
    }


}
