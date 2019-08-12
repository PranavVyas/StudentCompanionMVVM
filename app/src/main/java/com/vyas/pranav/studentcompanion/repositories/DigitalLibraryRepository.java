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

import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryDao;
import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryEntry;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

import java.util.List;

public class DigitalLibraryRepository {
    private final Context context;
    private final DigitalLibraryDao digitalLibraryDao;
    private final AppExecutors mExecutors;
    private static final Object LOCK = new Object();
    private static DigitalLibraryRepository instance;

    public static DigitalLibraryRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new DigitalLibraryRepository(context.getApplicationContext());
            }
        }
        return instance;
    }
    public DigitalLibraryRepository(Context context) {
        this.context = context.getApplicationContext();
        this.digitalLibraryDao = MainDatabase.getInstance(context).digitalLibraryDao();
        mExecutors = AppExecutors.getInstance();
    }

    public DataSource.Factory<Integer, DigitalLibraryEntry> getAllBooks() {
        return digitalLibraryDao.getAllBooks();
    }

    public void replaceAllTheBooks(List<DigitalLibraryEntry> booksFirestore) {
        mExecutors.diskIO().execute(() -> {
            digitalLibraryDao.deleteAllBooks();
            digitalLibraryDao.insertAllBooks(booksFirestore);
        });
    }

    public DataSource.Factory<Integer, DigitalLibraryEntry> getBookByName(String searchName) {
        return digitalLibraryDao.getBookByName(searchName);
    }
}
