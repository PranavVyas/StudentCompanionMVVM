package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryDao;
import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryEntry;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;

import java.util.List;

public class DigitalLibraryRepository {
    private final Context context;
    private final DigitalLibraryDao digitalLibraryDao;
    private final AppExecutors mExecutors;

    public DigitalLibraryRepository(Context context) {
        this.context = context.getApplicationContext();
        this.digitalLibraryDao = MainDatabase.getInstance(context).digitalLibraryDao();
        mExecutors = AppExecutors.getInstance();
    }

    public LiveData<List<DigitalLibraryEntry>> getAllBooks() {
        return digitalLibraryDao.getAllBooks();
    }

    public void replaceAllTheBooks(List<DigitalLibraryEntry> booksFirestore) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                digitalLibraryDao.deleteAllBooks();
                digitalLibraryDao.insertAllBooks(booksFirestore);
            }
        });
    }

    public LiveData<List<DigitalLibraryEntry>> getBookByName(String searchName) {
        return digitalLibraryDao.getBookByName(searchName);
    }
}
