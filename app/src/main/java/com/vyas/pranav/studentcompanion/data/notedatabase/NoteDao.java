package com.vyas.pranav.studentcompanion.data.notedatabase;
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
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM Notes")
    DataSource.Factory<Integer, NotesEntry> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(NotesEntry note);

    @Query("SELECT * FROM Notes WHERE date > :date")
    DataSource.Factory<Integer, NotesEntry> getNotesBefore(Date date);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(NotesEntry notesEntry);

    @Delete
    void deleteNote(NotesEntry note);

}
