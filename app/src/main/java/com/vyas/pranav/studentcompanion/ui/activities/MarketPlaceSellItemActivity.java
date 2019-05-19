package com.vyas.pranav.studentcompanion.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.vyas.pranav.studentcompanion.data.itemdatabase.firebase.ItemModel;
import com.vyas.pranav.studentcompanion.repositories.SharedPreferencesRepository;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.GlideApp;
import com.vyas.pranav.studentcompanion.viewmodels.MarketPlaceSellItemViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarketPlaceSellItemActivity extends AppCompatActivity {

    public static final String EXTRA_DOWNLOAD_URL = "MarketPlaceSellingActivity.DownloadUri";
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
    @BindView(R.id.btn_market_place_sell_item_post_ad)
    Button btnPostAd;
    @BindView(R.id.placeholder_marketplace_sell_item_no_connection)
    ConstraintLayout placeHOlderConnection;
    @BindView(R.id.image_placeholder_market_place_sell_item)
    ImageView imagePlaceHolder;
    @BindView(R.id.tv_marketplace_sell_item_image_state)
    TextView tvImageSelectionState;
    @BindView(R.id.scroll_market_place_sell_item_container)
    ScrollView scrollContainer;

    private String phoneNo, name, info, price, userName, selectedCategory;
    private Uri imageUri;
    private String downloadUri;

    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private StorageReference mStorageReference = mStorage.getReference();
    private StorageReference child;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mCollectionReference = mFirestore.collection("sell");
    private Snackbar sbar;
    private MarketPlaceSellItemViewModel marketPlaceSellItemViewModel;
    private UploadTask uploadTask;
    private String childRefString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesRepository.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_sell_item);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        marketPlaceSellItemViewModel = ViewModelProviders.of(this).get(MarketPlaceSellItemViewModel.class);
        userName = marketPlaceSellItemViewModel.getCurrUser().getDisplayName();
        populateUI();
    }

    private void populateUI() {
        setUpSpinner();
        refreshStatus();
        imageUri = marketPlaceSellItemViewModel.getImageUri();
        downloadUri = marketPlaceSellItemViewModel.getDownloadUri();
        GlideApp.with(this)
                .load(imageUri)
                .placeholder(R.drawable.ic_picture_sample)
                .error(R.drawable.ic_picture_sample)
                .circleCrop()
                .into(imageItem);
        continueUploadImageIfAvailable();
        GlideApp.with(this)
                .load(R.drawable.image_no_connection_placeholder)
                .into(imagePlaceHolder);
    }

    private void setUpSpinner() {
        List<String> categories = new ArrayList<>(Arrays.asList(
                "Book",
                "Bicycle",
                "Xerox"
        ));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_simple_custom_main, categories);
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

    @Override
    public void onBackPressed() {
        sendRemoveImageIntentIfAny();
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

    @Override
    public boolean onSupportNavigateUp() {
        sendRemoveImageIntentIfAny();
        return super.onSupportNavigateUp();
    }

    @OnClick(R.id.btn_market_place_sell_item_post_ad)
    void postAdClicked() {
        //Here & is used which is known as EAGER OPERATOR in Java which makes sures that there is not any short circuit in the evaluation if && is used than if first is false others are not checked !!
        if (validateInfo() & validatePhoneNo() & validateName() & validatePrice() & validateImageUri() & validateDownloadUri()) {
            ItemModel item = new ItemModel();
            item.setContact(phoneNo);
            item.setExtra_info(info);
            item.setImage_uri(downloadUri);
            item.setPrice(Float.valueOf(price));
            item.setP_name(userName);
            item.setName(name);
            item.setCategory(selectedCategory);

            mCollectionReference.add(item).addOnSuccessListener(this, new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    showSnackbar("Added to database");
//                    Toast.makeText(MarketPlaceSellItemActivity.this, "Added to database", Toast.LENGTH_SHORT).show();
                    Logger.d("Successfully added to the database");
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(EXTRA_DOWNLOAD_URL, marketPlaceSellItemViewModel.getChildRefString());
                    setResult(RESULT_OK, resultIntent);
                    MarketPlaceSellItemActivity.this.finish();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Logger.d("Failed to add Due to : " + e.toString());
                    showSnackbar("Failed to add data");
//                    Toast.makeText(MarketPlaceSellItemActivity.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                }
            });
        }
//        else {
//            showSnackbar("Some error occured");
////            Toast.makeText(this, "Some error occured", Toast.LENGTH_SHORT).show();
//        }
    }

    private boolean validateDownloadUri() {
        Logger.d("Validation of Download Uri executed");
        if (downloadUri == null) {
            showSnackbar("Photo is not uploaded");
//            Toast.makeText(this, "Photo is not uploaded", Toast.LENGTH_SHORT).show();
            return false;
        }
        return !downloadUri.isEmpty();
    }

    @OnClick(R.id.image_marketplace_sell_item_photo)
    void selectImage() {
        if (!validateName()) {
            showSnackbar("Input name first");
//            Toast.makeText(this, "Input name first", Toast.LENGTH_SHORT).show();
            return;
        }
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
                if (downloadUri != null) {
                    removeLastUploadedImage();
                    //TODO Delete Previously Selected Image
                }
                imageUri = data.getData();
                marketPlaceSellItemViewModel.setImageUri(imageUri);
                GlideApp.with(this)
                        .load(imageUri).circleCrop()
                        .into(imageItem);
                btnPostAd.setEnabled(false);
                imageItem.setEnabled(false);
                AppExecutors.getInstance().networkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (ConverterUtils.hasInternetAccess(MarketPlaceSellItemActivity.this)) {
                            uploadImage();
                        } else {
                            showPlaceHOlder(true);
                        }
                    }
                });
            }
        }
    }

    private void continueUploadImageIfAvailable() {
        if (marketPlaceSellItemViewModel.getUploadTask() != null) {
            startUpload();
        }
    }

    private void uploadImage() {
        child = mStorageReference.child("images/" + userName + "/items/" + name + System.currentTimeMillis() + imageUri.getLastPathSegment());
        uploadTask = child.putFile(imageUri);
        marketPlaceSellItemViewModel.setUploadTask(uploadTask);
        marketPlaceSellItemViewModel.setChildRefString(child.toString());
        marketPlaceSellItemViewModel.setProgress(0);
        startUpload();
    }

    private void startUpload() {
        showSnackbar(marketPlaceSellItemViewModel.getProgress());
        marketPlaceSellItemViewModel.getUploadTask().addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getDownloadUrl(taskSnapshot);
                marketPlaceSellItemViewModel.setUploadTask(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showSnackbar("Error while uploading image in database");
//                Toast.makeText(MarketPlaceSellItemActivity.this, "Error while uploading image in database", Toast.LENGTH_SHORT).show();
                Logger.d("Failed due to " + e.getMessage());
                marketPlaceSellItemViewModel.setUploadTask(null);
                btnPostAd.setEnabled(false);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                marketPlaceSellItemViewModel.setCurrProgress(progress);
                if (progress == 100) {
                    marketPlaceSellItemViewModel.setCurrProgress(0);
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
                    downloadUri = task.getResult().toString();
                    marketPlaceSellItemViewModel.setDownloadUri(downloadUri);
                    marketPlaceSellItemViewModel.setProgress(100);
                    btnPostAd.setEnabled(true);
                    imageItem.setEnabled(true);
                } else {
                    showSnackbar("Error While getting download link");
                    imageItem.setEnabled(true);
                    marketPlaceSellItemViewModel.setProgress(-1);
//                    Toast.makeText(MarketPlaceSellItemActivity.this, "Error While getting download link", Toast.LENGTH_SHORT).show();
                }
                refreshStatus();
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
        name = etName.getText().toString().trim();
        Logger.d("Validation of name executed");
        if (name == null) {
            inputName.setError("Input Correct Name");
            return false;
        }
        if (name.isEmpty()) {
            inputName.setError("Input Correct Name");
            return false;
        } else {
            inputName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateInfo() {
        info = etInfo.getText().toString().trim();
        Logger.d("Validation of info executed");
        if (info == null) {
            inputInfo.setError("Input Valid Info");
            return false;
        }
        if (info.isEmpty()) {
            inputInfo.setError("Input Valid Info");
            return false;
        } else {
            inputInfo.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePrice() {
        price = etPrice.getText().toString().trim();
        Logger.d("Validation of Price executed");
        if (price == null) {
            inputPrice.setError("Input Correct Price");
            return false;
        }
        if (price.isEmpty()) {
            inputPrice.setError("Input Correct Price");
            return false;
        } else {
            inputPrice.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhoneNo() {
        phoneNo = etPhone.getText().toString().trim();
        Logger.d("Validation of phone no executed");
        if (phoneNo == null) {
            inputPhone.setError("Input Correct Phone No");
            return false;
        }
        if (phoneNo.isEmpty()) {
            inputPhone.setError("Input Correct Phone No");
            return false;
            //TODO Random phone no limits
        }
        if (!(phoneNo.length() >= 6) || !(phoneNo.length() <= 14)) {
            inputPhone.setError("Input Correct Phone No");
            return false;
        }
        inputPhone.setErrorEnabled(false);
        return true;
    }

    private boolean validateImageUri() {
        Logger.d("Validation of Uri executed");
        return imageUri != null;
    }

    private void showPlaceHOlder(boolean isShown) {
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                if (isShown) {
                    placeHOlderConnection.setVisibility(View.VISIBLE);
                    scrollContainer.setVisibility(View.GONE);
                } else {
                    placeHOlderConnection.setVisibility(View.GONE);
                    scrollContainer.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @OnClick(R.id.btn_placeholder_marketplace_sell_item_no_connection_retry)
    void retryClicked() {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                showPlaceHOlder(!ConverterUtils.hasInternetAccess(MarketPlaceSellItemActivity.this));
                Logger.d("Internet Connection is " + ConverterUtils.hasInternetAccess(MarketPlaceSellItemActivity.this));
            }
        });
    }

    private void sendRemoveImageIntentIfAny() {
        if (child != null) {
            removeLastUploadedImage();
//            Intent resultIntent = new Intent();
//            resultIntent.putExtra(EXTRA_DOWNLOAD_URL, child.getPath());
//            setResult(RESULT_CANCELED, resultIntent);
//            Logger.d("Child is not null");
        } else {
            Logger.d("Child is null");
        }
    }

    private void removeLastUploadedImage() {
        String childPath = child.getPath();
        Logger.d("Path of image is " + childPath);
        if (childPath != null) {
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(childPath);
            ref.delete().addOnSuccessListener(this, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(MarketPlaceSellItemActivity.this, "Removed from database", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MarketPlaceSellItemActivity.this, "Error from database", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void refreshStatus() {
        if (marketPlaceSellItemViewModel.getProgress() == -1) {
            tvImageSelectionState.setText("Please Select Image");
        } else if (marketPlaceSellItemViewModel.getProgress() == 100) {
            tvImageSelectionState.setText("Successfully Selected Image");
        } else {
            tvImageSelectionState.setText("Uploading Image Now...");
        }
    }

}
