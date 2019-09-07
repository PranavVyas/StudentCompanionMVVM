package com.vyas.pranav.studentcompanion.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.TimetableTableAdapter;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.viewmodels.TimetableViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetUpTimetableNew extends Fragment implements ITableViewListener {


    @BindView(R.id.table_setup_timetable)
    TableView timetable;
    List<String> weekDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
    private TimetableTableAdapter mAdapter;
    private TimetableViewModel timetableViewModel;
    private int lecturesPerDay;
    private List<String> columnHeadings;
    private String oldSub, newSub;
    private List<String> subList;
    private OnTimetableSelectedListenerNew listener;

    public SetUpTimetableNew() {
        // Required empty public constructor
    }

    public static SetUpTimetableNew newInstance() {
        return new SetUpTimetableNew();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_up_timetable_new, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timetableViewModel = ViewModelProviders.of(this).get(TimetableViewModel.class);

        lecturesPerDay = timetableViewModel.getLecturesPerDay();
        subList = timetableViewModel.getSubjectList();
        subList.add(Constants.DEFAULT_LECTURE);
        bindUi();
        timetable.setTableViewListener(this);
        showHelp();
    }

    private void bindUi() {
        mAdapter = new TimetableTableAdapter(getContext());
        columnHeadings = getColumnHeaders(lecturesPerDay);
        timetable.setAdapter(mAdapter);
        mAdapter.setAllItems(columnHeadings, weekDays, timetableViewModel.getDaysLectures());
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

    private void showHelp() {
        BottomSheetDialog mDialog = new BottomSheetDialog(getContext());
        mDialog.setContentView(R.layout.item_holder_bottom_sheet_set_up_lecture);
        mDialog.show();
    }

    //Ex no of lectures = 7, days = 5
    //Weekdays = maximum 5
    //column Index = maximum as no of lecture = 7
    //
    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder viewHolder, int columnIndex, int rawIndex) {
        oldSub = timetableViewModel.getDaysLectures().get(rawIndex).get(columnIndex);
        int lectureNo = (columnIndex + 1);
        String day = weekDays.get(rawIndex);
        showSnackBar("Day : " + day + " Lecture No :" + lectureNo + " Subject Old : " + oldSub);
        BottomSheetDialog promptDialog = new BottomSheetDialog(getContext());
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
        tvLecture.setText("Lecture No : " + lectureNo);
        tvDay.setText("on : " + day);
        tvSubject.setText("Old Subject : " + oldSub);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_simple_custom_main, subList);
        mAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSubjects.setAdapter(mAdapter);
        spinnerSubjects.setSelection(0);
        newSub = subList.get(0);
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
            changeTemporary(columnIndex, rawIndex, newSub);
            promptDialog.dismiss();
        });

    }

    private void changeTemporary(int column, int raw, String newSub) {
        timetableViewModel.setDaysLectures(raw, column, newSub);
        mAdapter.setCellItems(timetableViewModel.getDaysLectures());
        mAdapter.notifyDataSetChanged();
    }

    private void showSnackBar(String s) {
        Logger.d(s);
        Snackbar.make(timetable, s, Snackbar.LENGTH_SHORT).show();
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

    @OnClick(R.id.btn_setup_timetable_previous)
    void previousClicked() {
        if (listener != null) {
            listener.onPreviousClickedInSetUpTimetableNew();
        }
    }

    @OnClick(R.id.btn_setup_timetable_next)
    void nextClicked() {
        if (listener != null) {
            listener.onTimetableSelectedNew(timetableViewModel.getDaysLectures(), weekDays, columnHeadings);
        }
    }

    public void setOnTimeTableSelectedListenerNew(SetUpTimetableNew.OnTimetableSelectedListenerNew listener) {
        this.listener = listener;
    }

    public interface OnTimetableSelectedListenerNew {
        void onTimetableSelectedNew(List<List<String>> subjects, List<String> days, List<String> columnTitles);

        void onPreviousClickedInSetUpTimetableNew();
    }

}
