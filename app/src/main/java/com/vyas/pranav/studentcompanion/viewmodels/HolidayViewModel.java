package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayEntry;
import com.vyas.pranav.studentcompanion.repositories.HolidayRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class HolidayViewModel extends AndroidViewModel {

    private HolidayRepository holidayRepository;
    private LiveData<List<HolidayEntry>> holidayEntries;

    public HolidayViewModel(@NonNull Application application) {
        super(application);
        holidayRepository = new HolidayRepository(application);
        holidayEntries = holidayRepository.getAllHolidays();
    }

    public LiveData<List<HolidayEntry>> getHolidayEntries() {
        return holidayEntries;
    }
}
