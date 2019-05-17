package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class UploadBookViewModel extends AndroidViewModel {

    private Uri imageUri = null;
    private String downloadUri = null;
    private UploadTask uploadTask = null;
    private FirebaseUser currUser = null;
    private long progress = 0;
    private String authorName, bookName, subject, extraInfo, downloadUriString;

    public UploadBookViewModel(@NonNull Application application) {
        super(application);
        currUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public FirebaseUser getCurrUser() {
        return currUser;
    }

    public void setCurrUser(FirebaseUser currUser) {
        this.currUser = currUser;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }

    public UploadTask getUploadTask() {
        return uploadTask;
    }

    public void setUploadTask(UploadTask uploadTask) {
        this.uploadTask = uploadTask;
    }

    public void setCurrProgress(long progress) {
        this.progress = progress;
    }

    public long getProgress() {
        return progress;
    }

    public String getDownloadUriString() {
        return downloadUriString;
    }

    public void setDownloadUriString(String downloadUriString) {
        this.downloadUriString = downloadUriString;
    }
}
