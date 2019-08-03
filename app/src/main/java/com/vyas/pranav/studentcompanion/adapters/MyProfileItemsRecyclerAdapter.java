package com.vyas.pranav.studentcompanion.adapters;

import android.content.Context;
import android.util.Pair;
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
import com.vyas.pranav.studentcompanion.data.itemdatabase.firebase.ItemModel;
import com.vyas.pranav.studentcompanion.utils.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProfileItemsRecyclerAdapter extends ListAdapter<Pair, MyProfileItemsRecyclerAdapter.MyProfileItemHolder> {

    private static final DiffUtil.ItemCallback<Pair> diffCallback = new DiffUtil.ItemCallback<Pair>() {
        @Override
        public boolean areItemsTheSame(@NonNull Pair oldItem, @NonNull Pair newItem) {
            return oldItem.first.toString().equals(newItem.first.toString());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Pair oldItem, @NonNull Pair newItem) {
            ItemModel oldItemModel = (ItemModel) oldItem.second;
            ItemModel newItemModel = (ItemModel) newItem.second;

            return (oldItemModel.getCategory().equals(newItemModel.getCategory())) &&
                    (oldItemModel.getContact().equals(newItemModel.getContact())) &&
                    (oldItemModel.getExtra_info().equals(newItemModel.getExtra_info())) &&
                    (oldItemModel.getName().equals(newItemModel.getName())) &&
                    (oldItemModel.getImage_uri()).equals(newItemModel.getImage_uri()) &&
                    (oldItemModel.getP_name().equals(newItemModel.getP_name())) &&
                    (oldItemModel.getPrice() == newItemModel.getPrice());
        }
    };
    private OnItemSoldButtonClickListener listener;

    public MyProfileItemsRecyclerAdapter() {
        super(diffCallback);
    }
//    private static final DiffUtil.ItemCallback<ItemModel> diffCallback = new DiffUtil.ItemCallback<ItemModel>() {
//        @Override
//        public boolean areItemsTheSame(@NonNull ItemModel oldItem, @NonNull ItemModel newItem) {
//            return oldItem.getImage_uri().equals(newItem.getImage_uri());
//        }
//
//        @Override
//        public boolean areContentsTheSame(@NonNull ItemModel oldItem, @NonNull ItemModel newItem) {
//            return (oldItem.getCategory().equals(newItem.getCategory())) &&
//                    (oldItem.getContact().equals(newItem.getContact())) &&
//                    (oldItem.getExtra_info().equals(newItem.getExtra_info())) &&
//                    (oldItem.getName().equals(newItem.getName())) &&
//                    (oldItem.getP_name().equals(newItem.getP_name())) &&
//                    (oldItem.getPrice() == newItem.getPrice());
//        }
//    };
//
//    public MyProfileItemsRecyclerAdapter() {
//        super(diffCallback);
//    }

    @NonNull
    @Override
    public MyProfileItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyProfileItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_my_profile_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyProfileItemHolder holder, int position) {
        ItemModel item = (ItemModel) getItem(position).second;
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(item.getPrice() + " /- Rs");
        String idOfItem = getItem(position).first.toString();
        GlideApp.with(holder.itemView)
                .load(item.getImage_uri())
                .placeholder(R.drawable.ic_market_place)
                .error(R.drawable.ic_market_place)
                .circleCrop()
                .into(holder.imageItem);
        View.OnClickListener clickListener = v -> showBottomSheet(item, idOfItem, v.getContext());
        holder.tvPrice.setOnClickListener(clickListener);
        holder.tvName.setOnClickListener(clickListener);
        holder.imageItem.setOnClickListener(clickListener);
        if (listener != null) {
            holder.btnSold.setOnClickListener(v -> listener.onItemSoldClicked(idOfItem));
        }
    }

    public void setOnItemSoldButtonClickListener(OnItemSoldButtonClickListener listener) {
        this.listener = listener;
    }

    private void showBottomSheet(ItemModel item, String id, Context context) {
        BottomSheetDialog mDialog = new BottomSheetDialog(context);
        mDialog.setContentView(R.layout.item_holder_alert_dialog_marketplace_sell);
        mDialog.show();

        TextView tvDialogName = mDialog.findViewById(R.id.tv_marketplace_sell_item_name);
        TextView tvDialogPrice = mDialog.findViewById(R.id.tv_marketplace_sell_item_price);
        TextView tvDialogInfo = mDialog.findViewById(R.id.tv_marketplace_sell_item_info);
        TextView tvDialogContact = mDialog.findViewById(R.id.tv_marketplace_sell_item_contact);
        TextView tvDialogCategory = mDialog.findViewById(R.id.tv_marketplace_sell_item_category);
        ImageView imageItem = mDialog.findViewById(R.id.image_marketplace_sell_item);

        tvDialogCategory.setText(item.getCategory());
        tvDialogName.setText(item.getName());
        tvDialogInfo.setText(item.getExtra_info());
        tvDialogPrice.setText(item.getPrice() + "//- Rs.");
        tvDialogContact.setText(item.getP_name() + "\n No : " + item.getContact());
        GlideApp.with(context)
                .load(item.getImage_uri())
                .circleCrop()
                .into(imageItem);


        Button btnSold = mDialog.findViewById(R.id.btn_marketplace_sell_item_action);
        if (btnSold != null) {
            btnSold.setText("Sold It!");
            btnSold.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemSoldClicked(id);
                    mDialog.dismiss();
                } else {
                    throw new NullPointerException("Listener is not Attached properly");
                }
            });
        } else {
            Toast.makeText(context, "Button is empty", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnItemSoldButtonClickListener {
        void onItemSoldClicked(String id);
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

        MyProfileItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
