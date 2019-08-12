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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.NoteRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.notedatabase.NotesEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.viewmodels.NoteViewModel;
import com.vyas.pranav.studentcompanion.viewmodels.NotesViewModelFactory;
import com.vyas.pranav.studentcompanion.viewmodels.NotesViewModelForDate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesListFragment extends Fragment implements NoteRecyclerAdapter.OnNotesDeleteClickedListener {

    public static final String EXTRA_NOTES_DISPLAY_TYPE = "SHOW_NOTES_FROM_DAY";
    public static final int TYPE_ALL_NOTES_SHOW = 0;
    public static final int TYPE_TILL_TODAY_NOTES_SHOW = 1;
    @BindView(R.id.rv_notes_list_fragment_list)
    RecyclerView rvNotesList;

    @BindView(R.id.placeholder_notes)
    ConstraintLayout placeHolder;

    private NoteRecyclerAdapter mAdapter;

    public NotesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new NoteRecyclerAdapter();
        mAdapter.setOnNotesDeleteClickedListener(this);
        rvNotesList.setAdapter(mAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvNotesList.setLayoutManager(llm);
        if (getArguments().getInt(EXTRA_NOTES_DISPLAY_TYPE, TYPE_ALL_NOTES_SHOW) == TYPE_TILL_TODAY_NOTES_SHOW) {
            long dateInMillis = new Date().getTime() - TimeUnit.DAYS.toMillis(1);
            NotesViewModelFactory factory = new NotesViewModelFactory(new Date(dateInMillis), getContext());
            NotesViewModelForDate viewModelForDate = ViewModelProviders.of(getActivity(), factory).get(NotesViewModelForDate.class);
            viewModelForDate.getNotes().observe(this, this::submit);
        } else {
            NoteViewModel viewModel = ViewModelProviders.of(getActivity()).get(NoteViewModel.class);
            viewModel.getAllNotes().observe(this, this::submit);
        }
    }

    private void submit(PagedList<NotesEntry> notesEntryList) {
        if (notesEntryList.size() > 0) {
            showPlaceHolder(false);
            mAdapter.submitList(notesEntryList);
        } else {
            showPlaceHolder(true);
        }
    }

    private void showPlaceHolder(boolean isShown) {
        if (isShown) {
            rvNotesList.setVisibility(View.GONE);
            placeHolder.setVisibility(View.VISIBLE);
        } else {
            rvNotesList.setVisibility(View.VISIBLE);
            placeHolder.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnNotesDeleteClicked(NotesEntry note) {
        new MaterialAlertDialogBuilder(getContext())
                .setMessage("Do you want to delete note?\nYou can not undo action afterwards.")
                .setTitle(R.string.title_attention)
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        MainDatabase.getInstance(getContext()).noteDao().deleteNote(note);
                        AppExecutors.getInstance().mainThread().execute(() -> Toast.makeText(getContext(), "Note was deleted successfully!", Toast.LENGTH_SHORT).show());
                    });
                }).setNegativeButton("No", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        }).show();

    }
}
