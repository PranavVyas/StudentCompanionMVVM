package com.vyas.pranav.studentcompanion.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.vyas.pranav.studentcompanion.viewmodels.DigitalLibraryViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DigitalLibraryActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_digital_library);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //TODO set parent Activity for back action and also set as single top activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        digitalLibraryViewModel = ViewModelProviders.of(this).get(DigitalLibraryViewModel.class);
        setUpUi();
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
                    mAdapter.refreshList(digitalLibraryEntries);
                }
            });
        } else {
            digitalLibraryViewModel.getBookByName(searchTerm).observe(this, new Observer<List<DigitalLibraryEntry>>() {
                @Override
                public void onChanged(List<DigitalLibraryEntry> digitalLibraryEntries) {
                    mAdapter.refreshList(digitalLibraryEntries);
                }
            });
        }
    }


    @OnClick(R.id.fab_digital_library_upload_book)
    void onFabClicked() {
        Intent intent = new Intent(this, UploadBookActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_digital_library_sync:
                onSyncClicked();
                break;

            default:
                Toast.makeText(this, "Invalid Selection", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpRecyclerView() {
        mAdapter = new DigitalLibraryRecyclerAdapter();
        mAdapter.setHasStableIds(true);
        LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvList.setAdapter(mAdapter);
        rvList.setLayoutManager(llm);
    }

    //TODO add automatic switch to auto sync

    private void onSyncClicked() {
        Toast.makeText(this, "Sync Clicked", Toast.LENGTH_SHORT).show();
        CollectionReference booksRef = FirebaseFirestore.getInstance().collection("digitalLibrary");
        FirestoreQueryLiveData liveBooksData = new FirestoreQueryLiveData(booksRef);
        liveBooksData.observe(this, new Observer<QuerySnapshot>() {
            @Override
            public void onChanged(QuerySnapshot queryDocumentSnapshots) {
                liveBooksData.removeObserver(this);
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
}
