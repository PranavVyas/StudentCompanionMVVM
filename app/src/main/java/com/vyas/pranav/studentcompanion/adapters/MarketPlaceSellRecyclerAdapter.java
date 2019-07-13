package com.vyas.pranav.studentcompanion.adapters;

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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.itemdatabase.firebase.ItemModel;
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
        holder.tvPrice.setText("Rs. " + price + " \\-");
        GlideApp.with(holder.itemView.getContext())
                .load(imageUri)
                .placeholder(R.drawable.ic_market_place)
                .error(R.drawable.ic_market_place)
                .circleCrop()
                .into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(v.getContext(), item);
            }
        });
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

    private void showAlertDialog(Context context, ItemModel item) {
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
                .placeholder(R.drawable.ic_market_place)
                .error(R.drawable.ic_market_place)
                .circleCrop()
                .into(imageItem);
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(context)
                .setView(alertView)
                .show();
        MaterialButton btnSold = alertDialog.findViewById(R.id.btn_marketplace_sell_item_action);
        if (btnSold != null) {
            btnSold.setText("Call Now!");
            btnSold.setIcon(context.getResources().getDrawable(R.drawable.ic_phone));
            btnSold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent call = new Intent(Intent.ACTION_DIAL);
                    call.setData(Uri.parse("tel:" + item.getContact()));
                    v.getContext().startActivity(call);
                    alertDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(context, "Button is empty", Toast.LENGTH_SHORT).show();
        }
    }

}
