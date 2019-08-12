package com.vyas.pranav.studentcompanion.data.notedatabase;

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
