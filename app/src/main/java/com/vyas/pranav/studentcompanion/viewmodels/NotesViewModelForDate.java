package com.vyas.pranav.studentcompanion.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vyas.pranav.studentcompanion.data.models.NotesEntry;
import com.vyas.pranav.studentcompanion.repositories.NoteRepository;

import java.util.Date;
import java.util.List;

public class NotesViewModelForDate extends ViewModel {

    private Context context;
    private Date date;
    private NoteRepository repository;
    private LiveData<List<NotesEntry>> notes;

    public NotesViewModelForDate(Context context, Date date) {
        this.context = context;
        this.date = date;
        repository = new NoteRepository(context);
        notes = repository.getNotesBeforeDate(date);
    }

    public LiveData<List<NotesEntry>> getNotes() {
        return notes;
    }
}
