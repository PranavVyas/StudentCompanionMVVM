package com.vyas.pranav.studentcompanion.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.attendancedatabase.AttendanceEntry;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
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
    public void onBindViewHolder(@NonNull AttendanceIndividualHolder holder, int position) {
        if (attendanceEntries != null) {
            Log.d(TAG, "onBindViewHolder: Lecture No " + attendanceEntries.get(position).getLectureNo());
            holder.tvLectureNo.setText("Lecture " + attendanceEntries.get(position).getLectureNo());
            holder.tvSubjectName.setText(attendanceEntries.get(position).getSubjectName());
            holder.tvSwitchSummery.setText(attendanceEntries.get(position).isPresent() ? "Present" : "Absent");
            holder.switchPresent.setChecked(attendanceEntries.get(position).isPresent());
        } else {
            holder.tvLectureNo.setText("Lecture XX");
            holder.tvSubjectName.setText("Subject Name");
            holder.tvSwitchSummery.setText("Present");
            holder.switchPresent.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return (attendanceEntries == null) ? 1 : attendanceEntries.size();
    }

    public void setAttendanceForDate(List<AttendanceEntry> attendanceEntries) {
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
        @BindView(R.id.tv_recycler_individual_attendance_switch_summery)
        TextView tvSwitchSummery;
        @BindView(R.id.switch_recycler_individual_attendance_present)
        SwitchCompat switchPresent;

        AttendanceIndividualHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            switchPresent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    //TODO Attach comment here for the if statement
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        AttendanceEntry attendanceEntry = attendanceEntries.get(position);
                        attendanceEntry.setPresent(switchPresent.isChecked());
                        listener.onAttendanceSwitchToggled(attendanceEntry);
                    }
                }
            });
        }
    }

}
