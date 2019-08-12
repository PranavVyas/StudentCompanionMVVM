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
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.models.ExternalLibraryModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpenSourceRecyclerAdapter extends RecyclerView.Adapter<OpenSourceRecyclerAdapter.LibraryHolder> {

    private List<ExternalLibraryModel> externalLibraryModels;

    @NonNull
    @Override
    public LibraryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Logger.addLogAdapter(new AndroidLogAdapter());
        return new LibraryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_recycler_open_source, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryHolder holder, int position) {
//        Logger.d("Position is : "+position);
        ExternalLibraryModel libraryModel = externalLibraryModels.get(position);
        holder.libInfo.setText(libraryModel.getLicense());
        holder.libName.setText(libraryModel.getName());
//        Logger.json(new Gson().toJson(libraryModel));
        holder.libLink.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(libraryModel.getUrl()));
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (externalLibraryModels == null) ? 0 : externalLibraryModels.size();
    }

    public void setExternalLibraryModels(List<ExternalLibraryModel> externalLibraryModels) {
        this.externalLibraryModels = externalLibraryModels;
        notifyDataSetChanged();
    }

    class LibraryHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recycler_open_source_lib_name)
        TextView libName;
        @BindView(R.id.tv_recycler_open_source_lib_info)
        TextView libInfo;
        @BindView(R.id.tv_recycler_open_source_lib_link)
        MaterialButton libLink;

        LibraryHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
