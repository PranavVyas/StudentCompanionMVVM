package com.vyas.pranav.studentcompanion.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.vyas.pranav.studentcompanion.data.models.NotesEntry;

import java.util.Date;
import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM Notes")
    LiveData<List<NotesEntry>> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(NotesEntry note);

    @Query("SELECT * FROM Notes WHERE date > :date")
    LiveData<List<NotesEntry>> getNotesBefore(Date date);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(NotesEntry notesEntry);

    @Delete
    void deleteNote(NotesEntry note);
}
