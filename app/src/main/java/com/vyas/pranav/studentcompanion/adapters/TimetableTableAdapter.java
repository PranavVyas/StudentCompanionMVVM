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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.vyas.pranav.studentcompanion.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimetableTableAdapter extends AbstractTableAdapter<String, String, String> {

    public TimetableTableAdapter(Context context) {
        super(context);
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int position) {
        return 0;
    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        return new CellViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_holder_table_timetable_cell, parent, false));
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int columnPosition, int rowPosition) {
        CellViewHolder viewHolder = (CellViewHolder) holder;
        String cell = (String) cellItemModel;
        viewHolder.tvCell.setText(cell);
        viewHolder.itemView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.tvCell.requestLayout();
    }

    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        return new ColumnHeaderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_holder_table_timetable_column_header, parent, false));
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int columnPosition) {
        ColumnHeaderViewHolder viewHolder = (ColumnHeaderViewHolder) holder;
        String columnHeader = (String) columnHeaderItemModel;
        viewHolder.tvColumnHeader.setText(columnHeader);
        viewHolder.itemView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.tvColumnHeader.requestLayout();
    }

    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        return new RowHeaderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_holder_table_timetable_row_header, parent, false));
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {
        RowHeaderViewHolder viewHolder = (RowHeaderViewHolder) holder;
        String rowHeader = (String) rowHeaderItemModel;
        viewHolder.tvRowHeader.setText(rowHeader);
        //viewHolder.itemView.setBackgroundColor(R.attr.table_background);
        //viewHolder.itemView.setBackgroundResource(R.color.accent);
        viewHolder.itemView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        viewHolder.tvRowHeader.requestLayout();
    }

    @Override
    public View onCreateCornerView() {
        return LayoutInflater.from(mContext).inflate(R.layout.item_holder_table_timetable_corner, null);
    }

    class RowHeaderViewHolder extends AbstractViewHolder {

        @BindView(R.id.tv_table_timetable_row_header)
        TextView tvRowHeader;

        public RowHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ColumnHeaderViewHolder extends AbstractViewHolder {

        @BindView(R.id.tv_table_timetable_column_header)
        TextView tvColumnHeader;

        public ColumnHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CellViewHolder extends AbstractViewHolder {

        @BindView(R.id.tv_table_timetable_cell)
        TextView tvCell;

        public CellViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
