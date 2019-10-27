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
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.DownloadRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.lecturedatabase.LectureEntry;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.models.DownloadModel;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.AttendanceUtils;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils.setUserTheme;

public class ImportExportActivity extends AppCompatActivity {

    public static final String EXTRA_FORCE_START_ACTIVITY = "EXTRA_FORCE_START_ACTIVITY";
    @BindView(R.id.tv_import_export_status)
    TextView tvStatus;
    @BindView(R.id.toolbar_import_export)
    Toolbar toolbar;
    @BindView(R.id.checkbox_import_export_show_tutorial)
    MaterialCheckBox showTutCheckbox;
    @BindView(R.id.image_import_export_import)
    ImageView btnImport;
    @BindView(R.id.image_import_export_export)
    ImageView btnExport;
    @BindView(R.id.btn_setup_fresh)
    Button btnStartFresh;
    @BindView(R.id.btn_import_export_continue)
    Button btnContinue;
    @BindView(R.id.textView8)
    TextView tvOr;
    @BindView(R.id.divider15)
    View divider;

    private SharedPreferencesUtils sharedPreferencesUtils;
    private MainDatabase mDb;
    private String dbPath, backDbPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserTheme(this);
        setContentView(R.layout.activity_import_export);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(this);
        dbPath = ImportExportActivity.this.getExternalFilesDir(null).getPath() + "/" + MainDatabase.DB_NAME;
        backDbPath = Environment.getExternalStorageDirectory().getPath() + "/Student Companion Backup/" + MainDatabase.DB_NAME;
        if (getIntent().getBooleanExtra(EXTRA_FORCE_START_ACTIVITY, false)) {
            tvOr.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
            btnStartFresh.setVisibility(View.GONE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            btnContinue.setVisibility(View.VISIBLE);
        } else {
            btnContinue.setVisibility(View.GONE);
            if (sharedPreferencesUtils.isRestoreDone()) {
                if (sharedPreferencesUtils.isAppFirstRun()) {
                    Intent intent = new Intent(this, SetUpActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                } else if (sharedPreferencesUtils.isTutorialDone()) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    setResult(RESULT_OK);
                    finish();
                    return;
                } else {
                    Intent intent = new Intent(this, TutorialActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        }
        checkForFiles();
        mDb = MainDatabase.getInstance(this);
    }

    private void checkForFiles() {
        if (validateFile(backDbPath)) {
            tvStatus.setText("Old Backup Available, You can restore It!");
            btnImport.setEnabled(true);
            btnImport.setImageResource(R.drawable.ic_arrow_down_rounded);
        } else {
            tvStatus.setText("No Backup Found, You can download it from website and place it in main directory in external storage");
            btnImport.setEnabled(false);
            btnImport.setImageResource(R.drawable.ic_delete);
        }
    }

    @OnClick(R.id.image_import_export_export)
    void exportClicked() {
        Animation rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_anticlockwise);
        btnExport.startAnimation(rotateAnim);
        tvStatus.setText("Backing Up Now!");
        copy(false);
    }

    @OnClick(R.id.image_import_export_import)
    void importClicked() {
        Animation rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise);
        btnImport.startAnimation(rotateAnim);
        tvStatus.setText("Restoring Files Now!");
        copy(true);
    }

    @OnClick(R.id.btn_setup_fresh)
    void continueSetupClicked() {
        Intent intent = new Intent(this, SetUpActivity.class);
        sharedPreferencesUtils.setAppFirstRun(true);
        startActivity(intent);
        sharedPreferencesUtils.setRestoreDone(true);
        finish();
    }

    @OnClick(R.id.btn_import_export_continue)
    void continueClicked() {
        if (showTutCheckbox.isChecked()) {
            Intent intent = new Intent(ImportExportActivity.this, TutorialActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(ImportExportActivity.this, MainActivity.class);
            startActivity(intent);
        }
        sharedPreferencesUtils.setAppFirstRun(false);
        sharedPreferencesUtils.setRestoreDone(true);
        Intent intent = new Intent();
        setResult(2, intent);
        finish();
    }

    private void showSnackBar(String s) {
        Snackbar.make(findViewById(android.R.id.content), s, Snackbar.LENGTH_SHORT);
    }

    void copy(boolean isRestoreMode) {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        String source = dbPath;
                        String dest = backDbPath;
                        if (isRestoreMode) {
                            source = backDbPath;
                            dest = dbPath;
                        }
                        try {
                            File dbMain = new File(source);
                            File dbDest = new File(dest);
                            File dbShm = new File(dbMain.getParent(), MainDatabase.DB_NAME + "-shm");
                            File dbWal = new File(dbMain.getParent(), MainDatabase.DB_NAME + "-wal");
                            File dbShmDest = new File(dbDest.getParent(), MainDatabase.DB_NAME + "-shm");
                            File dbWalDest = new File(dbDest.getParent(), MainDatabase.DB_NAME + "-wal");
                            FileUtils.copyFile(dbMain, dbDest);
                            FileUtils.copyFile(dbShm, dbShmDest);
                            FileUtils.copyFile(dbWal, dbWalDest);
                            if (isRestoreMode) {
                                tvStatus.setText("Restoring file done!");
                                ImportExportActivity.this.continueRestore();
                            } else {
                                tvStatus.setText("Back up Done!");
                                showSnackBar("Back up Done!");
                            }
                            checkForFiles();
                        } catch (IOException e) {
                            tvStatus.setText("Error Occurred While copying file");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        showSnackBar("Please provide me with permission!\nWrite to external storage permission is needed to copy database");
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    private void continueRestore() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            AppExecutors.getInstance().mainThread().execute(() -> {
                tvStatus.setText("Configuring Application!");
            });
            List<String> allSubjects = mDb.overallAttendanceDao().getAllSubjects();
            sharedPreferencesUtils.setSubjectListInSharedPrefrences(allSubjects);
            List<LectureEntry> lectures = mDb.lectureDao().getAll();
            for (int i = 0; i < lectures.size(); i++) {
                int lectureNo = lectures.get(i).getLectureNo();
                int startTime = lectures.get(i).getStartTime();
                int stopTime = lectures.get(i).getStopTime();
                sharedPreferencesUtils.setNoOfLecturesPerDay(lectures.size());
                sharedPreferencesUtils.setLectureStartTimeInSharedPrefs(lectureNo, startTime);
                sharedPreferencesUtils.setLectureEndTimeInSharedPrefs(lectureNo, stopTime);
            }
            sharedPreferencesUtils.setCurrentPath(mDb.metaDataDao().getMetadataOf(Constants.METADATA_CURRENT_PATH).getValue());
            sharedPreferencesUtils.setCurrentAttendanceCriteria(Integer.parseInt(mDb.metaDataDao().getMetadataOf(Constants.METADATA_ATTENDANCE_CRITERIA).getValue()));
            Date firstDate = mDb.attendanceDao().getFirstDate();
            Date lastDate = mDb.attendanceDao().getLastDate();
            sharedPreferencesUtils.setUpStartingDate(ConverterUtils.convertDateToString(firstDate));
            sharedPreferencesUtils.setUpEndingDate(ConverterUtils.convertDateToString(lastDate));
            sharedPreferencesUtils.setCurrentDay(5);
            sharedPreferencesUtils.setUpCurrentStep(5);
            sharedPreferencesUtils.setUpSemester(Integer.parseInt(mDb.metaDataDao().getMetadataOf(Constants.METADATA_SEMESTER).getValue()));
            sharedPreferencesUtils.setTutorialDone(!showTutCheckbox.isChecked());
            sharedPreferencesUtils.setRestoreDone(true);
            sharedPreferencesUtils.setAppFirstRun(false);
            AppExecutors.getInstance().mainThread().execute(() -> {
                tvStatus.setText("Successfully Restored Database!");
                Intent intent = new Intent(ImportExportActivity.this, TutorialActivity.class);
                startActivity(intent);
                ImportExportActivity.this.setResult(Activity.RESULT_OK);
                finish();
            });
        });
    }

    private boolean validateFile(String sourcePath) {
        File dbMain = new File(sourcePath);
        File dbShm = new File(dbMain.getParent(), MainDatabase.DB_NAME + "-shm");
        File dbWal = new File(dbMain.getParent(), MainDatabase.DB_NAME + "-wal");
        return dbMain.exists() && dbShm.exists() && dbWal.exists();
    }

    @OnClick(R.id.btn_setup_check_download)
    void checkAndDownloadClicked() {
        //check for internet connection
        AppExecutors.getInstance().networkIO().execute(() -> {
            if (AttendanceUtils.hasInternetAccess(this)) {
                AppExecutors.getInstance().mainThread().execute(() -> {
                    BottomSheetDialog mDialog = new BottomSheetDialog(this);
                    mDialog.setContentView(R.layout.item_holder_bottom_sheet_check_and_download);
                    mDialog.show();

                    RecyclerView rv = mDialog.findViewById(R.id.rv_bottom_holder_check_download_list);
                    LinearLayout progressContainer = mDialog.findViewById(R.id.linear_bottom_holder_check_download);
                    TextView tvProgressStatus = mDialog.findViewById(R.id.tv_bottom_holder_check_download_progress_status);

                    progressContainer.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.INVISIBLE);
                    DividerItemDecoration rvDivider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
                    tvProgressStatus.setText("Getting Available Downloads, Please Wait...");
                    DownloadRecyclerAdapter mAdapter = new DownloadRecyclerAdapter();
                    mAdapter.setOnDownloadSelectedListener(downloadModel -> {
                        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse response) {
                                        startDownloadAndImportDb(downloadModel, progressContainer, tvProgressStatus, rv);
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse response) {
                                        Toast.makeText(ImportExportActivity.this, "Writing Permission is needed to import database!", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                    }
                                }).check();
                    });

                    rv.addItemDecoration(rvDivider);
                    rv.setAdapter(mAdapter);
                    rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
                    FirebaseFirestore mRef = FirebaseFirestore.getInstance();
                    Query dataQuery = mRef.collection("dbs").document("svnit").collection("downloads");
                    FirestoreQueryLiveData availableDownloadList = new FirestoreQueryLiveData(dataQuery);
                    availableDownloadList.observe(this, queryDocumentSnapshots -> {
                        List<DownloadModel> downloadModels = queryDocumentSnapshots.toObjects(DownloadModel.class);
                        Logger.d("Available Downloads: " + downloadModels.size());
                        mAdapter.submitList(downloadModels);
                        progressContainer.setVisibility(View.INVISIBLE);
                        rv.setVisibility(View.VISIBLE);
                    });
                });
            } else {
                //Internet Connection is not available
                AppExecutors.getInstance().mainThread().execute(() -> {
                    Toast.makeText(this, "Internet Connection is Required!", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void startDownloadAndImportDb(DownloadModel downloadModel, LinearLayout progressContainer, TextView tvProgressStatus, RecyclerView rv) {
        progressContainer.setVisibility(View.VISIBLE);
        rv.setVisibility(View.INVISIBLE);
        tvProgressStatus.setText("Downloading File 1 of 3");
        String dbPath = downloadModel.getUrl();
//        String dbUri = Environment.getExternalStorageDirectory().getPath() + "/Student Companion Backup/" + MainDatabase.DB_NAME;
//        String walUri = Environment.getExternalStorageDirectory().getPath() + "/Student Companion Backup/" + MainDatabase.DB_NAME + "-wal";
//        String shmUri = Environment.getExternalStorageDirectory().getPath() + "/Student Companion Backup/" + MainDatabase.DB_NAME + "-shm";
        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        StorageReference mDbRef = mStorage.getReference().child("dbs").child(dbPath);
        try {
            String parentPath = Environment.getExternalStorageDirectory().getPath() + "/Student Companion Backup";
            File dir = new File(parentPath);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
            }
            File DbFile = new File(parentPath + "/" + MainDatabase.DB_NAME);
            File WalFile = new File(parentPath + "/" + MainDatabase.DB_NAME + "-wal");
            File ShmFile = new File(parentPath + "/" + MainDatabase.DB_NAME + "-shm");
            DbFile.createNewFile();
            WalFile.createNewFile();
            ShmFile.createNewFile();
//            FileOutputStream s = FileUtils.openOutputStream(DbFileOld);
//            s.close();
//            FileOutputStream s2 = FileUtils.openOutputStream(WalFileOld);
//            s.close();
//            FileOutputStream s3 = FileUtils.openOutputStream(DbFileOld);
//            s.close();
            mDbRef.child("MainDatabase").getFile(DbFile).addOnSuccessListener(taskSnapshot -> {
                tvProgressStatus.setText("Downloading File 2 of 3");
                mDbRef.child("MainDatabase-wal").getFile(WalFile).addOnSuccessListener(taskSnapshot1 -> {
                    tvProgressStatus.setText("Downloading File 3 of 3");
                    mDbRef.child("MainDatabase-shm").getFile(ShmFile).addOnSuccessListener(taskSnapshot2 -> {
                        tvProgressStatus.setText("Download Successful, Importing Data...");
                        importClicked();
                    }).addOnFailureListener(e -> tvProgressStatus.setText("Downloading File 3 failed, please close this and retry\nIf Problem persists Report Bug!"));
                }).addOnFailureListener(e -> tvProgressStatus.setText("Downloading File 2 failed, please close this and retry\nIf Problem persists Report Bug!"));
            }).addOnFailureListener(e -> tvProgressStatus.setText("Downloading File 1 failed, please close this and retry\nIf Problem persists Report Bug!"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
