package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class MarketPlaceSellItemViewModel extends AndroidViewModel {

    private Uri imageUri = null;
    private String downloadUri = null;
    private UploadTask uploadTask = null;
    private FirebaseUser currUser = null;
    private long progress = -1;
    private String childRefString;

    public MarketPlaceSellItemViewModel(@NonNull Application application) {
        super(application);
        currUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getChildRefString() {
        return childRefString;
    }

    public void setChildRefString(String childRefString) {
        this.childRefString = childRefString;
    }

    public void setProgress(long progress) {
        this.progress = progress;
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
}
