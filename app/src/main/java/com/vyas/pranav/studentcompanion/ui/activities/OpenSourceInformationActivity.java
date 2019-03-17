package com.vyas.pranav.studentcompanion.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.LibraryChildModel;
import com.vyas.pranav.studentcompanion.data.LibraryParentModel;
import com.vyas.pranav.studentcompanion.data.models.ExternalLibraryModel;
import com.vyas.pranav.studentcompanion.data.models.LibraryModel;
import com.vyas.pranav.studentcompanion.repositories.SharedPreferencesRepository;
import com.vyas.pranav.studentcompanion.utils.GlideApp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import iammert.com.expandablelib.ExpandCollapseListener;
import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;

public class OpenSourceInformationActivity extends AppCompatActivity {

    //    @BindView(R.id.rv_open_source_information)
//    RecyclerView rvMain;
    @BindView(R.id.expansion_layout_open_source_license)
    ExpandableLayout expandableLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesRepository.setUserTheme(this);
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
            setUpExpansionLayout();
        } else {
            Toast.makeText(this, "Something Wrong occurred", Toast.LENGTH_SHORT).show();
        }
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

    private void setUpExpansionLayout() {
        expandableLayout.setRenderer(new ExpandableLayout.Renderer<LibraryParentModel, LibraryChildModel>() {
            @Override
            public void renderParent(View view, LibraryParentModel libraryParentModel, boolean b, int i) {
                ((TextView) view.findViewById(R.id.tv_expansion_osl_parent_name)).setText(libraryParentModel.getName());
            }

            @Override
            public void renderChild(View view, LibraryChildModel libraryChildModel, int i, int i1) {
                ((TextView) view.findViewById(R.id.tv_expansion_osl_child_license)).setText(libraryChildModel.getLicense());
                ((TextView) view.findViewById(R.id.tv_expansion_osl_child_url)).setText(libraryChildModel.getUrl());
            }
        });
        expandableLayout.setCollapseListener(new ExpandCollapseListener.CollapseListener<LibraryParentModel>() {
            @Override
            public void onCollapsed(int i, LibraryParentModel libraryParentModel, View view) {
                GlideApp.with(view).load(R.drawable.ic_arrow_down)
                        .into((ImageView) view.findViewById(R.id.image_expansion_osl_parent_arrow));
            }
        });
        expandableLayout.setExpandListener(new ExpandCollapseListener.ExpandListener<LibraryParentModel>() {
            @Override
            public void onExpanded(int i, LibraryParentModel libraryParentModel, View view) {
                GlideApp.with(view).load(R.drawable.ic_arrow_up)
                        .into((ImageView) view.findViewById(R.id.image_expansion_osl_parent_arrow));
            }
        });
        for (int position = 0; position < external_libraries.size(); position++) {
            Section<LibraryParentModel, LibraryChildModel> section = new Section<>();
            section.parent = new LibraryParentModel(external_libraries.get(position).getName());
            section.children.add(new LibraryChildModel(external_libraries.get(position).getLicense(), external_libraries.get(position).getUrl()));
            expandableLayout.addSection(section);
        }
    }
}
