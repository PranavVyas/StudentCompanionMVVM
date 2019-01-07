package com.vyas.pranav.studentcompanion.adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MarketPlaceSellRecyclerAdapter extends RecyclerView.Adapter<MarketPlaceSellRecyclerAdapter.SellItemHolder> {

    private List<ItemModel> items;

    @NonNull
    @Override
    public SellItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_marketplace_item, parent, false);
        return new SellItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellItemHolder holder, int position) {
        ItemModel item = items.get(position);
        String name = item.getName();
        String pName = item.getP_name();
        Float price = item.getPrice();
        Uri imageUri = Uri.parse(items.get(position).getImage_uri());
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
                .circleCrop()
                .into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View alertView = LayoutInflater.from(v.getContext()).inflate(R.layout.item_holder_alert_dialog_marketplace_sell, null);
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
                GlideApp.with(v)
                        .load(item.getImage_uri())
                        .circleCrop()
                        .into(imageItem);

                new AlertDialog.Builder(v.getContext())
                        .setView(alertView)
                        .setPositiveButton("Call Now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent call = new Intent(Intent.ACTION_DIAL);
                                call.setData(Uri.parse("tel:" + item.getContact()));
                                v.getContext().startActivity(call);
                                //TODO implement call feature here
                            }
                        })
                        .show();
            }
        });
        //TODO Set Place holder
    }

    @Override
    public int getItemCount() {
        return (items == null) ? 0 : items.size();
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
        notifyDataSetChanged();
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

        public SellItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
