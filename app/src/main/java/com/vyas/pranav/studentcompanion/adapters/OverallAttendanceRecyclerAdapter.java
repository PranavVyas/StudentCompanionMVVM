package com.vyas.pranav.studentcompanion.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.ui.activities.OverallAttendanceDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.itangqi.waveloadingview.WaveLoadingView;

public class OverallAttendanceRecyclerAdapter extends ListAdapter<OverallAttendanceEntry, OverallAttendanceRecyclerAdapter.OverallAttendanceHolder> {

    private SharedPreferencesUtils sharedPreferencesUtils;
    private int currentAttendance = 0;


    private static final DiffUtil.ItemCallback<OverallAttendanceEntry> diffCallback = new DiffUtil.ItemCallback<OverallAttendanceEntry>() {
        @Override
        public boolean areItemsTheSame(@NonNull OverallAttendanceEntry oldItem, @NonNull OverallAttendanceEntry newItem) {
            return oldItem.get_ID() == newItem.get_ID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull OverallAttendanceEntry oldItem, @NonNull OverallAttendanceEntry newItem) {
            return (oldItem.getSubName().equals(newItem.getSubName())) &&
                    (oldItem.getPresentDays() == newItem.getPresentDays()) &&
                    (oldItem.getBunkedDays() == newItem.getBunkedDays()) &&
                    (oldItem.getTotalDays() == newItem.getTotalDays());
        }
    };

    public OverallAttendanceRecyclerAdapter() {
        super(diffCallback);
    }


    @NonNull
    @Override
    public OverallAttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_overall_attendance, parent, false);
        return new OverallAttendanceHolder(view);
    }

    private int getCurrentAttendanceCriteria(Context context) {
        if (sharedPreferencesUtils == null) {
            sharedPreferencesUtils = new SharedPreferencesUtils(context);
            currentAttendance = sharedPreferencesUtils.getCurrentAttendanceCriteria();
        }
        return currentAttendance;
    }

    @Override
    public void onBindViewHolder(@NonNull OverallAttendanceHolder holder, int position) {
        OverallAttendanceEntry item = getItem(position);
        holder.tvSubject.setText(item.getSubName());
        int presentDays = item.getPresentDays();
        int bunkedDays = item.getBunkedDays();
        int totalDays = item.getTotalDays();
        int currentAttendanceCriteria = getCurrentAttendanceCriteria(holder.itemView.getContext());
        if (totalDays == 0) {
            holder.tvAvailableToBunk.setText("Subject is not in the timetable");
            holder.progressPresent.setProgressValue(100);
            holder.progressPresent.setCenterTitle("100 %");
            return;
        }
        float presentPresent = (presentDays * 100) / totalDays;
        int daysTotalAvailableToBunk = (int) Math.ceil(totalDays * (1f - (currentAttendanceCriteria / 100.0f)));
        int daysAvailableToBunk = daysTotalAvailableToBunk - bunkedDays;
        holder.tvAvailableToBunk.setText("Available to Bunk " + daysAvailableToBunk);
        holder.progressPresent.setProgressValue((int) presentPresent);
        holder.progressPresent.setCenterTitle((int) presentPresent + " %");
        if (presentPresent > 90) {
            holder.cardMain.setBackgroundResource(R.color.colorSafeOverallAttendance);
            return;
        }
        if (presentPresent > 75) {
            holder.cardMain.setBackgroundResource(R.color.colorWarningOverallAttendance);
            return;
        }
        if (presentPresent < 76) {
            holder.cardMain.setBackgroundResource(R.color.colorDangerOverallAttendance);
            return;
        }
    }

    class OverallAttendanceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.progress_recycler_overall_attendance_present_percent)
        WaveLoadingView progressPresent;
        @BindView(R.id.tv_recycler_overall_attendance_available_to_bunk)
        TextView tvAvailableToBunk;
        @BindView(R.id.tv_recycler_overall_attendance_subject)
        TextView tvSubject;
        @BindView(R.id.card_recycler_overall_main)
        MaterialCardView cardMain;
        @BindView(R.id.btn_overall_attendance_more)
        ImageButton btnMore;

        OverallAttendanceHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            btnMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent openDetail = new Intent(view.getContext(), OverallAttendanceDetailActivity.class);
            Gson gson = new Gson();
            String JsonOverallAttendance = gson.toJson(getItem(getAdapterPosition()));
            if (getItem(getAdapterPosition()).getTotalDays() == 0) {
                Toast.makeText(view.getContext(), "Subject is not available in the timetable", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(),
                    Pair.create(progressPresent, progressPresent.getTransitionName()),
                    Pair.create(tvSubject, tvSubject.getTransitionName())).toBundle();
            openDetail.putExtra(OverallAttendanceDetailActivity.EXTRA_OVERALL_ATTENDANCE, JsonOverallAttendance);
            view.getContext().startActivity(openDetail, bundle);
        }
    }
}
