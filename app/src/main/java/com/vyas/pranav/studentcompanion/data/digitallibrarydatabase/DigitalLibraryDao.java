package com.vyas.pranav.studentcompanion.data.digitallibrarydatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface DigitalLibraryDao {

    @Query("SELECT * FROM `Digital Library`")
    LiveData<List<DigitalLibraryEntry>> getAllBooks();

    @Query("DELETE FROM `Digital Library`")
    void deleteAllBooks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllBooks(List<DigitalLibraryEntry> booksList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOneBook(DigitalLibraryEntry bookEntry);

    @Query("SELECT * FROM  `Digital Library` WHERE bookName LIKE :searchName")
    LiveData<List<DigitalLibraryEntry>> getBookByName(String searchName);

    //TODO Add methods to access books by their properties
}
