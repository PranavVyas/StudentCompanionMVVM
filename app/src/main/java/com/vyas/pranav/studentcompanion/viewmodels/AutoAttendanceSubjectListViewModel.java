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

import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;

import java.util.List;

public class AutoAttendanceSubjectListViewModel extends AndroidViewModel {

    private final SetUpProcessRepository setUpProcessRepository;

    public AutoAttendanceSubjectListViewModel(@NonNull Application application) {
        super(application);
        setUpProcessRepository = new SetUpProcessRepository(application);
    }

    public List<String> getSubjectList() {
        return setUpProcessRepository.getSubjectList();
    }
}
