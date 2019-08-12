package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryEntry;
import com.vyas.pranav.studentcompanion.repositories.DigitalLibraryRepository;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.util.List;

public class DigitalLibraryViewModel extends AndroidViewModel {

    private final DigitalLibraryRepository repository;
    private final SharedPreferencesUtils sharedPreferencesUtils;
    private FirestoreQueryLiveData liveBooksData;
    private CollectionReference booksRef;

    public DigitalLibraryViewModel(@NonNull Application application) {
        super(application);
        repository = DigitalLibraryRepository.getInstance(application);
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(application);
        booksRef = FirebaseFirestore.getInstance().collection(SharedPreferencesUtils.getInstance(application).getCurrentPath() + Constants.PATH_DIGITAL_LIBRARY_SVNIT);
        liveBooksData = new FirestoreQueryLiveData(booksRef);
    }

    public LiveData<PagedList<DigitalLibraryEntry>> getAllBooks() {
        return new LivePagedListBuilder<>(repository.getAllBooks(), Constants.PAGE_SIZE_DIGITAL_LIBRARY).build();
    }

    public void replaceAllBooks(List<DigitalLibraryEntry> booksFirestore) {
        repository.replaceAllTheBooks(booksFirestore);
    }

    public LiveData<PagedList<DigitalLibraryEntry>> getBookByName(String searchName) {
        searchName = "%" + searchName + "%";
        return new LivePagedListBuilder<>(repository.getBookByName(searchName), Constants.PAGE_SIZE_DIGITAL_LIBRARY).build();
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

    public FirestoreQueryLiveData getLiveDataOfBooks() {
        return liveBooksData;
    }

    public void nullLiveData() {
        this.liveBooksData = null;
    }

    public FirestoreQueryLiveData refreshLiveData() {
        nullLiveData();
        this.liveBooksData = new FirestoreQueryLiveData(booksRef);
        return liveBooksData;
    }
}
