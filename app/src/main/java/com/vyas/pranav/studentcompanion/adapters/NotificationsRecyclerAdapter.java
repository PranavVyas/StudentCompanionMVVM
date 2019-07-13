package com.vyas.pranav.studentcompanion.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.firestore.NotificationFirestoreModel;
import com.vyas.pranav.studentcompanion.ui.activities.MainActivity;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.GlideApp;
import com.vyas.pranav.studentcompanion.utils.NavigationDrawerUtil;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsRecyclerAdapter extends ListAdapter<NotificationFirestoreModel, NotificationsRecyclerAdapter.NotificationHolder> {

    private static final DiffUtil.ItemCallback<NotificationFirestoreModel> diffCallback = new DiffUtil.ItemCallback<NotificationFirestoreModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull NotificationFirestoreModel oldItem, @NonNull NotificationFirestoreModel newItem) {
            return oldItem.getUrl().equals(newItem.getUrl());
        }

        @Override
        public boolean areContentsTheSame(@NonNull NotificationFirestoreModel oldItem, @NonNull NotificationFirestoreModel newItem) {
            return (oldItem.getDateInMillis() == (newItem.getDateInMillis())) &&
                    (oldItem.getShort_info().equals(newItem.getShort_info())) &&
                    (oldItem.getVenue().equals(newItem.getVenue())) &&
                    (oldItem.getName().equals(newItem.getName())) &&
                    (oldItem.getType() == newItem.getType());
        }
    };

    public NotificationsRecyclerAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_notifications, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        NotificationFirestoreModel notification = getItem(position);
        Date date = new Date();
        date.setTime(notification.getDateInMillis());
        holder.tvDate.setText(ConverterUtils.convertDateToString(date));
        holder.tvName.setText(notification.getName());
        holder.tvShortInfo.setText(notification.getShort_info());

        if (notification.getType() == Constants.NOTI_TYPE_LOW_ATTENDANCE) {
            holder.btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent openOverallAttendance = new Intent(holder.itemView.getContext(), MainActivity.class);
                    openOverallAttendance.putExtra(Constants.EXTRA_MAIN_ACT_OPEN_OVERALL, NavigationDrawerUtil.ID_OVERALL_ATTENDANCE);
                    holder.itemView.getContext().startActivity(openOverallAttendance);
                }
            });
            GlideApp.with(holder.itemView)
                    .load(R.drawable.ic_caution)
                    .error(R.drawable.ic_caution)
                    .placeholder(R.drawable.ic_caution)
                    .circleCrop()
                    .into(holder.imageItem);
            return;
        }
        GlideApp.with(holder.itemView)
                .load(notification.getImage_url())
                .error(R.drawable.ic_caution)
                .placeholder(R.drawable.ic_caution)
                .circleCrop()
                .into(holder.imageItem);

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openLink = new Intent(Intent.ACTION_VIEW, Uri.parse(notification.getUrl()));
                if (openLink.resolveActivity(view.getContext().getPackageManager()) != null) {
                    view.getContext().startActivity(openLink);
                } else {
                    Toast.makeText(view.getContext(), "Link seems to be broken!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class NotificationHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_recycler_notification_photo)
        ImageView imageItem;
        @BindView(R.id.tv_recycler_notification_name)
        TextView tvName;
        @BindView(R.id.tv_recycler_notification_short_info)
        TextView tvShortInfo;
        @BindView(R.id.tv_recycler_notification_date)
        TextView tvDate;
        @BindView(R.id.btn_recycler_notification_details)
        Button btnMore;

        NotificationHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
