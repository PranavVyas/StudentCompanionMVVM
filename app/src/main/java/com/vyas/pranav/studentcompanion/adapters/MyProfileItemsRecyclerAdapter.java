package com.vyas.pranav.studentcompanion.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.bookdatabase.firebase.ItemModel;
import com.vyas.pranav.studentcompanion.utils.GlideApp;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProfileItemsRecyclerAdapter extends RecyclerView.Adapter<MyProfileItemsRecyclerAdapter.MyProfileItemHolder> {

    private List<ItemModel> items;
    private OnItemSoldButtonClickListener listener;

    @NonNull
    @Override
    public MyProfileItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyProfileItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_my_profile_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyProfileItemHolder holder, int position) {
        ItemModel item = items.get(position);
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(item.getPrice() + " /- Rs");
        GlideApp.with(holder.itemView)
                .load(item.getImage_uri())
                .circleCrop()
                .into(holder.imageItem);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(item, v.getContext());
            }
        };
        holder.tvPrice.setOnClickListener(clickListener);
        holder.tvName.setOnClickListener(clickListener);
        holder.imageItem.setOnClickListener(clickListener);
        if (listener != null) {
            holder.btnSold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemSoldClicked(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setOnItemSoldButtonClickListener(OnItemSoldButtonClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemSoldButtonClickListener {
        void onItemSoldClicked(ItemModel item);
    }

    class MyProfileItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recycler_profile_item_price)
        TextView tvPrice;
        @BindView(R.id.tv_recycler_profile_item_name)
        TextView tvName;
        @BindView(R.id.image_recycler_profile_item_photo)
        ImageView imageItem;
        @BindView(R.id.btn_recycler_profile_item_sold)
        Button btnSold;

        public MyProfileItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void showAlertDialog(ItemModel item, Context context) {
        View alertView = LayoutInflater.from(context).inflate(R.layout.item_holder_alert_dialog_marketplace_sell, null);
        TextView tvDialogName = alertView.findViewById(R.id.tv_marketplace_sell_item_name);
        TextView tvDialogPrice = alertView.findViewById(R.id.tv_marketplace_sell_item_price);
        TextView tvDialogInfo = alertView.findViewById(R.id.tv_marketplace_sell_item_info);
        TextView tvDialogContact = alertView.findViewById(R.id.tv_marketplace_sell_item_contact);
        TextView tvDialogCategory = alertView.findViewById(R.id.tv_marketplace_sell_item_category);
        ImageView imageItem = alertView.findViewById(R.id.image_marketplace_sell_item);

        tvDialogCategory.setText(item.getCategory());
        tvDialogName.setText(item.getName());
        tvDialogInfo.setText(item.getExtra_info());
        tvDialogPrice.setText(item.getPrice() + "//- Rs.");
        tvDialogContact.setText(item.getP_name() + "\n No : " + item.getContact());
        GlideApp.with(context)
                .load(item.getImage_uri())
                .circleCrop()
                .into(imageItem);

        new AlertDialog.Builder(context)
                .setView(alertView)
                .setPositiveButton("Call Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent call = new Intent(Intent.ACTION_DIAL);
                        call.setData(Uri.parse("tel:" + item.getContact()));
                        context.startActivity(call);
                    }
                })
                .show();
    }
}
