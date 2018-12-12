package com.vyas.pranav.studentcompanion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vyas.pranav.studentcompanion.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SetUpTimetableRecyclerAdapter extends RecyclerView.Adapter<SetUpTimetableRecyclerAdapter.SetUpTimetableHolder> {
    private int lecturesPerDay = 0;
    private List<String> subjectsList = new ArrayList<>();
    private List<String> daySchedule = new ArrayList<>();

    @NonNull
    @Override
    public SetUpTimetableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_setup_timetable, parent, false);
        return new SetUpTimetableHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetUpTimetableHolder holder, int position) {
        holder.tvTitle.setText("Select subject for lecture No : " + (position + 1));
        holder.mSpinner.setSelection(holder.adapter.getPosition(daySchedule.get(position)));
    }

    @Override
    public int getItemCount() {
        return lecturesPerDay;
    }

    public void setItem(int lecturesPerDay) {
        this.lecturesPerDay = lecturesPerDay;
        String defaultSubject = subjectsList.get(0);
        daySchedule = new ArrayList<>();
        for (int i = 0; i < lecturesPerDay; i++) {
            daySchedule.add(i, defaultSubject);
        }
        notifyDataSetChanged();
    }

    public void setSubjectsList(List<String> subjectsList) {
        this.subjectsList = subjectsList;
    }

    public List<String> getDaySchedule() {
        return daySchedule;
    }

    class SetUpTimetableHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recycler_setup_timetable_title)
        TextView tvTitle;
        @BindView(R.id.spinner_recycler_setup_timetable_choose_subject)
        Spinner mSpinner;

        private ArrayAdapter<String> adapter;
        private String selectedSubject;

        SetUpTimetableHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            adapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_item, subjectsList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedSubject = (String) adapterView.getSelectedItem();
                    daySchedule.set(getAdapterPosition(), selectedSubject);
                    Toast.makeText(view.getContext(), "Selected Item is " + selectedSubject, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Toast.makeText(adapterView.getContext(), "Nothing is selected", Toast.LENGTH_SHORT).show();
                }
            });
            mSpinner.setAdapter(adapter);
        }
    }

    public void setUpSchedule(List<String> schedule) {
        this.daySchedule = schedule;
        notifyDataSetChanged();
    }

}
