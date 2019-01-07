package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;
import android.net.Uri;

import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class MarketPlaceSellItemViewModel extends AndroidViewModel {

    private Uri imageUri = null;
    private String downloadUri = null;
    private UploadTask uploadTask = null;

    public MarketPlaceSellItemViewModel(@NonNull Application application) {
        super(application);
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
}
