package com.vyas.pranav.studentcompanion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.NotificationEntry;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.GlideApp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsRecyclerAdapter extends ListAdapter<NotificationEntry, NotificationsRecyclerAdapter.NotificationHolder> {

    public static final DiffUtil.ItemCallback<NotificationEntry> diffCallback = new DiffUtil.ItemCallback<NotificationEntry>() {
        @Override
        public boolean areItemsTheSame(@NonNull NotificationEntry oldItem, @NonNull NotificationEntry newItem) {
            return oldItem.get_ID() == newItem.get_ID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull NotificationEntry oldItem, @NonNull NotificationEntry newItem) {
            return (oldItem.getDate().equals(newItem.getDate())) &&
                    (oldItem.getImageUrl().equals(newItem.getImageUrl())) &&
                    (oldItem.getSubtitle().equals(newItem.getSubtitle())) &&
                    (oldItem.getTitle().equals(newItem.getTitle()));
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
        NotificationEntry notification = getItem(position);
        holder.tvDate.setText(ConverterUtils.convertDateToString(notification.getDate()));
        holder.tvTitle.setText(notification.getTitle());
        holder.tvSubtitle.setText(notification.getSubtitle());
        GlideApp.with(holder.itemView)
                .load(notification.getImageUrl())
                .error(R.drawable.ic_caution)
                .placeholder(R.drawable.ic_caution)
                .circleCrop()
                .into(holder.imageItem);
    }

    class NotificationHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_recycler_notification_photo)
        ImageView imageItem;
        @BindView(R.id.tv_recycler_notification_title)
        TextView tvTitle;
        @BindView(R.id.tv_recycler_notification_subtitle)
        TextView tvSubtitle;
        @BindView(R.id.tv_recycler_notification_date)
        TextView tvDate;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
