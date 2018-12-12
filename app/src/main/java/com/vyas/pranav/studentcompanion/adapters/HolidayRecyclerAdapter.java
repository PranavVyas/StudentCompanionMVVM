package com.vyas.pranav.studentcompanion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayEntry;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HolidayRecyclerAdapter extends RecyclerView.Adapter<HolidayRecyclerAdapter.HolidayHolder> {

    private List<HolidayEntry> holidays;

    @NonNull
    @Override
    public HolidayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_holiday, parent, false);
        return new HolidayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolidayHolder holder, int position) {
        holder.tvNo.setText("" + (position + 1));
        holder.tvDate.setText(ConverterUtils.convertDateToString(holidays.get(position).getDate()));
        holder.tvDay.setText(holidays.get(position).getDay());
        holder.tvName.setText(holidays.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return holidays == null ? 0 : holidays.size();
    }

    public void setHolidays(List<HolidayEntry> holidays) {
        this.holidays = holidays;
        notifyDataSetChanged();
    }

    class HolidayHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recycler_holiday_fragment_no)
        TextView tvNo;
        @BindView(R.id.tv_recycler_holiday_fragment_date)
        TextView tvDate;
        @BindView(R.id.tv_recycler_holiday_fragment_day)
        TextView tvDay;
        @BindView(R.id.tv_recycler_holiday_fragment_name)
        TextView tvName;

        public HolidayHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
