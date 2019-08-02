package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.vyas.pranav.studentcompanion.data.NoteDao;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.models.NotesEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

import java.util.Date;
import java.util.List;

public class NoteRepository {
    private Context context;
    private NoteDao noteDao;
    private AppExecutors mExecutors;

    public NoteRepository(Context context) {
        this.context = context;
        noteDao = MainDatabase.getInstance(context).noteDao();
        mExecutors = AppExecutors.getInstance();
    }

    public void insertNote(NotesEntry note) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                noteDao.insertNote(note);
            }
        });
    }

    public LiveData<List<NotesEntry>> getAllNotes() {
        return noteDao.getAllNotes();
    }

    public LiveData<List<NotesEntry>> getNotesBeforeDate(Date date) {
        return noteDao.getNotesBefore(date);
    }

    public void updateNote(NotesEntry notesEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                noteDao.update(notesEntry);
            }
        });
    }
}
