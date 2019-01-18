package com.vyas.pranav.studentcompanion.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.ui.fragments.TutorialStep2Fragment;
import com.vyas.pranav.studentcompanion.ui.fragments.TutorialStep3Fragment;
import com.vyas.pranav.studentcompanion.ui.fragments.TutorialStep4Fragment;
import com.vyas.pranav.studentcompanion.ui.fragments.TutorialStep5Fragment;
import com.vyas.pranav.studentcompanion.ui.fragments.TutorialStepFragment;
import com.vyas.pranav.studentcompanion.viewmodels.TutorialViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TutorialActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.toolbar_tutorial)
    Toolbar toolbar;
    @BindView(R.id.viewpager_tutorial)
    ViewPager viewPager;

    @BindView(R.id.btn_tutorial_next)
    Button btnNext;
    @BindView(R.id.btn_tutorial_previous)
    Button btnPrevious;

    private int currItem = 0;
    private int MAX_ITEMS = 5;
    private TutorialViewModel tutorialViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toturial);
        ButterKnife.bind(this);
        tutorialViewModel = ViewModelProviders.of(this).get(TutorialViewModel.class);
        PagerAdapter adapter = new TutorialViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        currItem = viewPager.getCurrentItem();
        viewPager.addOnPageChangeListener(this);
    }

    @OnClick(R.id.btn_tutorial_next)
    void nextClicked(Button btn) {
        if (currItem < MAX_ITEMS - 1) {
            currItem++;
            viewPager.setCurrentItem(currItem);
            return;
        }
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        startMainActivity();
    }

    @OnClick(R.id.btn_tutorial_previous)
    void previousClicked() {
        if (currItem > 0) {
            currItem--;
            viewPager.setCurrentItem(currItem);
            return;
        }
        Toast.makeText(this, "First Page", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currItem = position;
        if (currItem == MAX_ITEMS - 1) {
            btnNext.setText("Finish");
        } else {
            btnNext.setText("Next");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void startMainActivity() {
        Intent startMain = new Intent(this, MainActivity.class);
        startActivity(startMain);
        tutorialViewModel.setTutorialComplete(true);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (currItem < 1) {
            super.onBackPressed();
        } else {
            currItem--;
            viewPager.setCurrentItem(currItem);
        }
    }

    class TutorialViewPagerAdapter extends FragmentStatePagerAdapter {

        public TutorialViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TutorialStepFragment();
                case 1:
                    return new TutorialStep2Fragment();
                case 2:
                    return new TutorialStep3Fragment();
                case 3:
                    return new TutorialStep4Fragment();
                case 4:
                    return new TutorialStep5Fragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return MAX_ITEMS;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Step 1";
                case 1:
                    return "Step 2";
                case 2:
                    return "Step 3";
                case 3:
                    return "Step 4";
                case 4:
                    return "Step 5";
            }
            return super.getPageTitle(position);
        }
    }
}
