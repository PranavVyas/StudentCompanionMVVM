package com.vyas.pranav.studentcompanion.adapters;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.notificationdatabase.firestore.NotificationFirestoreModel;
import com.vyas.pranav.studentcompanion.utils.Constants;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.GlideApp;

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
            holder.btnMore.setVisibility(View.GONE);
//                    .setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent openOverallAttendance = new Intent(holder.itemView.getContext(), MainActivity.class);
//                    openOverallAttendance.putExtra(Constants.EXTRA_MAIN_ACT_OPEN_OVERALL, NavigationDrawerUtil.ID_OVERALL_ATTENDANCE);
//                    holder.itemView.getContext().startActivity(openOverallAttendance);
//                    Toast.makeText(view.getContext(), "Intent Fired", Toast.LENGTH_SHORT).show();
//                }
//            });
            GlideApp.with(holder.itemView)
                    .load(R.drawable.ic_caution)
                    .error(R.drawable.ic_caution)
                    .placeholder(R.drawable.ic_caution)
                    .circleCrop()
                    .into(holder.imageItem);
            return;
        }
        holder.btnMore.setVisibility(View.VISIBLE);


        GlideApp.with(holder.itemView)
                .load(notification.getImage_url())
                .error(R.drawable.ic_caution)
                .placeholder(R.drawable.ic_caution)
                .circleCrop()
                .into(holder.imageItem);

        holder.btnMore.setOnClickListener(view -> {
            BottomSheetDialog mDialog = new BottomSheetDialog(view.getContext());
            mDialog.setContentView(R.layout.item_holder_bottom_sheet_notification_event);
            mDialog.show();

            TextView tvName = mDialog.findViewById(R.id.tv_holder_bottom_sheet_notification_event_name);
            TextView tvDate = mDialog.findViewById(R.id.tv_holder_bottom_sheet_notification_event_date);
            TextView tvVenue = mDialog.findViewById(R.id.tv_holder_bottom_sheet_notification_event_venue);
            TextView tvExtra = mDialog.findViewById(R.id.tv_holder_bottom_sheet_notification_event_extra);
            Button btnOpen = mDialog.findViewById(R.id.button_holder_bottom_sheet_notification_event);

            tvName.setText(Html.fromHtml("Event: <b>" + notification.getName() + "</b>"));
            tvDate.setText(Html.fromHtml("Date: <b>" + ConverterUtils.convertDateToString(date) + "</b>"));
            tvVenue.setText(Html.fromHtml("Venue: <b>" + notification.getVenue() + "</b>"));
            tvExtra.setText(Html.fromHtml("About: <b>" + notification.getShort_info() + "</b>"));
            GlideApp.with(view.getContext())
                    .load(notification.getImage_url())
                    .error(R.drawable.ic_caution)
                    .placeholder(R.drawable.ic_caution)
                    .circleCrop()
                    .into((ImageView) mDialog.findViewById(R.id.image_holder_bottom_sheet_notification_event));

            btnOpen.setOnClickListener(view1 -> {
                Intent openLink = new Intent(Intent.ACTION_VIEW, Uri.parse(notification.getUrl()));
                if (openLink.resolveActivity(view1.getContext().getPackageManager()) != null) {
                    view1.getContext().startActivity(openLink);
                } else {
                    Toast.makeText(view1.getContext(), "Link seems to be broken!", Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            });

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
