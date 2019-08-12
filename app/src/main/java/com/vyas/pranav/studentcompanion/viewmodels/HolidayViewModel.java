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
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayEntry;
import com.vyas.pranav.studentcompanion.repositories.HolidayRepository;

import java.util.List;

public class HolidayViewModel extends AndroidViewModel {

    private final HolidayRepository holidayRepository;
    private final LiveData<List<HolidayEntry>> holidayEntries;

    public HolidayViewModel(@NonNull Application application) {
        super(application);
        holidayRepository = HolidayRepository.getInstance(application);
        holidayEntries = holidayRepository.getAllHolidays();
    }

    public LiveData<List<HolidayEntry>> getHolidayEntries() {
        return holidayEntries;
    }
}
