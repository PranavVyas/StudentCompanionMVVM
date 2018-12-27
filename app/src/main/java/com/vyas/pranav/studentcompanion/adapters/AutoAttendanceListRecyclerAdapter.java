package com.vyas.pranav.studentcompanion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.places.PlaceBuffer;
import com.vyas.pranav.studentcompanion.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AutoAttendanceListRecyclerAdapter extends RecyclerView.Adapter<AutoAttendanceListRecyclerAdapter.AutoAttendancePlaceHolder> {

    private PlaceBuffer places;
    private List<String> subjects;

    @NonNull
    @Override
    public AutoAttendancePlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_auto_attendance_list, parent, false);
        return new AutoAttendancePlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AutoAttendancePlaceHolder holder, int position) {
        String name = "" + places.get(position).getName();
        String address = "" + places.get(position).getAddress();
        holder.tvAddress.setText(address);
        holder.tvName.setText(name);
        holder.tvNo.setText("" + (position + 1));
        holder.tvSubject.setText(subjects.get(position));
    }

    @Override
    public int getItemCount() {
        return places == null ? 0 : places.getCount();
    }

    public void setPlaces(PlaceBuffer places, List<String> subjects) {
        this.places = places;
        this.subjects = subjects;
        notifyDataSetChanged();
    }

    class AutoAttendancePlaceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recycler_auto_attendance_list_address)
        TextView tvAddress;
        @BindView(R.id.tv_recycler_auto_attendance_list_no_old)
        TextView tvNo;
        @BindView(R.id.tv_recycler_auto_attendance_list_name)
        TextView tvName;
        @BindView(R.id.tv_recycler_auto_attendance_list_subject)
        TextView tvSubject;

        public AutoAttendancePlaceHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

}
