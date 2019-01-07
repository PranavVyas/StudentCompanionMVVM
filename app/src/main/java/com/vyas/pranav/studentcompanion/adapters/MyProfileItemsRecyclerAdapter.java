package com.vyas.pranav.studentcompanion.adapters;

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
                .centerCrop()
                .into(holder.imageItem);
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
}
