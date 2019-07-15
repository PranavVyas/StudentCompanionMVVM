package com.vyas.pranav.studentcompanion.ui.fragments;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.activities.ContainerActivity;
import com.vyas.pranav.studentcompanion.ui.activities.DigitalLibraryActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResourcesFragment extends Fragment {

    private static final String TAG = "ResourcesFragment";


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
        startInstruction(getActivity());
    }

    @OnClick(R.id.card_resources_fragment_digital_library)
    void digitalLibraryClicked() {
        Logger.d("OnDigitalLibraryClicked");
        Intent openDigitalLibrary = new Intent(getContext(), DigitalLibraryActivity.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity())
                .toBundle();
        startActivity(openDigitalLibrary);
    }

    @OnClick(R.id.card_resources_fragment_timetable)
    void timetableClicked() {
        Logger.d("On Timetable Clicked");
        Intent intent = new Intent(getContext(), ContainerActivity.class);
        intent.putExtra(ContainerActivity.KEY_EXTRA_RESIURCE_TO_CONTAINER_ACTIVITY, ContainerActivity.TIME_TABLE);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity())
                .toBundle();
        startActivity(intent);
    }

    @OnClick(R.id.card_resources_fragment_buy_sell)
    void marketpplaceClicked() {
        Intent intent = new Intent(getContext(), ContainerActivity.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity())
                .toBundle();
        intent.putExtra(ContainerActivity.KEY_EXTRA_RESIURCE_TO_CONTAINER_ACTIVITY, ContainerActivity.MARKETPLACE_ACTIVITY);
        startActivity(intent);
    }

    @OnClick(R.id.card_resources_fragment_holidays)
    void holidayClicked() {
        Intent intent = new Intent(getContext(), ContainerActivity.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity())
                .toBundle();
        intent.putExtra(ContainerActivity.KEY_EXTRA_RESIURCE_TO_CONTAINER_ACTIVITY, ContainerActivity.HOLIDAYS);
        startActivity(intent);
    }

    private void startInstruction(Activity activity) {

        BubbleShowCaseBuilder digital = new BubbleShowCaseBuilder(activity)
                .title(getContext().getString(R.string.instr_res_digital_lib_title))
                .description(getContext().getString(R.string.instr_res_digital_lib_desc))
                .targetView(activity.findViewById(R.id.card_resources_fragment_digital_library))
                .showOnce(TAG + "digitalLibrary");
        BubbleShowCaseBuilder buySell = new BubbleShowCaseBuilder(activity)
                .title(getContext().getString(R.string.instr_res_buy_sell_title))
                .description(getContext().getString(R.string.instr_res_buy_sell_desc))
                .targetView(activity.findViewById(R.id.card_resources_fragment_buy_sell))
                .showOnce(TAG + "buySellItems");
        BubbleShowCaseBuilder holiday = new BubbleShowCaseBuilder(activity)
                .title(getContext().getString(R.string.instr_res_holiday_title))
                .description(getContext().getString(R.string.instr_res_holiday_desc))
                .showOnce(TAG + "holidays");
        BubbleShowCaseBuilder timetable = new BubbleShowCaseBuilder(activity)
                .title(getContext().getString(R.string.instr_res_timetable_title))
                .description(getContext().getString(R.string.instr_res_timetable_desc))
                .targetView(activity.findViewById(R.id.card_resources_fragment_timetable))
                .showOnce(TAG + "timetable");

        new BubbleShowCaseSequence()
                .addShowCase(digital)
                .addShowCase(buySell)
                .addShowCase(timetable)
                .addShowCase(holiday)
                .show();
    }
}
