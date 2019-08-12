package com.vyas.pranav.studentcompanion.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.vyas.pranav.studentcompanion.ui.fragments.NotesListFragment;

public class NotesViewPagerAdapterNew extends FragmentStatePagerAdapter {

    public NotesViewPagerAdapterNew(@NonNull FragmentManager fm) {
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
