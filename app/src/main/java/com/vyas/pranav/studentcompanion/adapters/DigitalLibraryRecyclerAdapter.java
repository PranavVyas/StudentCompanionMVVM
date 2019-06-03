package com.vyas.pranav.studentcompanion.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
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

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForPermission(holder.itemView.getContext(), book.getBookUrl());
            }
        };

        holder.itemView.setOnClickListener(clickListener);
    }

    private void checkForPermission(Context context, String downloadUrl) {
        Dexter.withActivity((Activity) context)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(context, "Thank you for permission", Toast.LENGTH_SHORT).show();
                            startDownloadDocument(downloadUrl);
                        } else {
                            String deniedPermissioString = "";
                            List<PermissionDeniedResponse> deniedPermissionResponses = report.getDeniedPermissionResponses();
                            for (PermissionDeniedResponse res :
                                    deniedPermissionResponses) {
                                deniedPermissioString += res.getPermissionName();
                            }
                            Toast.makeText(context, "Permissions " + deniedPermissioString + " are required!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void startDownloadDocument(String downloadUrl) {

    }

    @Override
    public int getItemCount() {
        return (listOfBooks == null) ? 0 : listOfBooks.size();
    }

    public void refreshList(List<DigitalLibraryEntry> listOfBooks) {
        this.listOfBooks = listOfBooks;
        notifyDataSetChanged();
    }

    class BookHolder extends RecyclerView.ViewHolder {
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
        }
    }
}
