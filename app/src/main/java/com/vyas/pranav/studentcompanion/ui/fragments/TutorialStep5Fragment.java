package com.vyas.pranav.studentcompanion.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vyas.pranav.studentcompanion.R;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

public class TutorialStep5Fragment extends Fragment {


    public TutorialStep5Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial_step5, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
