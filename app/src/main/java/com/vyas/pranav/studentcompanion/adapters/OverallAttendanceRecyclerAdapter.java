package com.vyas.pranav.studentcompanion.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.ui.activities.OverallAttendanceDetailActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.itangqi.waveloadingview.WaveLoadingView;

public class OverallAttendanceRecyclerAdapter extends RecyclerView.Adapter<OverallAttendanceRecyclerAdapter.OverallAttendanceHolder> {

    private List<OverallAttendanceEntry> overallAttendanceEntries;
    @NonNull
    @Override
    public OverallAttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_overall_attendance, parent, false);
        return new OverallAttendanceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OverallAttendanceHolder holder, int position) {
        if (overallAttendanceEntries == null) {
            holder.progressPresent.setProgressValue(0);
            holder.tvAvailableToBunk.setText("XX");
            holder.tvSubject.setText("SUBJECT XX");
        } else {
            holder.tvSubject.setText(overallAttendanceEntries.get(position).getSubName());
            int presentDays = overallAttendanceEntries.get(position).getPresentDays();
            int bunkedDays = overallAttendanceEntries.get(position).getBunkedDays();
            int totalDays = overallAttendanceEntries.get(position).getTotalDays();
            float presentPresent = (presentDays * 100) / totalDays;
            int daysTotalAvailableToBunk = (int) Math.ceil(totalDays * 0.25);
            int daysAvailableToBunk = daysTotalAvailableToBunk - bunkedDays;
            holder.tvAvailableToBunk.setText("Available to Bunk " + daysAvailableToBunk);
            holder.progressPresent.setProgressValue((int) presentPresent);
            holder.progressPresent.setCenterTitle((int) presentPresent + " %");
        }
    }

    @Override
    public int getItemCount() {
        return (overallAttendanceEntries == null) ? 1 : overallAttendanceEntries.size();
    }

    class OverallAttendanceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.progress_recycler_overall_attendance_present_percent)
        WaveLoadingView progressPresent;
        @BindView(R.id.tv_recycler_overall_attendance_available_to_bunk)
        TextView tvAvailableToBunk;
        @BindView(R.id.tv_recycler_overall_attendance_subject)
        TextView tvSubject;

        OverallAttendanceHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent openDetail = new Intent(view.getContext(), OverallAttendanceDetailActivity.class);
            Gson gson = new Gson();
            String JsonOverallAttendance = gson.toJson(overallAttendanceEntries.get(getAdapterPosition()));
            openDetail.putExtra(OverallAttendanceDetailActivity.EXTRA_OVERALL_ATTENDANCE, JsonOverallAttendance);
            view.getContext().startActivity(openDetail);
        }
    }

    public void setOverallAttendanceEntries(List<OverallAttendanceEntry> overallAttendanceEntries) {
        this.overallAttendanceEntries = overallAttendanceEntries;
        notifyDataSetChanged();
    }
}
