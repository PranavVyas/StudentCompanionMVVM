package com.vyas.pranav.studentcompanion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryEntry;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
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
        holder.tvAuthorName.setText(book.getAuthorName());
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
            Toast.makeText(v.getContext(), "Link is : " + getItem(getAdapterPosition()).getBookUrl(), Toast.LENGTH_SHORT).show();
        }
    }
}
