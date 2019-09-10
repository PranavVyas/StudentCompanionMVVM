package com.vyas.pranav.studentcompanion.viewmodels;
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

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vyas.pranav.studentcompanion.data.notificationdatabase.NotificationEntry;
import com.vyas.pranav.studentcompanion.repositories.NotificationRepository;

import java.util.Date;
import java.util.List;

public class NotificationsViewModelForDate extends ViewModel {

    private Date date;
    private final Context context;
    private final NotificationRepository notificationRepository;
    private LiveData<List<NotificationEntry>> notificationFromDate;

    public NotificationsViewModelForDate(Date date, Context context) {
        this.date = date;
        this.context = context;
        notificationRepository = new NotificationRepository(context);
    }
}
