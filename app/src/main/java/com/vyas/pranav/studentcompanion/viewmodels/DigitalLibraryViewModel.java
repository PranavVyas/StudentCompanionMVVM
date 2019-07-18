package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryEntry;
import com.vyas.pranav.studentcompanion.repositories.DigitalLibraryRepository;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.util.List;

public class DigitalLibraryViewModel extends AndroidViewModel {

    private final DigitalLibraryRepository repository;
    private final SharedPreferencesUtils sharedPreferencesUtils;

    public DigitalLibraryViewModel(@NonNull Application application) {
        super(application);
        repository = new DigitalLibraryRepository(application);
        sharedPreferencesUtils = new SharedPreferencesUtils(application);
    }

    public LiveData<List<DigitalLibraryEntry>> getAllBooks() {
        return repository.getAllBooks();
    }

    public void replaceAllBooks(List<DigitalLibraryEntry> booksFirestore) {
        repository.replaceAllTheBooks(booksFirestore);
    }

    public LiveData<List<DigitalLibraryEntry>> getBookByName(String searchName) {
        searchName = "%" + searchName + "%";
        return repository.getBookByName(searchName);
    }

    public void changeAutoSync(boolean isEnabled) {
        sharedPreferencesUtils.changeAutoSync(isEnabled);
    }

    public boolean getStateOfAutoSync() {
        return sharedPreferencesUtils.getStateOfAutoSync();
    }

    public boolean getFirstRunForFile(String file) {
        return sharedPreferencesUtils.isFileFirstOpened(file);
    }

    public void setFirstRunForFile(String file, boolean isFirstTimeOpened) {
        sharedPreferencesUtils.setFileFirstTimeOpened(file, isFirstTimeOpened);
    }

}
