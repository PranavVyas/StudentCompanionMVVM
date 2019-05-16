package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryEntry;
import com.vyas.pranav.studentcompanion.repositories.DigitalLibraryRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class DigitalLibraryViewModel extends AndroidViewModel {

    private DigitalLibraryRepository repository;

    public DigitalLibraryViewModel(@NonNull Application application) {
        super(application);
        repository = new DigitalLibraryRepository(application);
    }

    public LiveData<List<DigitalLibraryEntry>> getAllBooks() {
        return repository.getAllBooks();
    }


    public void startSync() {
        repository.startSync();
    }

    public void replaceAllBooks(List<DigitalLibraryEntry> booksFirestore) {
        repository.replaceAllTheBooks(booksFirestore);
    }

    public LiveData<List<DigitalLibraryEntry>> getBookByName(String searchName) {
        searchName = "%" + searchName + "%";
        return repository.getBookByName(searchName);
    }
}
