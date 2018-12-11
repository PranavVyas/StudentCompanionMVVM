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
    @BindView(R.id.linear_set_up_details_sem_container)
    LinearLayout linearContainer;

    private SetUpViewModel setUpViewModel;
    private OnSubjectsSelectedListener listener;

    public SetUpDetailsSemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_up_details_sem, container, false);
        ButterKnife.bind(this, view);
        Logger.addLogAdapter(new AndroidLogAdapter());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViewModel = ViewModelProviders.of(getActivity()).get(SetUpViewModel.class);
    }

    @OnClick(R.id.btn_set_up_details_sem_fragment_go)
    void goClicked() {
        String semNo = etSemNo.getText().toString().trim();
        String noOfSubjects = etNoOfSubjects.getText().toString().trim();

        if (semNo == null || semNo.length() == 0 || noOfSubjects == null || noOfSubjects.length() == 0) {
            Toast.makeText(getContext(), "Please Fill all details", Toast.LENGTH_SHORT).show();
            return;
        }
        int index = 0;
        for (int i = 0; i < Integer.parseInt(noOfSubjects); i++) {
            EditText et = new EditText(getContext());
            et.setHint("Subject Name");
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            EditText et2 = new EditText(getContext());
            et2.setHint("Credits of Subject");
            et2.setInputType(InputType.TYPE_CLASS_NUMBER);
            et.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            et2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearContainer.addView(et, index);
            index++;
            linearContainer.addView(et2, index);
            index++;
        }
    }

    @OnClick(R.id.btn_set_up_details_dem_continue)
    void clickedContinue() {
        String noOfSubjects = etNoOfSubjects.getText().toString().trim();
        List<String> subjects = new ArrayList<>();
        List<String> credits = new ArrayList<>();
        int noOfSubjectsNo = Integer.parseInt(noOfSubjects);
        for (int i = 0; i < (noOfSubjectsNo * 2); i++) {
            String str = ((EditText) linearContainer.getChildAt(i)).getText().toString().trim();
            if (i % 2 == 0) {
                subjects.add(str);
            } else {
                credits.add(str);
            }
        }
        setUpViewModel.setListSubjectAndCredits(subjects, credits);
        if (listener != null) {
            listener.onSubjectSelected();
        }
    }

    public void setOnSubjectSelectedListener(OnSubjectsSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnSubjectsSelectedListener {
        void onSubjectSelected();
    }

}
