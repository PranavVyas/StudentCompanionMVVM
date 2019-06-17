package com.vyas.pranav.studentcompanion.ui.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.DigitalLibraryRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryEntry;
import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.firebase.BookModel;
import com.vyas.pranav.studentcompanion.repositories.SharedPreferencesRepository;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;
import com.vyas.pranav.studentcompanion.viewmodels.DigitalLibraryViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DigitalLibraryActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.recycler_digital_library_list)
    RecyclerView rvList;
    @BindView(R.id.toolbar_digital_library)
    Toolbar toolbar;

    @BindView(R.id.text_input_digital_library_search_container)
    TextInputLayout inputSearch;
    @BindView(R.id.et_digital_library_search)
    TextInputEditText etSearch;

    private DigitalLibraryRecyclerAdapter mAdapter;
    private DigitalLibraryViewModel digitalLibraryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesRepository.setUserTheme(this);
        setContentView(R.layout.activity_digital_library);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        digitalLibraryViewModel = ViewModelProviders.of(this).get(DigitalLibraryViewModel.class);
        setUpUi();

//        //TODO do something about recyclerview not transitioning
//        Slide slide = new Slide(Gravity.RIGHT);
//        slide.setInterpolator(
//                AnimationUtils.loadInterpolator(this,
//                        android.R.interpolator.linear_out_slow_in));
//        slide.addTarget(etSearch);
//        slide.addTarget(inputSearch);
//        slide.addTarget(R.id.btn_digital_library_search);
//        slide.addTarget(rvList);
//        slide.addTarget(R.id.textView59);
//        slide.addTarget(R.id.imageView12);
//        slide.addTarget(R.id.divider8);
//        slide.addTarget(R.id.fab_digital_library_upload_book);
////        slide.setDuration(TimeUnit.SECONDS.toMillis(20));
//        getWindow().setEnterTransition(android.R.transition.slide_right);
        searchClicked();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    private void setUpUi() {
        setUpRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.digital_library_menu, menu);
        return true;
    }

    @OnClick(R.id.btn_digital_library_search)
    void searchClicked() {
        String searchTerm = etSearch.getText().toString().trim();
        if (searchTerm.isEmpty()) {
            digitalLibraryViewModel.getAllBooks().observe(this, new Observer<List<DigitalLibraryEntry>>() {
                @Override
                public void onChanged(List<DigitalLibraryEntry> digitalLibraryEntries) {
                    mAdapter.submitList(digitalLibraryEntries);
                }
            });
        } else {
            digitalLibraryViewModel.getBookByName(searchTerm).observe(this, new Observer<List<DigitalLibraryEntry>>() {
                @Override
                public void onChanged(List<DigitalLibraryEntry> digitalLibraryEntries) {
                    mAdapter.submitList(digitalLibraryEntries);
                }
            });
        }
    }

    @OnClick(R.id.fab_digital_library_upload_book)
    void onFabClicked() {
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        Intent intent = new Intent(this, UploadBookActivity.class);
        startActivity(intent, bundle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_digital_library_sync:
                onSyncClicked();
                break;

            case R.id.menu_digital_library_auto_sync:
                showAlertDialogForAutoSync();
                break;

            default:
                Toast.makeText(this, "Invalid Selection", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialogForAutoSync() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_holder_alert_dialog_auto_sync, null, false);
        TextView status = view.findViewById(R.id.tv_holder_atert_dialog_auto_sync);
        status.setText("Current Status : " + (digitalLibraryViewModel.getStateOfAutoSync() ? "Enabled" : "Disabled"));
        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle("Auto Sync")
//                .setPositiveButton("Enable it", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        digitalLibraryViewModel.changeAutoSync(true);
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton("Disable it", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        digitalLibraryViewModel.changeAutoSync(false);
//                        dialog.dismiss();
//                    }
//                })
//                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
                .setView(view)
                .setCancelable(true)
                .create();
        dialog.show();
        dialog.findViewById(R.id.btn_holder_atert_dialog_auto_sync_enable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                digitalLibraryViewModel.changeAutoSync(true);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_holder_atert_dialog_auto_sync_disable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                digitalLibraryViewModel.changeAutoSync(false);
                dialog.dismiss();
            }
        });
    }

    private void setUpRecyclerView() {
        mAdapter = new DigitalLibraryRecyclerAdapter();
        LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvList.setLayoutManager(llm);
        rvList.setAdapter(mAdapter);
    }

    private void onSyncClicked() {
        Toast.makeText(this, "Sync Clicked", Toast.LENGTH_SHORT).show();
        CollectionReference booksRef = FirebaseFirestore.getInstance().collection("digitalLibrary");
        FirestoreQueryLiveData liveBooksData = new FirestoreQueryLiveData(booksRef);
        liveBooksData.removeObservers(this);
        liveBooksData.observe(this, new Observer<QuerySnapshot>() {
            @Override
            public void onChanged(QuerySnapshot queryDocumentSnapshots) {
                if (!digitalLibraryViewModel.getStateOfAutoSync()) {
                    liveBooksData.removeObserver(this);
                }
                List<BookModel> booksFirestore = queryDocumentSnapshots.toObjects(BookModel.class);
                List<DigitalLibraryEntry> booksDb = new ArrayList<>();
                for (int i = 0; i < booksFirestore.size(); i++) {
                    BookModel bookModel = booksFirestore.get(i);
                    DigitalLibraryEntry newEntry = new DigitalLibraryEntry(
                            bookModel.getBook_name(),
                            bookModel.getSubject(),
                            bookModel.getAuthor_name(),
                            bookModel.getDownload_url(),
                            bookModel.getExtra_info()
                    );
                    booksDb.add(newEntry);
                }
                digitalLibraryViewModel.replaceAllBooks(booksDb);
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(SharedPreferencesUtils.SHARED_PREF_AUTO_SYNC_DIGITAL_LIBRARY)) {
            onSyncClicked();
        }
    }
}
