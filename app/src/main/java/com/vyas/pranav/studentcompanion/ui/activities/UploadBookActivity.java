package com.vyas.pranav.studentcompanion.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.firebase.BookModel;
import com.vyas.pranav.studentcompanion.repositories.SharedPreferencesRepository;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.GlideApp;
import com.vyas.pranav.studentcompanion.viewmodels.UploadBookViewModel;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadBookActivity extends AppCompatActivity {

    public static final String EXTRA_DOWNLOAD_URL_DOCUMENT = "UploadBookActivity.DownloadUri";
    private static final int RC_OPEN_CHOOSER = 100;
    @BindView(R.id.text_input_upload_book_author_name)
    TextInputLayout inputAuthorName;
    @BindView(R.id.text_input_upload_book_book_name)
    TextInputLayout inputBookName;
    @BindView(R.id.text_input_upload_book_subject)
    TextInputLayout inputSubject;
    @BindView(R.id.text_input_upload_book_extra_info)
    TextInputLayout inputExtaInfo;

    @BindView(R.id.et_upload_book_author_name)
    TextInputEditText etAuthorName;
    @BindView(R.id.et_upload_book_book_name)
    TextInputEditText etBookName;
    @BindView(R.id.et_upload_book_subject)
    TextInputEditText etSubject;
    @BindView(R.id.et_upload_book_extra_info)
    TextInputEditText etExtraInfo;

    @BindView(R.id.toolbar_upload_book)
    Toolbar toolbar;
    @BindView(R.id.tv_upload_book_status)
    TextView uploadStatus;
    @BindView(R.id.btn_upload_book_upload)
    Button btnPostDocument;
    @BindView(R.id.btn_placeholder_upload_book_no_connection_retry)
    Button btnRetry;
    @BindView(R.id.btn_upload_book_select_book)
    Button btnSelectBook;
    @BindView(R.id.placeholder_upload_book_no_connection)
    ConstraintLayout placeHolderConnection;
    @BindView(R.id.scroll_upload_book_container)
    ScrollView scrollContainer;
    @BindView(R.id.image_placeholder_upload_book)
    ImageView imagePlaceHolder;

    private String authorName, bookName, subject, userName, extraInfo;
    private Uri selectedBookUri;
    private UploadBookViewModel uploadBookViewModel;
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private StorageReference mStorageReference = mStorage.getReference();
    private StorageReference child;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mCollectionReference = mFirestore.collection("digitalLibrary");
    private Snackbar sbar;
    private UploadTask uploadTask;
    private String downloadBookUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesRepository.setUserTheme(this);
        setContentView(R.layout.activity_upload_book);
        ButterKnife.bind(this);
        uploadBookViewModel = ViewModelProviders.of(this).get(UploadBookViewModel.class);
        initData();
        setUpUi();
    }

    private void initData() {
        userName = uploadBookViewModel.getCurrUser().getDisplayName();
        downloadBookUrl = uploadBookViewModel.getDownloadUri();
        bookName = uploadBookViewModel.getBookName();
        authorName = uploadBookViewModel.getAuthorName();
        subject = uploadBookViewModel.getSubject();
        extraInfo = uploadBookViewModel.getExtraInfo();
    }

    private void setUpUi() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        continueUploadDocumentIfAvailable();
        if (validateDownloadUrl()) {
            uploadStatus.setText("Selected successfully");
            btnPostDocument.setEnabled(true);
        } else {
            btnPostDocument.setEnabled(false);
            uploadStatus.setText("Please Select Document");
        }
        GlideApp.with(this)
                .load(R.drawable.image_no_connection_placeholder)
                .into(imagePlaceHolder);
    }

    private void continueUploadDocumentIfAvailable() {
        if (uploadBookViewModel.getUploadTask() != null) {
            uploadDocument();
        }
    }

    @Override
    public void onBackPressed() {
        sendRemoveDocumentIntentIfAny();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                retryClicked();
            }
        }, TimeUnit.SECONDS.toMillis(2));
    }

    @OnClick(R.id.btn_placeholder_upload_book_no_connection_retry)
    void retryClicked() {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                showPlaceHolder(!ConverterUtils.hasInternetAccess(UploadBookActivity.this));
                Logger.d("Internet Connection is " + ConverterUtils.hasInternetAccess(UploadBookActivity.this));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        sendRemoveDocumentIntentIfAny();
        return super.onSupportNavigateUp();
    }

    @OnClick(R.id.btn_upload_book_select_book)
    void selectBookClicked() {
        validateExtraInfo();
        validateAuthorName();
        validateSubject();
        if (validateName() | validateSelectedUri()) {
            String[] mimeTypes =
                    {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                            "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                            "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                            "text/plain",
                            "application/pdf",
                            "application/zip"};

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
            startActivityForResult(Intent.createChooser(intent, "Choose File"), RC_OPEN_CHOOSER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == RC_OPEN_CHOOSER) {
            if (resultCode == RESULT_OK) {
                selectedBookUri = data.getData();
                uploadBookViewModel.setImageUri(selectedBookUri);
                AppExecutors.getInstance().networkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (ConverterUtils.hasInternetAccess(UploadBookActivity.this)) {
                            startUploadingDocument();
                        } else {
                            showPlaceHolder(true);
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Something Went Wrong While Selecting File", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startUploadingDocument() {
        if (validateSelectedUri()) {
            child = mStorageReference.child("books/" + userName + "/items/" + bookName + System.currentTimeMillis() + selectedBookUri.getLastPathSegment());
            uploadBookViewModel.setDownloadUriString(child.toString());
            uploadTask = child.putFile(selectedBookUri);
            uploadBookViewModel.setUploadTask(uploadTask);
            uploadDocument();
        }
    }

    private void uploadDocument() {
        showSnackbar(uploadBookViewModel.getProgress());
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                btnSelectBook.setEnabled(false);

            }
        });
        uploadStatus.setText("Uploading Now...");
        uploadBookViewModel.getUploadTask().addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getDownloadUrl(taskSnapshot);
                uploadBookViewModel.setUploadTask(null);
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        btnSelectBook.setEnabled(true);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showSnackbar("Error while uploading document in database");
//                Toast.makeText(MarketPlaceSellItemActivity.this, "Error while uploading image in database", Toast.LENGTH_SHORT).show();
                Logger.d("Failed due to " + e.getMessage());
                uploadBookViewModel.setUploadTask(null);
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        btnSelectBook.setEnabled(true);

                    }
                });
                btnPostDocument.setEnabled(false);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                uploadBookViewModel.setCurrProgress(progress);
                if (progress == 100) {
                    uploadBookViewModel.setCurrProgress(0);
                }
                showSnackbar(progress);
            }
        });
    }

    private void getDownloadUrl(UploadTask.TaskSnapshot taskSnapshot) {
        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadBookUrl = task.getResult().toString();
                    uploadBookViewModel.setDownloadUri(downloadBookUrl);
                    btnPostDocument.setEnabled(true);
                    btnSelectBook.setEnabled(true);
                    uploadStatus.setText("Selected successfully");
                } else {
                    showSnackbar("Error While getting download link");
                    uploadStatus.setText("Upload Problem");
                    btnSelectBook.setEnabled(true);
//                    Toast.makeText(MarketPlaceSellItemActivity.this, "Error While getting download link", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showSnackbar(String message) {
        sbar = Snackbar.make(toolbar, message, Snackbar.LENGTH_SHORT);
        sbar.show();

    }

    private void showSnackbar(long progress) {
        if (sbar != null) {
            if (sbar.isShown()) {
                sbar.setText("Document is uploading now (" + progress + " % Done)");
                if (progress == 100) {
                    sbar.dismiss();
                }
                return;
            }
        }
        sbar = Snackbar.make(toolbar, "Photo is uploading now (" + progress + " % Done)", Snackbar.LENGTH_INDEFINITE);
        sbar.show();
    }

    private boolean validateSelectedUri() {
        return selectedBookUri != null;
    }

    @OnClick(R.id.btn_upload_book_upload)
    void uploadBookClicked() {
        if (validateName() & validateAuthorName() & validateSubject() & validateDownloadUrl() & validateExtraInfo()) {
            BookModel book = new BookModel();
            book.setExtra_info(extraInfo);
            book.setUploader_name(userName);
            book.setBook_name(bookName);
            book.setDownload_url(downloadBookUrl);
            book.setSubject(subject);
            book.setAuthor_name(authorName);

            mCollectionReference.add(book).addOnSuccessListener(this, new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    showSnackbar("Added to database");
                    Logger.d("Successfully added to the database");
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(EXTRA_DOWNLOAD_URL_DOCUMENT, uploadBookViewModel.getDownloadUriString());
                    UploadBookActivity.this.setResult(RESULT_OK, resultIntent);
                    UploadBookActivity.this.finish();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Logger.d("Failed to add Due to : " + e.toString());
                    showSnackbar("Failed to add data");
//                    Toast.makeText(MarketPlaceSellItemActivity.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            showSnackbar("Error while uploading");
        }
    }

    private boolean validateExtraInfo() {
        extraInfo = etExtraInfo.getText().toString().trim();
        uploadBookViewModel.setExtraInfo(extraInfo);
        if (extraInfo.isEmpty()) {
            inputExtaInfo.setError("Extra info is not Given, write N/A if not available");
            return false;
        } else {
            inputExtaInfo.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateDownloadUrl() {
        if (downloadBookUrl == null) {
            return false;
        }
        return !downloadBookUrl.isEmpty();
    }

    private boolean validateSubject() {
        subject = etSubject.getText().toString().trim();
        uploadBookViewModel.setSubject(subject);
        if (subject.isEmpty()) {
            inputSubject.setError("Subject can not be empty");
            return false;
        } else {
            inputSubject.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateAuthorName() {
        authorName = etAuthorName.getText().toString().trim();
        uploadBookViewModel.setAuthorName(authorName);
        if (authorName.isEmpty()) {
            inputAuthorName.setError("Author can not be empty");
            return false;
        } else {
            inputAuthorName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateName() {
        bookName = etBookName.getText().toString().trim();
        uploadBookViewModel.setBookName(bookName);
        if (bookName.isEmpty()) {
            inputBookName.setError("Book name can not be empty");
            return false;
        } else {
            inputBookName.setErrorEnabled(false);
            return true;
        }
    }

    private void sendRemoveDocumentIntentIfAny() {
        if (child != null) {
            removeLastUploadedDocument();
//            Intent resultIntent = new Intent();
//            resultIntent.putExtra(EXTRA_DOWNLOAD_URL, child.getPath());
//            setResult(RESULT_CANCELED, resultIntent);
//            Logger.d("Child is not null");
        } else {
            Logger.d("Child is null");
        }
    }

    private void removeLastUploadedDocument() {
        String childPath = child.getPath();
        Logger.d("Path of image is " + childPath);
        if (childPath != null) {
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(childPath);
            ref.delete().addOnSuccessListener(this, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(UploadBookActivity.this, "Removed from database", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadBookActivity.this, "Error from database", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showPlaceHolder(boolean isShown) {
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                if (isShown) {
                    placeHolderConnection.setVisibility(View.VISIBLE);
                    scrollContainer.setVisibility(View.GONE);
                } else {
                    placeHolderConnection.setVisibility(View.GONE);
                    scrollContainer.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
