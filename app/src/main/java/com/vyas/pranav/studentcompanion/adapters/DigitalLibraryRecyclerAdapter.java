package com.vyas.pranav.studentcompanion.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DigitalLibraryRecyclerAdapter extends ListAdapter<DigitalLibraryEntry, DigitalLibraryRecyclerAdapter.BookHolder> {

    private static final DiffUtil.ItemCallback<DigitalLibraryEntry> diffCallback = new DiffUtil.ItemCallback<DigitalLibraryEntry>() {
        @Override
        public boolean areItemsTheSame(@NonNull DigitalLibraryEntry oldItem, @NonNull DigitalLibraryEntry newItem) {
            return oldItem.get_ID() == newItem.get_ID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull DigitalLibraryEntry oldItem, @NonNull DigitalLibraryEntry newItem) {
            return oldItem.getBookUrl().equals(newItem.getBookUrl()) &&
                    oldItem.getAuthorName().equals(newItem.getAuthorName()) &&
                    oldItem.getBookName().equals(newItem.getBookName()) &&
                    oldItem.getSubject().equals(newItem.getSubject()) &&
                    oldItem.getExtraInfo().equals(newItem.getExtraInfo());
        }
    };

    public DigitalLibraryRecyclerAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_digital_library_book_holder, parent, false);
        return new BookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        DigitalLibraryEntry book = getItem(position);
        holder.tvAuthorName.setText("By: " + book.getAuthorName());
        holder.tvBookName.setText(book.getBookName());
        holder.tvSubject.setText(book.getSubject());
        holder.tvNo.setText((position + 1) + ".");
        holder.tvExtraInfo.setText(book.getExtraInfo());
    }

    class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_recycler_digital_library_book_holder_book_name)
        TextView tvBookName;
        @BindView(R.id.tv_recycler_digital_library_book_holder_author_name)
        TextView tvAuthorName;
        @BindView(R.id.tv_recycler_digital_library_book_holder_subject)
        TextView tvSubject;
        @BindView(R.id.tv_recycler_digital_library_book_holder_no)
        TextView tvNo;
        @BindView(R.id.tv_recycler_digital_library_book_holder_extra_info)
        TextView tvExtraInfo;

        BookHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            BottomSheetDialog mDialog = new BottomSheetDialog(v.getContext());
            mDialog.setContentView(R.layout.item_holder_sheet_open_link_digital);
            mDialog.show();

            TextView tvName = mDialog.findViewById(R.id.tv_holder_botttom_sheet_digital_name);
            TextView tvExtra = mDialog.findViewById(R.id.tv_holder_botttom_sheet_digital_extra);
            TextView tvAuthor = mDialog.findViewById(R.id.tv_holder_botttom_sheet_digital_author);
            TextView tvSubjectDialog = mDialog.findViewById(R.id.tv_holder_botttom_sheet_digital_subject);
            Button btnOk = mDialog.findViewById(R.id.btn_holder_bottom_sheet_digital_open_in_browser);

            DigitalLibraryEntry book = getItem(getAdapterPosition());
            tvAuthor.setText(book.getAuthorName());
            tvName.setText(book.getBookName());
            tvSubjectDialog.setText(book.getSubject());
            tvExtra.setText(book.getExtraInfo());

            btnOk.setOnClickListener(view -> {
                Intent openLink = new Intent(Intent.ACTION_VIEW, Uri.parse(getItem(getAdapterPosition()).getBookUrl()));
                v.getContext().startActivity(openLink);
                mDialog.dismiss();
            });
        }
    }
}
