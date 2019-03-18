package com.vyas.pranav.studentcompanion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vyas.pranav.studentcompanion.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TimetableDayRecyclerAdapter extends RecyclerView.Adapter<TimetableDayRecyclerAdapter.TimetableDayHolder> {

    private List<String> lectures;

    @NonNull
    @Override
    public TimetableDayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimetableDayHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_timetable_day, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimetableDayHolder holder, int position) {
        holder.tvDetail.setText("Lecture : " + (position + 1) +
                "\n" + lectures.get(position));
    }

    @Override
    public int getItemCount() {
        return (lectures == null) ? 0 : lectures.size();
    }

    public void setData(List<String> lectures) {
        this.lectures = lectures;
        notifyDataSetChanged();
    }

    class TimetableDayHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recycler_timetable_day_lecture_detail)
        TextView tvDetail;

        public TimetableDayHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
