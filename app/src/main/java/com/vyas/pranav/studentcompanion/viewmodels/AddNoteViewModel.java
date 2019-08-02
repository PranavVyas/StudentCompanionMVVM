package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.vyas.pranav.studentcompanion.data.models.NotesEntry;
import com.vyas.pranav.studentcompanion.repositories.NoteRepository;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

public class AddNoteViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private String title;
    private String desc;
    private String date = Constants.DATE_BTN_DEFAULT;
    private long id = -1;

    public AddNoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void addNote(NotesEntry notesEntry) {
        repository.insertNote(notesEntry);
    }

    public void setNote(NotesEntry note) {
        this.date = ConverterUtils.convertDateToString(note.getDate());
        this.desc = note.getDesc();
        this.title = note.getTitle();
        this.id = note.getId();
    }

    public void updateNote(NotesEntry notesEntry) {
        repository.updateNote(notesEntry);
    }
}
