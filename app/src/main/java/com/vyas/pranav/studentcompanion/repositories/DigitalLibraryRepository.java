package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryDao;
import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryDatabase;
import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryEntry;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;

import java.util.List;

import androidx.lifecycle.LiveData;

public class DigitalLibraryRepository {
    private Context context;
    private DigitalLibraryDao digitalLibraryDao;
    private FirestoreQueryLiveData queryLiveData;
    private AppExecutors mExecutors;

    public DigitalLibraryRepository(Context context) {
        this.context = context;
        this.digitalLibraryDao = DigitalLibraryDatabase.getInstance(context).digitalLibraryDao();
        mExecutors = AppExecutors.getInstance();
    }

    public LiveData<List<DigitalLibraryEntry>> getAllBooks() {
        return digitalLibraryDao.getAllBooks();
    }

    public void startSync() {
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
