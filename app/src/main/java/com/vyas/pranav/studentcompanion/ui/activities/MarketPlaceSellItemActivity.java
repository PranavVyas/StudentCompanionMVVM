package com.vyas.pranav.studentcompanion.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.bookdatabase.firebase.ItemModel;
import com.vyas.pranav.studentcompanion.utils.GlideApp;
import com.vyas.pranav.studentcompanion.viewmodels.MarketPlaceSellItemViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarketPlaceSellItemActivity extends AppCompatActivity {

    private static final int RC_SELECT_IMAGE = 13579;
    @BindView(R.id.text_input_marketplace_sell_item_name)
    TextInputLayout inputName;
    @BindView(R.id.text_input_marketplace_sell_item_item_info)
    TextInputLayout inputInfo;
    @BindView(R.id.text_input_marketplace_sell_item_phone_no)
    TextInputLayout inputPhone;
    @BindView(R.id.text_input_marketplace_sell_item_price)
    TextInputLayout inputPrice;

    @BindView(R.id.et_marketplace_sell_item_info)
    TextInputEditText etInfo;
    @BindView(R.id.et_marketplace_sell_item_name)
    TextInputEditText etName;
    @BindView(R.id.et_marketplace_sell_item_price)
    TextInputEditText etPrice;
    @BindView(R.id.et_marketplace_sell_item_phone_no)
    TextInputEditText etPhone;

    @BindView(R.id.image_marketplace_sell_item_photo)
    ImageView imageItem;
    @BindView(R.id.toolbar_marketplace_sell_item)
    Toolbar toolbar;
    @BindView(R.id.spinner_marketplace_sell_item_category)
    Spinner spinnerCategory;
    @BindView(R.id.btn_marketplace_sell_item_post_ad)
    Button btnPostAd;

    private String phoneNo, Name, Info, Price, userName, selectedCategory;
    private Uri imageUri;
    private String downloadUri;

    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private StorageReference mStorageReference = mStorage.getReference();
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mCollectionReference = mFirestore.collection("sell");
    private FirebaseAuth auth;
    private Snackbar sbar;
    private MarketPlaceSellItemViewModel marketPlaceSellItemViewModel;
    private UploadTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_sell_item);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sell Item");
        auth = FirebaseAuth.getInstance();
        userName = auth.getCurrentUser().getDisplayName();
        marketPlaceSellItemViewModel = ViewModelProviders.of(this).get(MarketPlaceSellItemViewModel.class);
        populateUI();
    }

    private void populateUI() {
        setUpSpinner();
//        etInfo.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        etName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        etPhone.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        etPrice.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        imageUri = marketPlaceSellItemViewModel.getImageUri();
        downloadUri = marketPlaceSellItemViewModel.getDownloadUri();
        GlideApp.with(this)
                .load(imageUri)
                .placeholder(R.drawable.ic_picture_sample)
                .error(R.drawable.ic_picture_sample)
                .circleCrop()
                .into(imageItem);
        continueUploadImageIfAvailable();
    }

    private void setUpSpinner() {
        List<String> categories = new ArrayList<>(Arrays.asList(
                "Book",
                "Bicycle",
                "Xerox"
        ));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = categories.get(0);
            }
        });
        spinnerCategory.setAdapter(adapter);
    }

    @OnClick(R.id.btn_marketplace_sell_item_post_ad)
    void postAdClicked() {
        phoneNo = etPhone.getText().toString().trim();
        Name = etName.getText().toString().trim();
        Info = etInfo.getText().toString().trim();
        Price = etPrice.getText().toString().trim();

        if (validateInfo() && validateName() && validatePhoneNo() && validatePrice() && validateImageUri() && validateDownloadUri()) {
            inputInfo.setErrorEnabled(false);
            inputName.setErrorEnabled(false);
            inputPrice.setErrorEnabled(false);
            inputPhone.setErrorEnabled(false);
            ItemModel book = new ItemModel();
            book.setContact(phoneNo);
            book.setExtra_info(Info);
            book.setImage_uri(downloadUri);
            book.setPrice(Float.valueOf(Price));
            book.setP_name(userName);
            book.setName(Name);
            book.setCategory(selectedCategory);

            mCollectionReference.add(book).addOnSuccessListener(this, new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(MarketPlaceSellItemActivity.this, "Added to database", Toast.LENGTH_SHORT).show();
                    Logger.d("Successfully added to the database");
                    MarketPlaceSellItemActivity.this.finish();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Logger.d("Failed to add Due to : " + e.toString());
                    Toast.makeText(MarketPlaceSellItemActivity.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            if (!validatePhoneNo()) {
                inputPhone.setError("Input Correct Phone No");
            } else {
                inputPhone.setErrorEnabled(false);
            }
            if (!validatePrice()) {
                inputPrice.setError("Input Correct Price");
            } else {
                inputPrice.setErrorEnabled(false);
            }
            if (!validateName()) {
                inputName.setError("Input Correct Name");
            } else {
                inputName.setErrorEnabled(false);
            }
            if (!validateInfo()) {
                inputInfo.setError("Input Valid Info");
            } else {
                inputInfo.setErrorEnabled(false);
            }
            if (!validateImageUri()) {
                Toast.makeText(this, "Image is not selected", Toast.LENGTH_SHORT).show();
            }
            if (!validateDownloadUri()) {
                Toast.makeText(this, "Image is not uploaded", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateDownloadUri() {
        if (downloadUri == null) {
            return false;
        }
        return !downloadUri.isEmpty();
    }

    @OnClick(R.id.image_marketplace_sell_item_photo)
    void selectImage() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, RC_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SELECT_IMAGE) {
            if (resultCode == RESULT_OK) {
                imageUri = data.getData();
                marketPlaceSellItemViewModel.setImageUri(imageUri);
                GlideApp.with(this)
                        .load(imageUri).circleCrop()
                        .into(imageItem);
                btnPostAd.setEnabled(false);
                uploadImage();
            }
        }
    }

    private void continueUploadImageIfAvailable() {
        if (marketPlaceSellItemViewModel.getUploadTask() != null) {
            marketPlaceSellItemViewModel.getUploadTask().addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    getDownloadUrl(taskSnapshot);
                    marketPlaceSellItemViewModel.setUploadTask(null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MarketPlaceSellItemActivity.this, "Error while uploading image in database", Toast.LENGTH_SHORT).show();
                    Logger.d("FAiled due to " + e.getMessage());
                    marketPlaceSellItemViewModel.setUploadTask(null);
                    btnPostAd.setEnabled(false);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    showSnackbarProgress(progress);
                }
            });
        }
    }

    private void uploadImage() {
        StorageReference child = mStorageReference.child("images/" + userName + "/items/" + Name + System.currentTimeMillis() + imageUri.getLastPathSegment());
        uploadTask = child.putFile(imageUri);
        marketPlaceSellItemViewModel.setUploadTask(uploadTask);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getDownloadUrl(taskSnapshot);
                marketPlaceSellItemViewModel.setUploadTask(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MarketPlaceSellItemActivity.this, "Error while uploading image in database", Toast.LENGTH_SHORT).show();
                Logger.d("FAiled due to " + e.getMessage());
                marketPlaceSellItemViewModel.setUploadTask(null);
                btnPostAd.setEnabled(false);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                showSnackbarProgress(progress);
            }
        });
    }

    private void getDownloadUrl(UploadTask.TaskSnapshot taskSnapshot) {
        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult().toString();
                    marketPlaceSellItemViewModel.setDownloadUri(downloadUri);
                } else {
                    Toast.makeText(MarketPlaceSellItemActivity.this, "Error While getting download link", Toast.LENGTH_SHORT).show();
                }
                btnPostAd.setEnabled(true);
            }
        });
    }

    private void showSnackbarProgress(long progress) {
        if (sbar != null) {

            if (sbar.isShown()) {
                sbar.setText("Photo is uploading now (" + progress + " % Done)");
                if (progress == 100) {
                    sbar.dismiss();
                }
                return;
            }
        }
        sbar = Snackbar.make(toolbar, "Photo is uploading now (" + progress + " % Done)", Snackbar.LENGTH_INDEFINITE);
        sbar.show();
    }

    private boolean validateName() {
        if (Name == null) {
            return false;
        }
        return !Name.isEmpty();
    }

    private boolean validateInfo() {
        if (Info == null) {
            return false;
        }
        return !Info.isEmpty();
    }

    private boolean validatePrice() {
        if (Price == null) {
            return false;
        }
        return !Price.isEmpty();
    }

    private boolean validatePhoneNo() {
        if (phoneNo == null) {
            return false;
        }
        if (phoneNo.isEmpty()) {
            return false;
            //TODO Random phone no limits
        }
        return phoneNo.length() >= 6 && phoneNo.length() <= 14;
    }

    private boolean validateImageUri() {
        return imageUri != null;
    }


}
