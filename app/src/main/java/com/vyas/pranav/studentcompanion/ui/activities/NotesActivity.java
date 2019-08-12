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
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.NotesViewPagerAdapterNew;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils.setUserTheme;

public class NotesActivity extends AppCompatActivity {

    private static final String TAG = "NotesActivity";
    @BindView(R.id.viewpager_notes)
    ViewPager viewPagerNotes;
    @BindView(R.id.toolbar_notes)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserTheme(this);
        setContentView(R.layout.activity_notes);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        NotesViewPagerAdapterNew mAdapter = new NotesViewPagerAdapterNew(getSupportFragmentManager());
        viewPagerNotes.setAdapter(mAdapter);
        new Handler().postDelayed(this::showInstruction, TimeUnit.SECONDS.toMillis(1));
    }

    @OnClick(R.id.fab_notes_add_new)
    void fabClicked() {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notes_menu_help:
                BottomSheetDialog mDialog = new BottomSheetDialog(this);
                mDialog.setContentView(R.layout.item_holder_bottom_sheet_note_help);
                mDialog.show();
                return false;
        }
        return false;
    }

    private void showInstruction() {
        BubbleShowCaseBuilder help = new BubbleShowCaseBuilder(this)
                .title(getString(R.string.instr_note_help_title))
                .description(getString(R.string.instr_note_help_desc))
                .targetView(findViewById(R.id.notes_menu_help))
                .showOnce(TAG + "MenuHelp");
        BubbleShowCaseBuilder add = new BubbleShowCaseBuilder(this)
                .title((getString(R.string.instr_notes_add_title)))
                .description(getString(R.string.instr_notes_add_desc))
                .targetView(findViewById(R.id.fab_notes_add_new))
                .showOnce(TAG + "AddNote");

        new BubbleShowCaseSequence()
                .addShowCase(help)
                .addShowCase(add)
                .show();
    }
}



