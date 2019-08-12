package com.vyas.pranav.studentcompanion.data.digitallibrarydatabase;
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
