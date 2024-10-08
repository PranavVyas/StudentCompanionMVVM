package com.vyas.pranav.studentcompanion.adapters;
/*
Student Companion - An Android App that has features like attendance manager, note manager etc
Copyright (C) 2019  Pranav Vyas

This file is a part of Student Companion.

Student Companion is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Student Companion is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.
*/
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.models.ItemModel;
import com.vyas.pranav.studentcompanion.utils.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarketPlaceSellRecyclerAdapter extends ListAdapter<ItemModel, MarketPlaceSellRecyclerAdapter.SellItemHolder> {

    private static final DiffUtil.ItemCallback<ItemModel> diffCallback = new DiffUtil.ItemCallback<ItemModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull ItemModel oldItem, @NonNull ItemModel newItem) {
            return oldItem.getImage_uri().equals(newItem.getImage_uri());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ItemModel oldItem, @NonNull ItemModel newItem) {
            return (oldItem.getCategory().equals(newItem.getCategory())) &&
                    (oldItem.getContact().equals(newItem.getContact())) &&
                    (oldItem.getExtra_info().equals(newItem.getExtra_info())) &&
                    (oldItem.getName().equals(newItem.getName())) &&
                    (oldItem.getP_name().equals(newItem.getP_name())) &&
                    (oldItem.getPrice() == newItem.getPrice());
        }
    };

    public MarketPlaceSellRecyclerAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public SellItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_marketplace_item, parent, false);
        return new SellItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellItemHolder holder, int position) {
        ItemModel item = getItem(position);
        String name = item.getName();
        String pName = item.getP_name();
        Float price = item.getPrice();
        Uri imageUri = Uri.parse(item.getImage_uri());
        if (name == null) {
            name = "No Name";
        }
        if (pName == null) {
            pName = "Person Name";
        }
        holder.tvName.setText(name);
        holder.tvPersonName.setText(pName);
        holder.tvPrice.setText("Rs. " + price + " /-");
        GlideApp.with(holder.itemView.getContext())
                .load(imageUri)
                .placeholder(R.drawable.ic_market_place)
                .error(R.drawable.ic_market_place)
                .circleCrop()
                .into(holder.image);
        holder.itemView.setOnClickListener(v -> showBottomSheet(v.getContext(), item));
    }

    private void showBottomSheet(Context context, ItemModel item) {
        BottomSheetDialog mDialog = new BottomSheetDialog(context);
        mDialog.setContentView(R.layout.item_holder_alert_dialog_marketplace_sell);
        mDialog.show();

        TextView tvDialogName = mDialog.findViewById(R.id.tv_marketplace_sell_item_name);
        TextView tvDialogPrice = mDialog.findViewById(R.id.tv_marketplace_sell_item_price);
        TextView tvDialogInfo = mDialog.findViewById(R.id.tv_marketplace_sell_item_info);
        TextView tvDialogContact = mDialog.findViewById(R.id.tv_marketplace_sell_item_contact);
        TextView tvDialogCategory = mDialog.findViewById(R.id.tv_marketplace_sell_item_category);
        ImageView imageItem = mDialog.findViewById(R.id.image_marketplace_sell_item);
        ImageView imageShare = mDialog.findViewById(R.id.image_marketplace_sell_item_share);

        tvDialogCategory.setText(item.getCategory());
        tvDialogName.setText(item.getName());
        tvDialogInfo.setText(item.getExtra_info());
        tvDialogPrice.setText(item.getPrice() + "/- Rs.");
        tvDialogContact.setText(item.getP_name() + "\n No : " + item.getContact());
        GlideApp.with(context)
                .load(item.getImage_uri())
                .placeholder(R.drawable.ic_market_place)
                .error(R.drawable.ic_market_place)
                .circleCrop()
                .into(imageItem);
        imageShare.setOnClickListener((view -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey there is new Item in Student Companion, Check it out :\nName: *" + item.getName() + "*\nSeller : *" + item.getP_name() + "*\n\nCheck out Price in *Student Companion*" + context.getString(R.string.share_message_digital_library) + context.getString(R.string.feature_url) + "\n\n Download now: " + context.getString(R.string.download_url));
            shareIntent.setType("text/plain");
            context.startActivity(shareIntent);
        }));

        MaterialButton btnSold = mDialog.findViewById(R.id.btn_marketplace_sell_item_action);
        if (btnSold != null) {
            btnSold.setText("Call Now!");
            btnSold.setIcon(context.getResources().getDrawable(R.drawable.ic_phone));
            btnSold.setOnClickListener(v -> {
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:" + item.getContact()));
                v.getContext().startActivity(call);
                mDialog.dismiss();
            });
        } else {
            Toast.makeText(context, "Button is empty", Toast.LENGTH_SHORT).show();
        }
    }

    class SellItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_recycler_marketplace_item_photo)
        ImageView image;
        @BindView(R.id.tv_recycler_marketplace_item_name)
        TextView tvName;
        @BindView(R.id.tv_recycler_marketplace_item_price)
        TextView tvPrice;
        @BindView(R.id.tv_recycler_marketplace_item_p_name)
        TextView tvPersonName;

        SellItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
