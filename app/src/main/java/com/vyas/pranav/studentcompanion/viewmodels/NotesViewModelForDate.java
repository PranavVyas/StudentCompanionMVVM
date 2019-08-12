package com.vyas.pranav.studentcompanion.viewmodels;
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
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.vyas.pranav.studentcompanion.data.notedatabase.NotesEntry;
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
        repository = NoteRepository.getInstance(context);
        notes = new LivePagedListBuilder<>(repository.getNotesBeforeDate(date), PAGE_SIZE_NOTES).build();
    }

    public LiveData<PagedList<NotesEntry>> getNotes() {
        return notes;
    }
}
