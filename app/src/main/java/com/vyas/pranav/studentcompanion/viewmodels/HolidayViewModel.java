package com.vyas.pranav.studentcompanion.viewmodels;

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
