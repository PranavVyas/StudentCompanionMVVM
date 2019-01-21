package com.vyas.pranav.studentcompanion.viewmodels;

import android.content.Context;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NotificationsForDateViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Date date;
    private Context context;

    public NotificationsForDateViewModelFactory(Date date, Context context) {
        this.date = date;
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new NotificationsViewModelForDate(date, context);
    }
}
