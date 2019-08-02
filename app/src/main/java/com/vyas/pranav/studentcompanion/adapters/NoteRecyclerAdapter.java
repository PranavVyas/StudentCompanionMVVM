package com.vyas.pranav.studentcompanion.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.models.NotesEntry;
import com.vyas.pranav.studentcompanion.ui.activities.AddNoteActivity;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteRecyclerAdapter extends PagedListAdapter<NotesEntry, NoteRecyclerAdapter.NoteHolderSingle> {

    public static String EXTRA_TYPE_EDIT_ADD_NOTE = "RecyclerView.EditNote.FromRv";
    public static String EXTRA_EDIT_NOTE = "RecyclerView.EditNote";
    public static boolean EDIT_NOTE = true;
    static DiffUtil.ItemCallback<NotesEntry> diffCallback = new DiffUtil.ItemCallback<NotesEntry>() {
        @Override
        public boolean areItemsTheSame(@NonNull NotesEntry oldItem, @NonNull NotesEntry newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull NotesEntry oldItem, @NonNull NotesEntry newItem) {
            return (oldItem.getTitle().equals(newItem.getTitle())) &&
                    (oldItem.getDate().equals(newItem.getDate())) &&
                    (oldItem.getDesc().equals(newItem.getDesc()));
        }
    };
    private OnNotesDeleteClickedListener mCallback;

    public NoteRecyclerAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public NoteHolderSingle onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteHolderSingle(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolderSingle holder, int position) {
        NotesEntry note = getItem(position);
        holder.tvDate.setText(ConverterUtils.convertDateToString(note.getDate()));
        holder.tvDesc.setText(note.getDesc());
        holder.tvTitle.setText(note.getTitle());

        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.OnNotesDeleteClicked(note);
                }
            }
        });

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog mDialog = new BottomSheetDialog(holder.itemView.getContext());
                mDialog.setContentView(R.layout.item_holder_bottom_sheet_note);
                mDialog.show();

                TextView tvTitle = mDialog.findViewById(R.id.tv_note_bottom_sheet_title);
                TextView tvDesc = mDialog.findViewById(R.id.tv_note_bottom_sheet_desc);
                TextView tvDate = mDialog.findViewById(R.id.tv_note_bottom_sheet_date);
                ImageView imageEdit = mDialog.findViewById(R.id.image_note_bottom_sheet_edit);
                TextView tvEdit = mDialog.findViewById(R.id.tv_note_bottom_sheet_edit_note);

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(holder.itemView.getContext(), AddNoteActivity.class);
                        intent.putExtra(EXTRA_TYPE_EDIT_ADD_NOTE, EDIT_NOTE);
                        intent.putExtra(EXTRA_EDIT_NOTE, new Gson().toJson(note));
                        holder.itemView.getContext().startActivity(intent);
                        mDialog.dismiss();
                    }
                };

                tvDate.setText(ConverterUtils.convertDateToString(note.getDate()));
                tvTitle.setText(note.getTitle());
                tvDesc.setText(note.getDesc());
                imageEdit.setOnClickListener(listener);
                tvEdit.setOnClickListener(listener);
            }
        });
    }

    public void setOnNotesDeleteClickedListener(OnNotesDeleteClickedListener callback) {
        mCallback = callback;
    }

    public interface OnNotesDeleteClickedListener {
        void OnNotesDeleteClicked(NotesEntry note);
    }

    class NoteHolderSingle extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recycler_holder_note_title)
        TextView tvTitle;
        @BindView(R.id.tv_recycler_holder_note_desc)
        TextView tvDesc;
        @BindView(R.id.tv_recycler_holder_note_date)
        TextView tvDate;
        @BindView(R.id.btn_recycler_holder_more)
        Button btnMore;
        @BindView(R.id.image_recycler_holder_note_delete)
        ImageView imageDelete;

        public NoteHolderSingle(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
