package com.vyas.pranav.studentcompanion.ui.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughActivity;
import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughCard;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.viewmodels.TutorialViewModel;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends FancyWalkthroughActivity {

    private TutorialViewModel tutorialViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tutorialViewModel = ViewModelProviders.of(this).get(TutorialViewModel.class);

        FancyWalkthroughCard welcomeCard = new FancyWalkthroughCard("Welcome!", "Hello user, Here are sime features of ths app!", R.drawable.ic_logo);
        FancyWalkthroughCard attendance = new FancyWalkthroughCard("Attendance Manager", "Simple and easy to use Attendance Manager to track attendance", R.drawable.ic_today_attendance);
        FancyWalkthroughCard smartSilent = new FancyWalkthroughCard("Smart Silent", "Smart Silent is feature that will make your phone silent when you are in the lecture", R.drawable.ic_silent);
        FancyWalkthroughCard autoAttendance = new FancyWalkthroughCard("Auto Attendance", "Works on the GPS location, WiFi and cellular signals to mark your present at you selected location", R.drawable.ic_auto_attendance_map);
        FancyWalkthroughCard resources = new FancyWalkthroughCard("Resources", "See Resources Menu from the navigation menu for surprises", R.drawable.ic_resources_magic_wand);
        FancyWalkthroughCard digitalLibrary = new FancyWalkthroughCard("Digital Library", "Look for book from digital library section for Ebooks, Photos etc", R.drawable.ic_bookshelf);
        FancyWalkthroughCard buySellItem = new FancyWalkthroughCard("Buy/Sell Items", "Buy or sell Items like Books, Bicycles etc and have users notify when new Items available in store", R.drawable.ic_market_place);
        FancyWalkthroughCard overallAttendance = new FancyWalkthroughCard("Overall Attendance", "Know where you are in required attendance criteria and know available lectures for bunk, Bunked lecutres etc..", R.drawable.ic_overall_attendance);
        FancyWalkthroughCard finalCard = new FancyWalkthroughCard("There's More", "That's not all!! Discover new features by exploring this app!", R.drawable.ic_gift);

//        welcomeCard.setBackgroundColor(R.color.white);
//        welcomeCard.setIconLayoutParams(300,300,0,0,0,0);
//        smartSilent.setBackgroundColor(R.color.white);
//        smartSilent.setIconLayoutParams(300,300,0,0,0,0);
//        autoAttendance.setBackgroundColor(R.color.white);
//        autoAttendance.setIconLayoutParams(300,300,0,0,0,0);
//        resources.setBackgroundColor(R.color.white);
//        resources.setIconLayoutParams(300,300,0,0,0,0);

        List<FancyWalkthroughCard> list = new ArrayList<>();
        list.add(welcomeCard);
        list.add(attendance);
        list.add(smartSilent);
        list.add(autoAttendance);
        list.add(resources);
        list.add(digitalLibrary);
        list.add(buySellItem);
        list.add(overallAttendance);
        list.add(finalCard);


        for (FancyWalkthroughCard page : list) {
            page.setTitleColor(R.color.black);
            page.setDescriptionColor(R.color.black);
        }
        setColorBackground(R.color.colorBackGroundTutorial);
        //Show/Hide navigation controls
        showNavigationControls(true);
        //Set pager indicator colors
        setInactiveIndicatorColor(R.color.grey_200);
        setActiveIndicatorColor(R.color.white);
        //Set finish button text
        setFinishButtonTitle("Get Started");
        //Set the finish button style
        setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.shape_rounded_button_margin_big));
        setOnboardPages(list);
    }

    @Override
    public void onFinishButtonPressed() {
        startMainActivity();
    }

    private void startMainActivity() {
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        Intent startMain = new Intent(this, MainActivity.class);
        startActivity(startMain);
        tutorialViewModel.setTutorialComplete(true);
        finish();
    }
}

//public class TutorialActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
//
//    @BindView(R.id.toolbar_tutorial)
//    Toolbar toolbar;
//    @BindView(R.id.viewpager_tutorial)
//    ViewPager viewPager;
//
//    @BindView(R.id.btn_tutorial_next)
//    Button btnNext;
//    @BindView(R.id.btn_tutorial_previous)
//    Button btnPrevious;
//
//    private int currItem = 0;
//    private int MAX_ITEMS = 5;
//    private TutorialViewModel tutorialViewModel;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        SharedPreferencesRepository.setUserTheme(this);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_toturial);
//        ButterKnife.bind(this);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        tutorialViewModel = ViewModelProviders.of(this).get(TutorialViewModel.class);
//        PagerAdapter adapter = new TutorialViewPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(adapter);
//        currItem = viewPager.getCurrentItem();
//        viewPager.addOnPageChangeListener(this);
//    }
//
//    @OnClick(R.id.btn_tutorial_next)
//    void nextClicked(Button btn) {
//        if (currItem < MAX_ITEMS - 1) {
//            currItem++;
//            viewPager.setCurrentItem(currItem);
//            return;
//        }
//        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
//        startMainActivity();
//    }
//
//    @OnClick(R.id.btn_tutorial_previous)
//    void previousClicked() {
//        if (currItem > 0) {
//            currItem--;
//            viewPager.setCurrentItem(currItem);
//            return;
//        }
//        Toast.makeText(this, "First Page", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        currItem = position;
//        if (currItem == MAX_ITEMS - 1) {
//            btnNext.setText("Finish");
//        } else {
//            btnNext.setText("Next");
//        }
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }
//
//    private void startMainActivity() {
//        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
//        Intent startMain = new Intent(this, MainActivity.class);
//        startActivity(startMain, bundle);
//        tutorialViewModel.setTutorialComplete(true);
//        finish();
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (currItem < 1) {
//            super.onBackPressed();
//        } else {
//            currItem--;
//            viewPager.setCurrentItem(currItem);
//        }
//    }
//
//    class TutorialViewPagerAdapter extends FragmentStatePagerAdapter {
//
//        public TutorialViewPagerAdapter(@NonNull FragmentManager fm) {
//            super(fm);
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            switch (position) {
//                case 0:
//                    return new TutorialStepFragment();
//                case 1:
//                    return new TutorialStep2Fragment();
//                case 2:
//                    return new TutorialStep3Fragment();
//                case 3:
//                    return new TutorialStep4Fragment();
//                case 4:
//                    return new TutorialStep5Fragment();
//            }
//            return null;
//        }
//
//        @Override
//        public int getCount() {
//            return MAX_ITEMS;
//        }
//
//        @Nullable
//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "Step 1";
//                case 1:
//                    return "Step 2";
//                case 2:
//                    return "Step 3";
//                case 3:
//                    return "Step 4";
//                case 4:
//                    return "Step 5";
//            }
//            return super.getPageTitle(position);
//        }
//    }
//}
