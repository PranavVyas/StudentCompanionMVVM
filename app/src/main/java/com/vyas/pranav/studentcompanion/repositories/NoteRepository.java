package com.vyas.pranav.studentcompanion.repositories;
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

import androidx.paging.DataSource;

import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.notedatabase.NoteDao;
import com.vyas.pranav.studentcompanion.data.notedatabase.NotesEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

import java.util.Date;

public class NoteRepository {
    private Context context;
    private NoteDao noteDao;
    private AppExecutors mExecutors;
//    public static final Object LOCK = new Object();
//
//    private static NoteRepository instance;

//    public static NoteRepository getInstance(Context context) {
//        if (instance == null) {
//            synchronized (LOCK) {
//                instance = new NoteRepository(context.getApplicationContext());
//            }
//        }
//        return instance;
//    }

    public NoteRepository(Context context) {
        this.context = context;
        noteDao = MainDatabase.getInstance(context).noteDao();
        mExecutors = AppExecutors.getInstance();
    }

    public void insertNote(NotesEntry note) {
        mExecutors.diskIO().execute(() -> noteDao.insertNote(note));
    }

    public DataSource.Factory<Integer, NotesEntry> getAllNotes() {
        return noteDao.getAllNotes();
    }

    public DataSource.Factory<Integer, NotesEntry> getNotesBeforeDate(Date date) {
        return noteDao.getNotesBefore(date);
    }

    public void updateNote(NotesEntry notesEntry) {
        mExecutors.diskIO().execute(() -> noteDao.update(notesEntry));
    }
}
