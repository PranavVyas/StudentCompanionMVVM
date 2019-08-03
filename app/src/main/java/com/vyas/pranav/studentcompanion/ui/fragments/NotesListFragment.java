package com.vyas.pranav.studentcompanion.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.NoteRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.models.NotesEntry;
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
        NoteRecyclerAdapter mAdapter = new NoteRecyclerAdapter();
        mAdapter.setOnNotesDeleteClickedListener(this);
        rvNotesList.setAdapter(mAdapter);
        rvNotesList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        if (getArguments().getInt(EXTRA_NOTES_DISPLAY_TYPE, TYPE_ALL_NOTES_SHOW) == TYPE_TILL_TODAY_NOTES_SHOW) {
            long dateInMillis = new Date().getTime() - TimeUnit.DAYS.toMillis(1);
            NotesViewModelFactory factory = new NotesViewModelFactory(new Date(dateInMillis), getContext());
            NotesViewModelForDate viewModelForDate = ViewModelProviders.of(getActivity(), factory).get(NotesViewModelForDate.class);
            viewModelForDate.getNotes().observe(this, mAdapter::submitList);
        } else {
            NoteViewModel viewModel = ViewModelProviders.of(getActivity()).get(NoteViewModel.class);
            viewModel.getAllNotes().observe(this, mAdapter::submitList);
        }
    }

    @Override
    public void OnNotesDeleteClicked(NotesEntry note) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            MainDatabase.getInstance(getContext()).noteDao().deleteNote(note);
            AppExecutors.getInstance().mainThread().execute(() -> Toast.makeText(getContext(), "Note was deleted successfully!", Toast.LENGTH_SHORT).show());
        });
    }
}
