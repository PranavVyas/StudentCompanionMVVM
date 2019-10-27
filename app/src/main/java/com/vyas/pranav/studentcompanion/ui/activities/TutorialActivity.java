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

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughActivity;
import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughCard;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.viewmodels.TutorialViewModel;

import java.util.Arrays;
import java.util.List;

public class TutorialActivity extends FancyWalkthroughActivity {

    private TutorialViewModel tutorialViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tutorialViewModel = ViewModelProviders.of(this).get(TutorialViewModel.class);
        if (tutorialViewModel.isTutorialComplete()) {
            startMainActivity();
            return;
        }

        FancyWalkthroughCard welcomeCard = new FancyWalkthroughCard(getString(R.string.tut_welcome_title), getString(R.string.tut_welcome_desc), R.drawable.ic_logo);
        FancyWalkthroughCard attendance = new FancyWalkthroughCard(getString(R.string.tut_att_man_title), getString(R.string.tut_att_man_desc), R.drawable.ic_today_attendance);
        FancyWalkthroughCard smartSilent = new FancyWalkthroughCard(getString(R.string.tut_smart_silent_title), getString(R.string.tut_smart_silent_desc), R.drawable.ic_mute);
        FancyWalkthroughCard autoAttendance = new FancyWalkthroughCard(getString(R.string.tut_auto_att_title), getString(R.string.tut_auto_att_desc), R.drawable.ic_auto_attendance_map);
        FancyWalkthroughCard resources = new FancyWalkthroughCard(getString(R.string.tut_resources_title), getString(R.string.tut_resources_desc), R.drawable.ic_resources_magic_wand);
        FancyWalkthroughCard digitalLibrary = new FancyWalkthroughCard(getString(R.string.tut_digital_library_title), getString(R.string.tut_digital_library_desc), R.drawable.ic_bookshelf);
        FancyWalkthroughCard buySellItem = new FancyWalkthroughCard(getString(R.string.tut_buy_sell_title), getString(R.string.tut_buy_sell_desc), R.drawable.ic_market_place);
        FancyWalkthroughCard notes = new FancyWalkthroughCard(getString(R.string.tut_note_title), getString(R.string.tut_note_desc), R.drawable.ic_note_full);
        FancyWalkthroughCard overallAttendance = new FancyWalkthroughCard(getString(R.string.tut_overall_att_title), getString(R.string.tut_overall_att_desc), R.drawable.ic_overall_attendance);
        FancyWalkthroughCard finalCard = new FancyWalkthroughCard(getString(R.string.tut_that_s_more_title), getString(R.string.tut_that_s_more_desc), R.drawable.ic_gift);

        List<FancyWalkthroughCard> list = Arrays.asList(
                welcomeCard,
                attendance,
                smartSilent,
                autoAttendance,
                resources,
                digitalLibrary,
                buySellItem,
                notes,
                overallAttendance,
                finalCard);

        for (FancyWalkthroughCard page : list) {
            page.setTitleColor(R.color.black);
            page.setDescriptionColor(R.color.black);
        }
        setColorBackground(R.color.colorBackGroundTutorial);
        //Show/Hide navigation controls
        showNavigationControls(true);
        //Set pager indicator colors
        setInactiveIndicatorColor(R.color.grey_200);
        setActiveIndicatorColor(R.color.white);
        //Set finish button text
        setFinishButtonTitle(getString(R.string.tut_get_started));
        //Set the finish button style
        setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.shape_rounded_button_margin_big));
        setOnboardPages(list);
    }

    @Override
    public void onFinishButtonPressed() {
        startMainActivity();
    }

    private void startMainActivity() {
        Intent startMain = new Intent(this, MainActivity.class);
        startActivity(startMain);
        tutorialViewModel.setTutorialComplete(true);
        finish();
    }
}