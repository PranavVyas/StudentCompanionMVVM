package com.vyas.pranav.studentcompanion.ui.activities;
/*
Student Companion - An Android App that has features like attendance manager, note manager etc
Copyright (C) 2019  Pranav Vyas

This file is a part of Student Companion.

Student Companion is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Student Companion is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.
*/

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils.setUserTheme;

public class ImportExportActivity extends AppCompatActivity {

    @BindView(R.id.tv_import_export_status)
    TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserTheme(this);
        setContentView(R.layout.activity_import_export);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_import_export_status)
    void exportClicked() {
        backUpFiles();
    }

    void backUpFiles() {
        tvStatus.setText("Backing Up Now!");
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        copy(
                                ImportExportActivity.this.getExternalFilesDir(null).getPath() + MainDatabase.DB_NAME
                                , Environment.getExternalStorageDirectory().getPath()
                        );
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    void copy(String sourcePath, String destinationPath) {

        MainDatabase.getInstance(this).close();

        File dbMain = new File(sourcePath);
//        File dbShm = new File(dbMain.getParent(), MainDatabase.DB_NAME + "-shm");
//        File dbWal = new File(dbMain.getParent(), MainDatabase.DB_NAME + "-wal");

        File dbDest = new File(destinationPath, MainDatabase.DB_NAME);
//        File dbShmDest = new File(dbDest.getParent(), MainDatabase.DB_NAME + "-shm");
//        File dbWalDest = new File(dbDest.getParent(), MainDatabase.DB_NAME + "-wal");

        try {
            FileUtils.copyFile(dbMain, dbDest);
            tvStatus.setText("Back up Done!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
