package com.vyas.pranav.studentcompanion.ui.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.repositories.SharedPreferencesRepository;
import com.vyas.pranav.studentcompanion.ui.fragments.HolidayFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.MarketPlaceFragment;
import com.vyas.pranav.studentcompanion.ui.fragments.TimetableFragment;
import com.vyas.pranav.studentcompanion.viewmodels.ContainerViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ContainerActivity extends AppCompatActivity {

    public static final int DIGITAL_LIBRARY = 1;
    public static final int TIME_TABLE = 2;
    public static final int HOLIDAYS = 3;
    public static final int MARKETPLACE_ACTIVITY = 4;
    public static final String KEY_EXTRA_RESIURCE_TO_CONTAINER_ACTIVITY = "KEY_CONTAINER_RECEIVED_EXTRA";
    @BindView(R.id.toolbar_container)
    Toolbar toolbar;
    @BindView(R.id.tv_toolbar_container)
    TextView tvToolbar;

    private ContainerViewModel containerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesRepository.setUserTheme(this);
        setContentView(R.layout.activity_container);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        containerViewModel = ViewModelProviders.of(this).get(ContainerViewModel.class);
        initLogic();
    }

    private void initLogic() {
        int receivedData = getIntent().getIntExtra(KEY_EXTRA_RESIURCE_TO_CONTAINER_ACTIVITY, 0);
        if (containerViewModel.getCurrentFragmentId() != 0) {
            containerViewModel.setCurrentFragmentId(receivedData);
        }
        containerViewModel.setCurrentFragmentId(containerViewModel.getCurrentFragmentId());
        setFragmentTitle(receivedData);
        switch (receivedData) {
            case TIME_TABLE:
                TimetableFragment timetableFragment = new TimetableFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container_activity_container, timetableFragment, "TIME_TABLE")
                        .commit();
                break;

            case HOLIDAYS:
                HolidayFragment holidayFragment = new HolidayFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container_activity_container, holidayFragment, "HOLIDAY")
                        .commit();
                break;

            case MARKETPLACE_ACTIVITY:
                MarketPlaceFragment marketPlaceFragment = new MarketPlaceFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container_activity_container, marketPlaceFragment, "MARKET_PLACE")
                        .commit();
                break;
        }
    }

    private void setFragmentTitle(int receivedData) {
        switch (receivedData) {
            case TIME_TABLE:
                tvToolbar.setText("Time Table");
                break;

            case HOLIDAYS:
                tvToolbar.setText("Holidays");
                break;

            case MARKETPLACE_ACTIVITY:
                tvToolbar.setText("Market Place");
                break;
        }
    }

}
