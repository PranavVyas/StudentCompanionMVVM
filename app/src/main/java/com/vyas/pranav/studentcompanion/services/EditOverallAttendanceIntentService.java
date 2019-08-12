package com.vyas.pranav.studentcompanion.services;
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
import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class EditOverallAttendanceIntentService extends IntentService {

    public static final String SERVICE = "EDIT_OVERALL_ATTENDANCE";
    private static final int RC_SHOW_NOTIFICATION = 1000;
    private static final int RC_OPEN_APP = 2000;

    public EditOverallAttendanceIntentService() {
        super(SERVICE);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Application application = getApplication();

    }


}
