package com.vyas.pranav.studentcompanion.data.digitallibrarydatabase;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DigitalLibraryDao {

    @Query("SELECT * FROM `Digital Library`")
    DataSource.Factory<Integer, DigitalLibraryEntry> getAllBooks();

    @Query("DELETE FROM `Digital Library`")
    void deleteAllBooks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllBooks(List<DigitalLibraryEntry> booksList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOneBook(DigitalLibraryEntry bookEntry);

    @Query("SELECT * FROM  `Digital Library` WHERE bookName LIKE :searchName")
    DataSource.Factory<Integer, DigitalLibraryEntry> getBookByName(String searchName);

}
