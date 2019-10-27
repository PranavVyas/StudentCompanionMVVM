package com.vyas.pranav.studentcompanion.adapters;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.models.DownloadModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadRecyclerAdapter extends ListAdapter<DownloadModel, DownloadRecyclerAdapter.DownloadHolder> {

    public static final DiffUtil.ItemCallback<DownloadModel> diffCallback = new DiffUtil.ItemCallback<DownloadModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull DownloadModel oldItem, @NonNull DownloadModel newItem) {
            return oldItem.getUrl().equals(newItem.getUrl());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DownloadModel oldItem, @NonNull DownloadModel newItem) {
            return oldItem.getName().equals(newItem.getName()) && oldItem.getExtra_info().equals(newItem.getExtra_info());
        }
    };
    private OnDownloadSelectedListener mCallback;

    public DownloadRecyclerAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public DownloadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DownloadHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_download_db, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadHolder holder, int position) {
        bindUi(holder, getItem(position));
    }

    private void bindUi(DownloadHolder holder, DownloadModel item) {
        holder.tvName.setText(item.getName());
        holder.tvExtraInfo.setText(Html.fromHtml(item.getExtra_info()));
        holder.imageDownload.setOnClickListener((view -> {
            //TODO Send back Download Link to Import/Export Activity
            if (mCallback != null) {
                mCallback.onDownloadSelected(item);
            }
        }));
        holder.tvExtraInfo.setOnClickListener((view -> {
            new MaterialAlertDialogBuilder(holder.itemView.getContext())
                    .setTitle(item.getName())
                    .setMessage(item.getExtra_info().replace("<br>", "\n").replace("<b>", "").replace("<i>", "").replace("</b>", "").replace("</i>", "")).create().show();
        }));
    }

    public void setOnDownloadSelectedListener(OnDownloadSelectedListener callback) {
        this.mCallback = callback;
    }

    public interface OnDownloadSelectedListener {
        void onDownloadSelected(DownloadModel downloadModel);
    }

    class DownloadHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_holder_download_extra_info)
        TextView tvExtraInfo;
        @BindView(R.id.tv_holder_download_name)
        TextView tvName;
        @BindView(R.id.image_holder_download_download)
        ImageView imageDownload;

        public DownloadHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
