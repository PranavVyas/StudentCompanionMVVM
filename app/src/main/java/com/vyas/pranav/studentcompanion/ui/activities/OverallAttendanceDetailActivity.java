package com.vyas.pranav.studentcompanion.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.itangqi.waveloadingview.WaveLoadingView;

public class OverallAttendanceDetailActivity extends AppCompatActivity {

    public static final String EXTRA_OVERALL_ATTENDANCE = "OverallAttendanceDetailActivity.EXTRA_OVERALL_ATTENDANCE";
    @BindView(R.id.progress_overall_attendance_detail)
    WaveLoadingView progressSubject;
    @BindView(R.id.tv_overall_attendance_detail_bunked)
    TextView tvBunkedDays;
    @BindView(R.id.tv_overall_attendance_detail_credits)
    TextView tvCredits;
    @BindView(R.id.tv_overall_attendance_detail_date_today)
    TextView tvDateToday;
    @BindView(R.id.tv_overall_attendance_detail_elapsed)
    TextView tvElapsedDays;
    @BindView(R.id.tv_overall_attendance_detail_end_date)
    TextView tvEndDate;
    @BindView(R.id.tv_overall_attendance_detail_left_bunk)
    TextView tvLeftToBunkDays;
    @BindView(R.id.tv_overall_attendance_detail_present)
    TextView tvPresentDays;
    @BindView(R.id.tv_overall_attendance_detail_start_date)
    TextView tvStartDate;
    @BindView(R.id.tv_overall_attendance_detail_subject)
    TextView tvSubject;
    @BindView(R.id.tv_overall_attendance_detail_total)
    TextView tvTotalDays;
    @BindView(R.id.toolbar_overall_attendance_detail)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_attendacne_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent receivedData = getIntent();
        if (receivedData != null) {
            if (receivedData.hasExtra(EXTRA_OVERALL_ATTENDANCE)) {
                String receivedJson = receivedData.getStringExtra(EXTRA_OVERALL_ATTENDANCE);
                Gson gson = new Gson();
                OverallAttendanceEntry entry = gson.fromJson(receivedJson, OverallAttendanceEntry.class);
                populateUI(entry);
            }
        }
    }

    private void populateUI(OverallAttendanceEntry entry) {
        tvSubject.setText(entry.getSubName());
        tvCredits.setText(String.valueOf(entry.getCredits()));
        tvDateToday.setText(ConverterUtils.convertDateToString(new Date()));
        tvStartDate.setText(Constants.SEM_START_DATE_STR);
        tvEndDate.setText(Constants.SEM_END_DATE_STR);
        tvTotalDays.setText(String.valueOf(entry.getTotalDays()));
        tvPresentDays.setText(String.valueOf(entry.getPresentDays()));
        tvBunkedDays.setText(String.valueOf(entry.getBunkedDays()));
        tvElapsedDays.setText(String.valueOf((entry.getBunkedDays() + entry.getPresentDays())));
        int leftToBunk = (int) Math.ceil(entry.getTotalDays() * 0.25) - entry.getBunkedDays();
        tvLeftToBunkDays.setText(String.valueOf(leftToBunk));
        int precentPercent = (entry.getPresentDays() * 100) / entry.getTotalDays();
        progressSubject.setProgressValue(precentPercent);
        progressSubject.setCenterTitle(precentPercent + " %");
    }
}
