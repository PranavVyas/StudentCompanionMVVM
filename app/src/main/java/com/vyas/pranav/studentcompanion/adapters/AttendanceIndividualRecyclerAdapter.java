package com.vyas.pranav.studentcompanion.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceIndividualRecyclerAdapter extends RecyclerView.Adapter<AttendanceIndividualRecyclerAdapter.AttendanceIndividualHolder> {
    private static final String TAG = "AttendanceIndividualRec";

    private List<AttendanceEntry> attendanceEntries;
    private onAttendanceSwitchToggleListener listener;

    @NonNull
    @Override
    public AttendanceIndividualHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_individual_attendance, parent, false);
        return new AttendanceIndividualHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceIndividualHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: Lecture No " + attendanceEntries.get(position).getLectureNo());
        holder.tvLectureNo.setText("Lecture " + attendanceEntries.get(position).getLectureNo());
        holder.tvSubjectName.setText(attendanceEntries.get(position).getSubjectName());
        holder.switchPresent.setOn(attendanceEntries.get(position).isPresent());

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.switchPresent.performClick();
            }
        };
        holder.itemView.setOnClickListener(onClickListener);
        holder.switchPresent.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (listener != null) {
                    AttendanceEntry attendanceEntry = attendanceEntries.get(position);
                    attendanceEntry.setPresent(isOn);
                    listener.onAttendanceSwitchToggled(attendanceEntry);
                } else {
                    Logger.d("Listener is not init");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (attendanceEntries == null) ? 0 : attendanceEntries.size();
    }

    public void setAttendanceForDate(List<AttendanceEntry> attendanceEntries) {
        this.attendanceEntries = new ArrayList<>();
        this.attendanceEntries = attendanceEntries;
        notifyDataSetChanged();
    }

    public void setOnAttendanceSwitchToggledListener(onAttendanceSwitchToggleListener listener) {
        this.listener = listener;
    }

    public interface onAttendanceSwitchToggleListener {
        void onAttendanceSwitchToggled(AttendanceEntry attendanceEntry);
    }

    class AttendanceIndividualHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recycler_individual_attendance_leture_no)
        TextView tvLectureNo;
        @BindView(R.id.tv_recycler_individual_attendance_subject_name)
        TextView tvSubjectName;
        @BindView(R.id.switch_recycler_individual_attendance_present)
        LabeledSwitch switchPresent;

        AttendanceIndividualHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
