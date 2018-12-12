package com.vyas.pranav.studentcompanion.ui.fragments;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.viewmodels.SetUpViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vyas.pranav.studentcompanion.utils.ConverterUtils.convertTimeInInt;

public class SetUpLectureTimeFragment extends Fragment {

    @BindView(R.id.linear_set_up_lecture_time_fragment_container)
    LinearLayout linearContainer;

    private SetUpViewModel setUpViewModel;
    private int noOfLecturesPerDay;
    private OnLectureTimeSelectedListener listener;

    private int index;

    public SetUpLectureTimeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_lecture_time, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViewModel = ViewModelProviders.of(getActivity()).get(SetUpViewModel.class);
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        populateUI();
    }

    private void populateUI() {
        noOfLecturesPerDay = setUpViewModel.getNoOfLecturesPerDay();
        index = 0;
        linearContainer.removeAllViews();
        for (int i = 0; i < noOfLecturesPerDay; i++) {
            final Button btnStartTime = new Button(getContext());
            btnStartTime.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            btnStartTime.setText("Select start time of lecture " + (i + 1));
            linearContainer.addView(btnStartTime, index);
            index++;
            final Button btnEndTime = new Button(getContext());
            btnEndTime.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            btnEndTime.setText("Select ending time of lecture " + (i + 1));
            linearContainer.addView(btnEndTime, index);
            index++;

            btnStartTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            Toast.makeText(getContext(), "Hour is " + i + " Minute is " + i1, Toast.LENGTH_SHORT).show();
                            btnStartTime.setText(i + ":" + i1);
                        }
                    }, 0, 0, false);
                    timePickerDialog.show();
                }
            });

            btnEndTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            btnEndTime.setText(i + ":" + i1);
                        }
                    }, 0, 0, false);
                    timePickerDialog.show();
                }
            });
        }
    }

//    private void insertTimeFor(int lectureNo,int startHour,int startMin,int endHour, int endMin){
//        long startTimeInMillis = ConverterUtils.convertTimeInInt(startHour,startMin);
//        long endTimeInMillis = ConverterUtils.convertTimeInInt(endHour,endMin);
//        setUpViewModel.setLectureTimeInSharedPrefs(lectureNo,startTimeInMillis,endTimeInMillis);
//    }

    //TODO Validation check left here
    @OnClick(R.id.btn_set_up_lecture_time_continue)
    void clickedContinue() {
        int maxIndex = (noOfLecturesPerDay * 2);
        for (int i = 0; i < maxIndex; i++) {
            int time = convertTimeInInt(((Button) linearContainer.getChildAt(i)).getText().toString());
            if (i % 2 == 0) {
                setUpViewModel.setLectureStartTimeInSharedPrefs(i / 2, time);
                Logger.d("Setting starting time for Lecture " + i / 2 + "as " + time + " Minutes");
            } else {
                setUpViewModel.setLectureEndTimeInSharedPrefs(i / 2, time);
                Logger.d("Setting ending time for Lecture " + i / 2 + "as " + time + " Minutes");
            }
        }
        if (listener != null) {
            listener.OnLectureTimeSelected();
        }
    }

    @OnClick(R.id.btn_set_up_lecture_time_previous)
    void clickedPrevious() {
        if (listener != null) {
            listener.OnPreviousClickedOnSetUpLectureTime();
        }
    }

    public void setOnLectureTimeSelectedListener(OnLectureTimeSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnLectureTimeSelectedListener {
        void OnLectureTimeSelected();

        void OnPreviousClickedOnSetUpLectureTime();
    }
}
