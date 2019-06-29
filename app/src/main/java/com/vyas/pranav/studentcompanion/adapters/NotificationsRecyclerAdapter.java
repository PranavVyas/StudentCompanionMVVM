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
import com.vyas.pranav.studentcompanion.utils.GlideApp;

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

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
