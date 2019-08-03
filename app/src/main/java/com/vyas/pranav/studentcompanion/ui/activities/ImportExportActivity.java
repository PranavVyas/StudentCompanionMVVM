package com.vyas.pranav.studentcompanion.ui.activities;

import android.Manifest;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;

import static com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils.setUserTheme;

public class ImportExportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserTheme(this);
        setContentView(R.layout.activity_import_export);
        ButterKnife.bind(this);
        backUpFiles();
    }

    void backUpFiles() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        copyDbToDownloads();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    void copyDbToDownloads() {
        MainDatabase.getInstance(this).close();

        File dbMain = new File(this.getExternalFilesDir(null).getPath() + MainDatabase.DB_NAME);
        File dbShm = new File(dbMain.getParent(), MainDatabase.DB_NAME + "-shm");
        File dbWal = new File(dbMain.getParent(), MainDatabase.DB_NAME + "-wal");

        File dbDest = new File("/sdcard/", MainDatabase.DB_NAME);
        File dbShmDest = new File(dbDest.getParent(), MainDatabase.DB_NAME + "-shm");
        File dbWalDest = new File(dbDest.getParent(), MainDatabase.DB_NAME + "-wal");

        try {
            FileUtils.copyFile(dbMain, dbDest);
            FileUtils.copyFile(dbShm, dbShmDest);
            FileUtils.copyFile(dbWal, dbWalDest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
