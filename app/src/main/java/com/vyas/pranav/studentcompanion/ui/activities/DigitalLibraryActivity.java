package com.vyas.pranav.studentcompanion.ui.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.DigitalLibraryRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryEntry;
import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.firebase.BookModel;
import com.vyas.pranav.studentcompanion.utils.FirestoreQueryLiveData;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.viewmodels.DigitalLibraryViewModel;

import org.jetbrains.annotations.NotNull;

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
    private static final String TAG = "DigitalLibraryActivity";

    @BindView(R.id.text_input_digital_library_search_container)
    TextInputLayout inputSearch;
    @BindView(R.id.et_digital_library_search)
    TextInputEditText etSearch;

    private DigitalLibraryRecyclerAdapter mAdapter;
    private DigitalLibraryViewModel digitalLibraryViewModel;
    @BindView(R.id.placeholder_digital_library_no_item)
    ConstraintLayout placeHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesUtils.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_library);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        digitalLibraryViewModel = ViewModelProviders.of(this).get(DigitalLibraryViewModel.class);
        setUpUi();
        startInstruction(this);
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
                    if (digitalLibraryEntries.size() == 0) {
                        showPlaceHolder(true);
                    } else {
                        showPlaceHolder(false);
                    }
                    mAdapter.submitList(digitalLibraryEntries);
                }
            });
        } else {
            digitalLibraryViewModel.getBookByName(searchTerm).observe(this, new Observer<List<DigitalLibraryEntry>>() {
                @Override
                public void onChanged(List<DigitalLibraryEntry> digitalLibraryEntries) {
                    if (digitalLibraryEntries.size() == 0) {
                        showPlaceHolder(true);
                    } else {
                        showPlaceHolder(false);
                    }
                    mAdapter.submitList(digitalLibraryEntries);
                }
            });
        }
    }

    @OnClick(R.id.fab_digital_library_upload_book)
    void onFabClicked() {
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        Intent intent = new Intent(this, UploadBookActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_digital_library_sync:
                onSyncClicked();
                break;

            case R.id.menu_digital_library_auto_sync:
                showBottomSheetForAutoSync();
                break;

            default:
                Toast.makeText(this, "Invalid Selection", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showBottomSheetForAutoSync() {
        BottomSheetDialog mDialog = new BottomSheetDialog(this);
        mDialog.setContentView(R.layout.item_holder_alert_dialog_auto_sync);
        mDialog.show();
        TextView status = mDialog.findViewById(R.id.tv_holder_atert_dialog_auto_sync);
        status.setText("Current Status : " + (digitalLibraryViewModel.getStateOfAutoSync() ? "Enabled" : "Disabled"));
        mDialog.findViewById(R.id.btn_holder_atert_dialog_auto_sync_enable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                digitalLibraryViewModel.changeAutoSync(true);
                mDialog.dismiss();
            }
        });
        mDialog.findViewById(R.id.btn_holder_atert_dialog_auto_sync_disable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                digitalLibraryViewModel.changeAutoSync(false);
                mDialog.dismiss();
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

    private void startInstruction(Activity activity) {
        BubbleShowCaseBuilder intro = new BubbleShowCaseBuilder(activity)
                .title(getString(R.string.instr_digital_search_title))
                .description(getString(R.string.instr_digital_search_desc))
                .targetView(inputSearch)
//                .showOnce(TAG + "Search")
                ;
        BubbleShowCaseBuilder menu = new BubbleShowCaseBuilder(activity)
                .title(getString(R.string.instr_digital_menu_title))
                .description(getString(R.string.instr_digital_menu_desc))
//                .showOnce(TAG + "Menu")
                ;
        new BubbleShowCaseSequence()
                .addShowCase(intro)
                .addShowCase(menu)
                .show();
    }

    private void showPlaceHolder(boolean isShown) {
        if (isShown) {
            placeHolder.setVisibility(View.VISIBLE);
            rvList.setVisibility(View.GONE);
        } else {
            placeHolder.setVisibility(View.GONE);
            rvList.setVisibility(View.VISIBLE);
        }
    }
}
