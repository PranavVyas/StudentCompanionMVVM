package com.vyas.pranav.studentcompanion.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.TimetableDayRecyclerAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vyas.pranav.studentcompanion.utils.Constants.EXTRA_TIMETABLE_DAY;
import static com.vyas.pranav.studentcompanion.utils.Constants.EXTRA_TIMETABLE_DAY_KEY;

public class TimetableDayFragment extends Fragment {

    @BindView(R.id.tv_fragment_timetable_day_day)
    TextView tvDay;
    @BindView(R.id.recycler_fragment_timetable_day_main)
    RecyclerView rvDays;
    private List<String> lectures;
    private String day;
    private TimetableDayRecyclerAdapter mAdapter;

    public TimetableDayFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable_day, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle receivedData = getArguments();
        if (receivedData != null) {
            day = receivedData.getString(EXTRA_TIMETABLE_DAY);
            lectures = receivedData.getStringArrayList(EXTRA_TIMETABLE_DAY_KEY);
        }
        setUpRecycler();
        tvDay.setText(day);
    }

    private void setUpRecycler() {
        rvDays.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mAdapter = new TimetableDayRecyclerAdapter();
        mAdapter.setHasStableIds(true);
        rvDays.setAdapter(mAdapter);
        mAdapter.setData(lectures);
    }
}
