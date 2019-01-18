package com.vyas.pranav.studentcompanion.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

public class TutorialStep2Fragment extends Fragment {


    public TutorialStep2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(com.vyas.pranav.studentcompanion.R.layout.fragment_tutorial_step2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
