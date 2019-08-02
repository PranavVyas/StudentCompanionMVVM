package com.vyas.pranav.studentcompanion.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.vyas.pranav.studentcompanion.data.models.NotesEntry;
import com.vyas.pranav.studentcompanion.repositories.NoteRepository;

import java.util.Date;

import static com.vyas.pranav.studentcompanion.utils.Constants.PAGE_SIZE_NOTES;

public class NotesViewModelForDate extends ViewModel {

    private Context context;
    private Date date;
    private NoteRepository repository;
    private LiveData<PagedList<NotesEntry>> notes;

    public NotesViewModelForDate(Context context, Date date) {
        this.context = context;
        this.date = date;
        repository = new NoteRepository(context);
        notes = new LivePagedListBuilder<>(repository.getNotesBeforeDate(date), PAGE_SIZE_NOTES).build();
    }

    public LiveData<PagedList<NotesEntry>> getNotes() {
        return notes;
    }
}
