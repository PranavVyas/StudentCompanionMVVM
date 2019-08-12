package com.vyas.pranav.studentcompanion.adapters;
/*
Student Companion - An Android App that has features like attendance manager, note manager etc
Copyright (C) 2019  Pranav Vyas

This file is a part of Student Companion.

Student Companion is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Student Companion is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.
*/
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.notedatabase.NotesEntry;
import com.vyas.pranav.studentcompanion.ui.activities.AddNoteActivity;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteRecyclerAdapter extends PagedListAdapter<NotesEntry, NoteRecyclerAdapter.NoteHolderSingle> {

    public static String EXTRA_TYPE_EDIT_ADD_NOTE = "RecyclerView.EditNote.FromRv";
    public static String EXTRA_EDIT_NOTE = "RecyclerView.EditNote";
    public static boolean EDIT_NOTE = true;
    private static DiffUtil.ItemCallback<NotesEntry> diffCallback = new DiffUtil.ItemCallback<NotesEntry>() {
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

        holder.imageDelete.setOnClickListener(view -> {
            if (mCallback != null) {
                mCallback.OnNotesDeleteClicked(note);
            }
        });

        holder.imageEdit.setOnClickListener(view -> {
            openEditActivity(holder.itemView.getContext(), note);
        });


        holder.imageMore.setOnClickListener(view -> {
            BottomSheetDialog mDialog = new BottomSheetDialog(holder.itemView.getContext());
            mDialog.setContentView(R.layout.item_holder_bottom_sheet_note);
            mDialog.show();

            TextView tvTitle = mDialog.findViewById(R.id.tv_note_bottom_sheet_title);
            TextView tvDesc = mDialog.findViewById(R.id.tv_note_bottom_sheet_desc);
            TextView tvDate = mDialog.findViewById(R.id.tv_note_bottom_sheet_date);
            ImageView imageEdit = mDialog.findViewById(R.id.image_note_bottom_sheet_edit);
            TextView tvEdit = mDialog.findViewById(R.id.tv_note_bottom_sheet_edit_note);

            View.OnClickListener listener = view1 -> {
                openEditActivity(holder.itemView.getContext(), note);
                mDialog.dismiss();
            };

            tvDate.setText(ConverterUtils.convertDateToString(note.getDate()));
            tvTitle.setText("Title: " + note.getTitle());
            tvDesc.setText(note.getDesc());
            imageEdit.setOnClickListener(listener);
            tvEdit.setOnClickListener(listener);
        });
    }

    private void openEditActivity(Context context, NotesEntry noteToOpen) {
        Intent intent = new Intent(context, AddNoteActivity.class);
        intent.putExtra(EXTRA_TYPE_EDIT_ADD_NOTE, EDIT_NOTE);
        intent.putExtra(EXTRA_EDIT_NOTE, new Gson().toJson(noteToOpen));
        context.startActivity(intent);
    }
    public void setOnNotesDeleteClickedListener(OnNotesDeleteClickedListener callback) {
        mCallback = callback;
    }

    public interface OnNotesDeleteClickedListener {
        void OnNotesDeleteClicked(NotesEntry note);
    }

    public class NoteHolderSingle extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recycler_holder_note_title)
        TextView tvTitle;
        @BindView(R.id.tv_recycler_holder_note_desc)
        TextView tvDesc;
        @BindView(R.id.tv_recycler_holder_note_date)
        TextView tvDate;
        @BindView(R.id.btn_recycler_holder_more)
        ImageView imageMore;
        @BindView(R.id.image_recycler_holder_note_edit)
        ImageView imageDelete;
        @BindView(R.id.image_note_holder_edit_parent)
        ImageView imageEdit;

        public NoteHolderSingle(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
