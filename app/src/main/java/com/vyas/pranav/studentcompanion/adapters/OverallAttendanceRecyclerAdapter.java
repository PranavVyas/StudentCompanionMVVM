package com.vyas.pranav.studentcompanion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.itangqi.waveloadingview.WaveLoadingView;

public class OverallAttendanceRecyclerAdapter extends RecyclerView.Adapter<OverallAttendanceRecyclerAdapter.OverallAttendanceHolder> {

    List<OverallAttendanceEntry> overallAttendanceEntries;
    @NonNull
    @Override
    public OverallAttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_overall_attendance, parent, false);
        return new OverallAttendanceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OverallAttendanceHolder holder, int position) {
        if (overallAttendanceEntries == null) {
            holder.progressPresent.setProgressValue(50);
            holder.tvAvailableToBunk.setText("HIi");
            holder.tvSubject.setText("Sunhetdjsdhd");
        } else {
            holder.tvSubject.setText(overallAttendanceEntries.get(position).getSubName());
            int presentDays = overallAttendanceEntries.get(position).getPresentDays();
            int bunkedDays = overallAttendanceEntries.get(position).getBunkedDays();
            int totalDays = overallAttendanceEntries.get(position).getTotalDays();
            float presentPersent = (presentDays * 100) / totalDays;
            int daysTotalAvailableToBunk = (int) Math.ceil(totalDays * 0.25);
            int daysAvailableToBunk = daysTotalAvailableToBunk - bunkedDays;
            holder.tvAvailableToBunk.setText("Available to Bunk " + daysAvailableToBunk);
            holder.progressPresent.setProgressValue((int) presentPersent);
            holder.progressPresent.setCenterTitle((int) presentPersent + " %");
        }
    }

    @Override
    public int getItemCount() {
        return (overallAttendanceEntries == null) ? 1 : overallAttendanceEntries.size();
    }

    class OverallAttendanceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.progress_recycler_overall_attendance_present_percent)
        WaveLoadingView progressPresent;
        @BindView(R.id.tv_recycler_overall_attendance_available_to_bunk)
        TextView tvAvailableToBunk;
        @BindView(R.id.tv_recycler_overall_attendance_subject)
        TextView tvSubject;

        public OverallAttendanceHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOverallAttendanceEntries(List<OverallAttendanceEntry> overallAttendanceEntries) {
        this.overallAttendanceEntries = overallAttendanceEntries;
        notifyDataSetChanged();
    }
}
