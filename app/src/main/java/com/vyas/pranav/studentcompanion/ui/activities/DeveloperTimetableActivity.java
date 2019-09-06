package com.vyas.pranav.studentcompanion.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.TimetableTableAdapter;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.Generators;
import com.vyas.pranav.studentcompanion.viewmodels.TimetableViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils.setUserTheme;

public class DeveloperTimetableActivity extends AppCompatActivity implements ITableViewListener {

    @BindView(R.id.toolbar_developer_timetable)
    Toolbar toolbar;
    @BindView(R.id.table_developer_timetable)
    TableView tableTimetable;
    List<String> weekDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
    private TimetableTableAdapter mAdapter;
    private TimetableViewModel timetableViewModel;
    private int lecturesPerDay;
    private List<String> columnHeadings;
    private List<List<String>> daysLectures;
    private List<String> Monday, Tuesday, Wednesday, Thursday, Friday;
    private String oldSub, newSub;
    private List<String> subList;
    private MainDatabase mDb;
    private int semester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserTheme(this);
        setContentView(R.layout.activity_developer_timetable);
        ButterKnife.bind(this);
        timetableViewModel = ViewModelProviders.of(this).get(TimetableViewModel.class);
        lecturesPerDay = timetableViewModel.getLecturesPerDay();
        subList = timetableViewModel.getSubjectList();
        subList.add(Constants.DEFAULT_LECTURE);
        mDb = MainDatabase.getInstance(this);
        semester = timetableViewModel.getSemInfo();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bindUi();
        tableTimetable.setTableViewListener(this);
    }

    private void bindUi() {
        mAdapter = new TimetableTableAdapter(this);
        final LiveData<List<TimetableEntry>> timetableEntriesLiveData = timetableViewModel.getTimetableEntries();
        timetableEntriesLiveData.observe(this, timetableEntries -> {
//                timetableEntriesLiveData.removeObserver(DeveloperTimetableActivity.this);
            List<String> Monday = new ArrayList<>();
            List<String> Tuesday = new ArrayList<>();
            List<String> Wednesday = new ArrayList<>();
            List<String> Thursday = new ArrayList<>();
            List<String> Friday = new ArrayList<>();

            for (int i = 0; i < timetableEntries.size(); i++) {
                int day = i / lecturesPerDay;
                int lecture = i % lecturesPerDay;

                switch (day) {
                    case 0:
                        //Monday
                        Monday.add(lecture, timetableEntries.get(i).getSubName());
                        break;

                    case 1:
                        //Tuesday
                        Tuesday.add(lecture, timetableEntries.get(i).getSubName());
                        break;

                    case 2:
                        //Wednesday
                        Wednesday.add(lecture, timetableEntries.get(i).getSubName());
                        break;

                    case 3:
                        //Thursday
                        Thursday.add(lecture, timetableEntries.get(i).getSubName());
                        break;

                    case 4:
                        //Friday
                        Friday.add(lecture, timetableEntries.get(i).getSubName());
                        break;
                }

            }
            daysLectures = new ArrayList<>(Arrays.asList(
                    Monday, Tuesday, Wednesday, Thursday, Friday
            ));
            columnHeadings = getColumnHeaders(lecturesPerDay);
            tableTimetable.setAdapter(mAdapter);
            mAdapter.setAllItems(columnHeadings, weekDays, daysLectures);
        });
    }

    private List<String> getColumnHeaders(int lecturesPerDay) {
        List<String> columnHeader = new ArrayList<>();
        for (int i = 0; i < lecturesPerDay; i++) {
            String header = "Lecture" + (i + 1);
            int startingTime = timetableViewModel.getStartingTimeOfLecture(i);
            int endingTime = timetableViewModel.getEndingTimeOfLecture(i);
            String start = ConverterUtils.convertTimeIntInString(startingTime);
            String end = ConverterUtils.convertTimeIntInString(endingTime);
            header = header + "\n" + start + "\nTo\n" + end;
            columnHeader.add(header);
        }
        return columnHeader;
    }

    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder viewHolder, int rawPosition, int columnPosition) {
        showSnackBar("Day : " + weekDays.get(rawPosition) + " Lecture No :" + rawPosition + " Subject Old : " + daysLectures.get(rawPosition).get(columnPosition));
        oldSub = daysLectures.get(columnPosition).get(rawPosition);
        BottomSheetDialog promptDialog = new BottomSheetDialog(this);
        promptDialog.setContentView(R.layout.item_holder_bottom_sheet_timetable_edit);
        promptDialog.setOnDismissListener(dialogInterface -> {
            if (!newSub.equals(oldSub)) {
                showSnackBar("Subject : " + oldSub + " was changed to " + newSub);
            } else {
                showSnackBar("That entry was not changed!");
            }
        });
        promptDialog.show();

        TextView tvLecture = promptDialog.findViewById(R.id.tv_bottom_dev_timetable_lecture);
        TextView tvSubject = promptDialog.findViewById(R.id.tv_bottom_dev_timetable_subject);
        TextView tvDay = promptDialog.findViewById(R.id.tv_bottom_dev_timetable_day);
        Spinner spinnerSubjects = promptDialog.findViewById(R.id.spinner_bottom_dev_timetable_new_subject);
        tvLecture.setText("Lecture No : " + (rawPosition + 1));
        tvDay.setText("on " + weekDays.get(rawPosition));
        tvSubject.setText("Old Subject : " + oldSub);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(DeveloperTimetableActivity.this, R.layout.spinner_simple_custom_main, subList);
        mAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSubjects.setAdapter(mAdapter);
        spinnerSubjects.setSelection(0);
        newSub = oldSub;
        spinnerSubjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                newSub = subList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btnEdit = promptDialog.findViewById(R.id.btn_bottom_dev_timetable_done);
        btnEdit.setOnClickListener((view) -> {
            changeTemporary(columnPosition, rawPosition, newSub);
            promptDialog.dismiss();
        });

    }

    private void changeTemporary(int column, int raw, String newSub) {
        daysLectures.get(column).set(raw, newSub);
        mAdapter.setCellItems(daysLectures);
        mAdapter.notifyDataSetChanged();
    }

    private void showSnackBar(String s) {
        Logger.d(s);
        Snackbar.make(toolbar, s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCellLongPressed(@NonNull RecyclerView.ViewHolder viewHolder, int i, int i1) {

    }

    @Override
    public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    void refreshNewTimetable(List<List<String>> subjects, List<String> days, List<String> columnTitles) {
        List<TimetableEntry> timetableEntries = new ArrayList<>();
        for (int i = 0; i < days.size(); i++) {
            for (int j = 0; j < columnTitles.size(); j++) {
                String subject = subjects.get(i).get(j);
                int lectureNo = j + 1;
                String day = days.get(i);
                String columnTitle = columnTitles.get(j);
                int timeStart = ConverterUtils.convertTimeInInt(columnTitle.substring(columnTitle.lastIndexOf('e') + 3, columnTitle.lastIndexOf('T') - 1));
                int timeEnd = ConverterUtils.convertTimeInInt(columnTitle.substring(columnTitle.lastIndexOf('o') + 2));
                String id = Generators.generateIdForTimetableEntry(lectureNo, semester, i);
                TimetableEntry entry = new TimetableEntry(id, timeStart, timeEnd, day, subject, lectureNo);
                timetableEntries.add(entry);
                showSnackBar("For Subject: " + subject + " at day: " + day + " of lecture no: " + lectureNo + "time End is : " + timeEnd + "times tart is: " + timeStart);
            }
        }
        AppExecutors.getInstance().diskIO().execute(() -> {
            mDb.timetableDao().deleteWholeTimetable();
            mDb.timetableDao().insertAllTimeTableEntry(timetableEntries);
            continueWithNewTimetable();
        });
    }

    @OnClick(R.id.btn_developer_timetable_edit)
    void finalized() {
        refreshNewTimetable(daysLectures, weekDays, columnHeadings);
    }

    void continueWithNewTimetable() {
        mDb.attendanceDao().removeAttendanceAfter(new Date());
        //to remove creation of duplicate overall attendances
        mDb.overallAttendanceDao().deleteAllOverall();
        SetUpProcessRepository setUpProcessRepository = SetUpProcessRepository.getInstance(this);
        setUpProcessRepository.holidayInitialized(mDb.holidayDao().getHolidayDates(), ConverterUtils.convertStringToDate(ConverterUtils.convertDateToString(new Date(new Date().getTime() + TimeUnit.DAYS.toMillis(1)))), ConverterUtils.convertStringToDate(timetableViewModel.getEndDate()));
    }

}
