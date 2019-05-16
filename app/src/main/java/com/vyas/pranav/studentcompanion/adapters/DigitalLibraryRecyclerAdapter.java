package com.vyas.pranav.studentcompanion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.digitallibrarydatabase.DigitalLibraryEntry;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DigitalLibraryRecyclerAdapter extends RecyclerView.Adapter<DigitalLibraryRecyclerAdapter.BookHolder> {

    private List<DigitalLibraryEntry> listOfBooks;

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_digital_library_book_holder, parent, false);
        return new BookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        DigitalLibraryEntry book = listOfBooks.get(position);
        holder.tvAuthorName.setText(book.getAuthorName());
        holder.tvBookName.setText(book.getBookName());
        holder.tvSubject.setText(book.getSubject());
        holder.tvNo.setText((position + 1) + ".");
        holder.tvExtraInfo.setText(book.getExtraInfo());
    }

    @Override
    public int getItemCount() {
        return (listOfBooks == null) ? 0 : listOfBooks.size();
    }

    public void refreshList(List<DigitalLibraryEntry> listOfBooks) {
        this.listOfBooks = listOfBooks;
        notifyDataSetChanged();
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
            Toast.makeText(v.getContext(), "Link is : " + listOfBooks.get(getAdapterPosition()).getBookUrl(), Toast.LENGTH_SHORT).show();
        }
    }
}
