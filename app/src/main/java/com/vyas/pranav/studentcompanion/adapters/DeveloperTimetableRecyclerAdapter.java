package com.vyas.pranav.studentcompanion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeveloperTimetableRecyclerAdapter extends ListAdapter<TimetableEntry, DeveloperTimetableRecyclerAdapter.TimetableHolder> {

    private static final DiffUtil.ItemCallback<TimetableEntry> diffCallback = new DiffUtil.ItemCallback<TimetableEntry>() {
        @Override
        public boolean areItemsTheSame(@NonNull TimetableEntry oldItem, @NonNull TimetableEntry newItem) {
            return oldItem.get_ID().equals(newItem.get_ID());
        }

        @Override
        public boolean areContentsTheSame(@NonNull TimetableEntry oldItem, @NonNull TimetableEntry newItem) {
            return (oldItem.getDay().equals(newItem.getDay())) &&
                    (oldItem.getLectureNo() == newItem.getLectureNo()) &&
                    (oldItem.getSubName().equals(newItem.getSubName())) &&
                    (oldItem.getTimeEnd() == newItem.getTimeEnd()) &&
                    (oldItem.getTimeStart() == newItem.getTimeStart());
        }
    };

    public DeveloperTimetableRecyclerAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public TimetableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimetableHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_developer_timetable, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull TimetableHolder holder, int position) {
        bindUi(getItem(position), holder);

    }

    private void bindUi(TimetableEntry entry, TimetableHolder holder) {
        holder.tvDay.setText(entry.getDay());
        holder.tvDuration.setText(ConverterUtils.convertTimeIntInString(entry.getTimeStart()) + " TO " + ConverterUtils.convertTimeIntInString(entry.getTimeEnd()));
        holder.tvLecture.setText("" + entry.getLectureNo());
        holder.tvSubject.setText(entry.getSubName());
    }

    class TimetableHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recycler_dev_timetable_sub)
        TextView tvSubject;
        @BindView(R.id.tv_recycler_dev_timetable_day)
        TextView tvDay;
        @BindView(R.id.tv_recycler_dev_timetable_duration)
        TextView tvDuration;
        @BindView(R.id.tv_recycler_dev_timetable_lecture)
        TextView tvLecture;

        public TimetableHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
