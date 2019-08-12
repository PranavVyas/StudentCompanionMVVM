package com.vyas.pranav.studentcompanion.repositories;

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
