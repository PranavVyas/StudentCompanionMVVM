package com.vyas.pranav.studentcompanion.ui.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.OpenSourceRecyclerAdapter;
import com.vyas.pranav.studentcompanion.data.models.ExternalLibraryModel;
import com.vyas.pranav.studentcompanion.data.models.LibraryModel;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpenSourceInformationActivity extends AppCompatActivity {

    @BindView(R.id.rv_open_source_information)
    RecyclerView rvMain;
    @BindView(R.id.tv_open_source_information_license_extra)
    TextView tvLicenceExtra;
    @BindView(R.id.tv_open_source_information_license)
    TextView tvLicense;
    @BindView(R.id.tv_open_source_information_app_name)
    TextView tvName;
    @BindView(R.id.toolbar_open_source_information)
    Toolbar toolbar;

    private String jsonString;
    private List<ExternalLibraryModel> external_libraries;
    private OpenSourceRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesUtils.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source_information);
        ButterKnife.bind(this);
        populateUI();
    }

    private void populateUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        jsonString = loadJsonFromAsset();
        if (jsonString != null) {
            Gson gson = new Gson();
            LibraryModel libraryModel = gson.fromJson(jsonString, LibraryModel.class);
            external_libraries = libraryModel.getExternal_libraries();
            tvLicenceExtra.setText("License Info : " + libraryModel.getExtra_license());
            tvLicense.setText("License Type : " + libraryModel.getLicense());
            tvName.setText(libraryModel.getName());
            libraryModel.getExternal_libraries();
            setUpRecyclerView();
//            setUpExpansionLayout();
        } else {
            Toast.makeText(this, "Something Wrong occurred", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpRecyclerView() {
        rvMain.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mAdapter = new OpenSourceRecyclerAdapter();
        mAdapter.setExternalLibraryModels(external_libraries);
        rvMain.setAdapter(mAdapter);
    }

    private String loadJsonFromAsset() {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("libraries_info.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            int read = inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

}
