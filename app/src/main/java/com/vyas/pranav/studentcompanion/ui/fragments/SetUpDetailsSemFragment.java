package com.vyas.pranav.studentcompanion.ui.fragments;


import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.viewmodels.SetUpViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetUpDetailsSemFragment extends Fragment {

    @BindView(R.id.btn_set_up_details_sem_fragment_go)
    Button go;
    @BindView(R.id.btn_set_up_details_dem_continue)
    Button btnContinue;
    @BindView(R.id.et_set_up_details_sem_fragment_no_of_subjects)
    EditText etNoOfSubjects;
    @BindView(R.id.et_set_up_details_sem_fragment_sem_no)
    EditText etSemNo;
    @BindView(R.id.et_set_up_sem_details_no_of_lectures)
    EditText etNoOfLecturesPerDay;
    @BindView(R.id.linear_set_up_details_sem_container)
    LinearLayout linearContainer;
    @BindView(R.id.text_input_set_up_details_sem_fragment_sem_no)
    TextInputLayout inputSemNo;
    @BindView(R.id.text_input_set_up_details_sem_fragment_no_of_subjects)
    TextInputLayout inputSubject;
    @BindView(R.id.text_input_set_up_sem_details_no_of_lectures)
    TextInputLayout inputNoOfLectures;

    private SetUpViewModel setUpViewModel;
    private OnSubjectsSelectedListener listener;

    private String semNo;
    private String noOfSubjects;
    private String noOfLecturesPerDay;


    public SetUpDetailsSemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_up_details_sem, container, false);
        ButterKnife.bind(this, view);
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViewModel = ViewModelProviders.of(getActivity()).get(SetUpViewModel.class);
        btnContinue.setEnabled(false);
    }

    @OnClick(R.id.btn_set_up_details_sem_fragment_go)
    void goClicked() {
        semNo = etSemNo.getText().toString().trim();
        noOfSubjects = etNoOfSubjects.getText().toString().trim();
        noOfLecturesPerDay = etNoOfLecturesPerDay.getText().toString().trim();

        if (!validateSemNo() || !validateNoOfSubjects() || !validateNoOfLectures()) {
            if (!validateNoOfLectures()) {
                inputNoOfLectures.setError("Please enter valid No Of Lecture");
            } else {
                inputNoOfLectures.setErrorEnabled(false);
            }

            if (!validateNoOfSubjects()) {
                inputSubject.setError("Please Enter valid No Of Subjects");
            } else {
                inputSubject.setErrorEnabled(false);
            }

            if (!validateSemNo()) {
                inputSemNo.setError("Please Enter valid sem No");
            } else {
                inputSemNo.setErrorEnabled(false);
            }
            return;
        }
        inputSemNo.setErrorEnabled(false);
        inputSubject.setErrorEnabled(false);
        inputNoOfLectures.setErrorEnabled(false);
        setUpViewModel.setSemester(Integer.parseInt(semNo));
        setUpViewModel.setNoOfLecturesPerDay(Integer.parseInt(noOfLecturesPerDay));
        linearContainer.removeAllViews();
        for (int i = 0; i < Integer.parseInt(noOfSubjects); i++) {
            LinearLayout subjectDetailsContainer = new LinearLayout(getContext());
            subjectDetailsContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            subjectDetailsContainer.setOrientation(LinearLayout.HORIZONTAL);
//            TextInputLayout input1 = new TextInputLayout(getContext());

            EditText et = new EditText(getContext());
            et.setHint("Subject Name " + (i + 1));
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            et.setMaxLines(1);
            et.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            subjectDetailsContainer.addView(et);

//            input1.addView(et);

            EditText et2 = new EditText(getContext());
            et2.setHint("Credits of Subject " + (i + 1));
            et2.setInputType(InputType.TYPE_CLASS_NUMBER);
            et2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            subjectDetailsContainer.addView(et2);

            linearContainer.addView(subjectDetailsContainer, i);
        }
        btnContinue.setEnabled(true);
    }

    @OnClick(R.id.btn_set_up_details_dem_previous)
    void clickedPrevious() {
        if (listener != null) {
            listener.onPreviousClickedOnSemSetUp();
        }
    }

    @OnClick(R.id.btn_set_up_details_dem_continue)
    void clickedContinue() {
        String noOfSubjects = etNoOfSubjects.getText().toString().trim();
        List<String> subjects = new ArrayList<>();
        List<String> credits = new ArrayList<>();
        int noOfSubjectsNo = Integer.parseInt(noOfSubjects);
        for (int i = 0; i < noOfSubjectsNo; i++) {
            LinearLayout subjectDetailsContainer = (LinearLayout) linearContainer.getChildAt(i);
            String strSubject = ((EditText) subjectDetailsContainer.getChildAt(0)).getText().toString().trim();
            String strCredits = ((EditText) subjectDetailsContainer.getChildAt(1)).getText().toString().trim();
            if (strSubject.isEmpty()) {
                Toast.makeText(getContext(), "Please Fill Subject " + (i / 2), Toast.LENGTH_SHORT).show();
                return;
            }
            subjects.add(strSubject);
            if (strCredits.isEmpty() || Integer.parseInt(strCredits) < 1) {
                Toast.makeText(getContext(), "Please Fill Appropriate Credit for Subject " + (i / 2), Toast.LENGTH_SHORT).show();
                return;
            }
            credits.add(strCredits);

        }
        setUpViewModel.setListSubjectAndCredits(subjects, credits);
        if (listener != null) {
            listener.onSubjectSelected();
        }
    }

    public void setOnSubjectSelectedListener(OnSubjectsSelectedListener listener) {
        this.listener = listener;
    }

    private boolean validateSemNo() {
        if (semNo.isEmpty()) {
            return false;
        }
        return Integer.valueOf(semNo) >= 1;
    }

    private boolean validateNoOfSubjects() {
        if (noOfSubjects.isEmpty()) {
            return false;
        }
        return Integer.valueOf(noOfSubjects) >= 1;
    }

    private boolean validateNoOfLectures() {
        if (noOfLecturesPerDay.isEmpty()) {
            return false;
        }
        return Integer.valueOf(noOfLecturesPerDay) >= 1;
    }

    public interface OnSubjectsSelectedListener {
        void onSubjectSelected();
        void onPreviousClickedOnSemSetUp();
    }

}
