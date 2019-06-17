package com.vyas.pranav.studentcompanion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.firestore.NotificationFirestoreModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsRecyclerAdapter extends ListAdapter<NotificationFirestoreModel, NotificationsRecyclerAdapter.NotificationHolder> {

    public static final DiffUtil.ItemCallback<NotificationFirestoreModel> diffCallback = new DiffUtil.ItemCallback<NotificationFirestoreModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull NotificationFirestoreModel oldItem, @NonNull NotificationFirestoreModel newItem) {
            return oldItem.getUrl() == newItem.getUrl();
        }

        @Override
        public boolean areContentsTheSame(@NonNull NotificationFirestoreModel oldItem, @NonNull NotificationFirestoreModel newItem) {
            return (oldItem.getDate().equals(newItem.getDate())) &&
                    (oldItem.getShort_info().equals(newItem.getShort_info())) &&
                    (oldItem.getVenue().equals(newItem.getVenue()) &&
                            (oldItem.getName().equals(newItem.getName())));
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
        holder.tvDate.setText(notification.getDate());
        holder.tvName.setText(notification.getName());
        holder.tvShortInfo.setText(notification.getShort_info());
//        GlideApp.with(holder.itemView)
//                .load(null)
//                .error(R.drawable.ic_caution)
//                .placeholder(R.drawable.ic_caution)
//                .circleCrop()
//                .into(holder.imageItem);
        //TODO load image here
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

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
