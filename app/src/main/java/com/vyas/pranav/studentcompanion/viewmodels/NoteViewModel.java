package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.vyas.pranav.studentcompanion.data.models.NotesEntry;
import com.vyas.pranav.studentcompanion.repositories.NoteRepository;
import com.vyas.pranav.studentcompanion.utils.Constants;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private LiveData<PagedList<NotesEntry>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = new LivePagedListBuilder<>(repository.getAllNotes(), Constants.PAGE_SIZE_NOTES).build();
    }

    public LiveData<PagedList<NotesEntry>> getAllNotes() {
        return allNotes;
    }
}
