package com.vyas.pranav.studentcompanion.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.viewmodels.QuickAnalysisOverallViewModel;
import com.vyas.pranav.studentcompanion.viewmodels.QuickAnalysisOverallViewModelFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuickAnalysisOverallActivity extends AppCompatActivity {

    public static final String EXTRA_SUBEJECT_NAME_QUICK_ANALYSIS = "SUBJECT_NAME_IN_QUICK_ANALYSIS";
    @BindView(R.id.calendar_quick_analysis_main)
    CalendarView calendar;
    @BindView(R.id.tv_quick_analysis_subject_info)
    TextView tvSubject;
    @BindView(R.id.tv_quick_analysis_from_date)
    TextView tvFrom;
    @BindView(R.id.tv_quick_analysis_to_date)
    TextView tvTo;
    @BindView(R.id.tv_quick_analysis_sem)
    TextView tvSem;
    @BindView(R.id.toolbar_quick_analysis)
    Toolbar toolbar;

    private String subject;
    private QuickAnalysisOverallViewModel mViewModel;
    private List<EventDay> attendances;
    private List<EventDay> holidays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesUtils.setUserTheme(this);
        setContentView(R.layout.activity_quick_analysis_overall);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getIntent() != null) {
            subject = getIntent().getStringExtra(EXTRA_SUBEJECT_NAME_QUICK_ANALYSIS);
            QuickAnalysisOverallViewModelFactory factory = new QuickAnalysisOverallViewModelFactory(subject, this);
            mViewModel = ViewModelProviders.of(this, factory).get(QuickAnalysisOverallViewModel.class);
            LiveData<List<AttendanceEntry>> attendanceForSubject = mViewModel.getAttendanceForSubject();
            tvFrom.setText(mViewModel.getStartingDate());
            tvTo.setText(mViewModel.getEndingDate());
            tvSem.setText("" + mViewModel.getSemester());
            tvSubject.setText("Subject : " + subject);
            attendanceForSubject.observe(this, attendanceEntries -> {
                attendances = new ArrayList<>();
                for (AttendanceEntry x :
                        attendanceEntries) {
                    Calendar instance = Calendar.getInstance();
                    instance.setTime(x.getDate());
                    if (x.getPresent() == Constants.PRESENT) {
                        attendances.add(new EventDay(instance, R.drawable.ic_present_material_tick));
                    } else if (x.getPresent() == Constants.ABSENT) {
                        attendances.add(new EventDay(instance, R.drawable.ic_absent_material_cross));
                    } else {
                        attendances.add(new EventDay(instance, R.drawable.ic_cancel_material_neutral));
                    }
                }
                calendar.setEvents(attendances);
                Calendar start = Calendar.getInstance();
                start.setTime(ConverterUtils.convertStringToDate(mViewModel.getStartingDate()));
                Calendar end = Calendar.getInstance();
                end.setTime(ConverterUtils.convertStringToDate(mViewModel.getEndingDate()));
                calendar.setMinimumDate(start);
                calendar.setMaximumDate(end);
                calendar.setOnDayClickListener(eventDay -> {
                    Intent intent = new Intent(this, AttendanceIndividualActivity.class);
                    Date date = new Date();
                    date.setTime(eventDay.getCalendar().getTimeInMillis());
                    intent.putExtra(AttendanceIndividualActivity.EXTRA_DATE, ConverterUtils.convertDateToString(date));
                    startActivity(intent);
                });
            });
        }

    }
}
