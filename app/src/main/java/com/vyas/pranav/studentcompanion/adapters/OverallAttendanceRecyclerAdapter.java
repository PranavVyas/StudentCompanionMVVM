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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.overallattendancedatabase.OverallAttendanceEntry;
import com.vyas.pranav.studentcompanion.ui.activities.OverallAttendanceDetailActivity;
import com.vyas.pranav.studentcompanion.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.itangqi.waveloadingview.WaveLoadingView;

public class OverallAttendanceRecyclerAdapter extends ListAdapter<OverallAttendanceEntry, OverallAttendanceRecyclerAdapter.OverallAttendanceHolder> {

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
    private SharedPreferencesUtils sharedPreferencesUtils;
    private int currentAttendance = 0;

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
            sharedPreferencesUtils = SharedPreferencesUtils.getInstance(context);
            currentAttendance = sharedPreferencesUtils.getCurrentAttendanceCriteria();
        }
        return currentAttendance;
    }

    @Override
    public void onBindViewHolder(@NonNull OverallAttendanceHolder holder, int position) {
        OverallAttendanceEntry item = getItem(position);

        ViewCompat.setTransitionName(holder.progressPresent, item.getSubName() + item.get_ID());
        ViewCompat.setTransitionName(holder.tvSubject, item.getSubName() + item.getSubName());

        holder.tvSubject.setText(item.getSubName());
        Context context = holder.itemView.getContext();
        int presentDays = item.getPresentDays();
        int bunkedDays = item.getBunkedDays();
        int totalDays = item.getTotalDays();
        int currentAttendanceCriteria = getCurrentAttendanceCriteria(holder.itemView.getContext());
        //For Example currentAttendanceCriteria = 75
        double dangerPercent = 100 - ((100.0 - currentAttendanceCriteria) * 3 / 3);
        //dangerPercent = 75
        double warningPercent = 100 - ((100.0 - currentAttendanceCriteria) * 2 / 3);
        //warning Percent = 83.33
        double safePercent = 100 - ((100.0 - currentAttendanceCriteria) / 3);
        //safe Percent = 91.67
        if (totalDays == 0) {
            holder.tvAvailableToBunk.setText("Subject is not in the timetable");
            holder.progressPresent.setProgressValue(100);
            holder.progressPresent.setCenterTitle("100 %");
            return;
        }

        View.OnClickListener listener = view -> {
            Intent openDetail = new Intent(view.getContext(), OverallAttendanceDetailActivity.class);
            Gson gson = new Gson();
            String JsonOverallAttendance = gson.toJson(getItem(position));
            if (getItem(position).getTotalDays() == 0) {
                Toast.makeText(view.getContext(), "Subject is not available in the timetable", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(),
                    Pair.create(holder.progressPresent, ViewCompat.getTransitionName(holder.progressPresent)),
                    Pair.create(holder.tvSubject, ViewCompat.getTransitionName(holder.tvSubject))).toBundle();
            openDetail.putExtra(OverallAttendanceDetailActivity.EXTRA_OVERALL_ATTENDANCE, JsonOverallAttendance);
            view.getContext().startActivity(openDetail, bundle);
        };

        holder.itemView.setOnClickListener(listener);
        holder.btnMore.setOnClickListener(listener);

        float presentPresent = (presentDays * 100) / totalDays;
        int daysTotalAvailableToBunk = (int) Math.ceil(totalDays * (1f - (currentAttendanceCriteria / 100.0f)));
        int daysAvailableToBunk = daysTotalAvailableToBunk - bunkedDays;
        holder.tvAvailableToBunk.setText(context.getString(R.string.overall_days_available_to_bunk) + daysAvailableToBunk);
        holder.progressPresent.setProgressValue((int) presentPresent);
        holder.progressPresent.setCenterTitle((int) presentPresent + " %");
        int maxAttendance = (int) Math.ceil(((totalDays - bunkedDays) * 100.0) / totalDays);
        if (maxAttendance > safePercent) {
            holder.cardMain.setCardBackgroundColor(context.getResources().getColor(R.color.colorSafeOverallAttendance));
            return;
        }
        if (maxAttendance > warningPercent) {
            holder.cardMain.setCardBackgroundColor(context.getResources().getColor(R.color.colorWarningOverallAttendance));
            return;
        }
        if (maxAttendance > 0) {
            holder.cardMain.setCardBackgroundColor(context.getResources().getColor(R.color.colorDangerOverallAttendance));
            return;
        }
    }

    class OverallAttendanceHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_recycler_overall_attendance_present_percent)
        WaveLoadingView progressPresent;
        @BindView(R.id.tv_recycler_overall_attendance_available_to_bunk)
        TextView tvAvailableToBunk;
        @BindView(R.id.tv_recycler_overall_attendance_subject)
        TextView tvSubject;
        @BindView(R.id.card_recycler_overall_main)
        MaterialCardView cardMain;
        @BindView(R.id.btn_overall_attendance_more)
        Button btnMore;

        OverallAttendanceHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
