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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceIndividualRecyclerAdapter extends ListAdapter<AttendanceEntry, AttendanceIndividualRecyclerAdapter.AttendanceIndividualHolder> {
    private static final String TAG = "AttendanceIndividualRec";

    private onAttendanceSwitchToggleListener listener;

    public static final DiffUtil.ItemCallback<AttendanceEntry> diffCallback = new DiffUtil.ItemCallback<AttendanceEntry>() {
        @Override
        public boolean areItemsTheSame(@NonNull AttendanceEntry oldItem, @NonNull AttendanceEntry newItem) {
            return oldItem.get_ID() == newItem.get_ID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull AttendanceEntry oldItem, @NonNull AttendanceEntry newItem) {
            return (oldItem.getDate().equals(newItem.getDate())) &&
                    (oldItem.getLectureNo() == newItem.getLectureNo()) &&
                    (oldItem.getSubjectName().equals(newItem.getSubjectName())) &&
                    (oldItem.isPresent() == newItem.isPresent());
        }
    };

    public AttendanceIndividualRecyclerAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public AttendanceIndividualHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_individual_attendance, parent, false);
        return new AttendanceIndividualHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceIndividualHolder holder, final int position) {
//        if(attendanceEntries.get(position).getSubjectName().equals(Constants.DEFAULT_LECTURE)){
//            holder.itemView.setVisibility(View.GONE);
//            return;
//        }
        AttendanceEntry attendanceOfDay = getItem(position);
        Log.d(TAG, "onBindViewHolder: Lecture No " + attendanceOfDay.getLectureNo());
        holder.tvLectureNo.setText("Lecture " + attendanceOfDay.getLectureNo());
        holder.tvSubjectName.setText(attendanceOfDay.getSubjectName());
        holder.switchPresent.setOn(attendanceOfDay.isPresent());

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
                    AttendanceEntry attendanceEntry = attendanceOfDay;
                    attendanceEntry.setPresent(isOn);
                    listener.onAttendanceSwitchToggled(attendanceEntry);
                } else {
                    Logger.d("Listener is not init");
                }
            }
        });
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
