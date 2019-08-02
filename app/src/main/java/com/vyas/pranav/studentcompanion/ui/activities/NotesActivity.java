package com.vyas.pranav.studentcompanion.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.fragments.NotesListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils.setUserTheme;

public class NotesActivity extends AppCompatActivity {

    @BindView(R.id.viewpager_notes)
    ViewPager viewPagerNotes;
    @BindView(R.id.toolbar_notes)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserTheme(this);
        setContentView(R.layout.activity_notes);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewPagerNotes.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        NotesViewPagerAdapterNew mAdapter = new NotesViewPagerAdapterNew(getSupportFragmentManager());
        viewPagerNotes.setAdapter(mAdapter);
    }

    @OnClick(R.id.fab_notes_add_new)
    void fabClicked() {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }
}

class NotesViewPagerAdapterNew extends FragmentStatePagerAdapter {

    NotesViewPagerAdapterNew(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Till Today";

            case 1:
                return "All";

            default:
                return "Default";
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NotesListFragment todayNotes = new NotesListFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(NotesListFragment.EXTRA_NOTES_DISPLAY_TYPE, NotesListFragment.TYPE_TILL_TODAY_NOTES_SHOW);
                todayNotes.setArguments(bundle);
                return todayNotes;

            case 1:
                NotesListFragment allNotes = new NotesListFragment();
                Bundle bundleAll = new Bundle();
                bundleAll.putInt(NotesListFragment.EXTRA_NOTES_DISPLAY_TYPE, NotesListFragment.TYPE_ALL_NOTES_SHOW);
                allNotes.setArguments(bundleAll);
                return allNotes;

            default:
                return null;
        }
    }
}



