package com.vyas.pranav.studentcompanion.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class QuickAnalysisOverallViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String subName;
    private Context context;

    public QuickAnalysisOverallViewModelFactory(String subName, Context context) {
        this.subName = subName;
        this.context = context;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new QuickAnalysisOverallViewModel(subName, context);
    }
}
