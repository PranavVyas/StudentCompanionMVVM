package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;

public class TutorialViewModel extends AndroidViewModel {

    private SetUpProcessRepository setUpProcessRepository;

    public TutorialViewModel(@NonNull Application application) {
        super(application);
        setUpProcessRepository = SetUpProcessRepository.getInstance(application);
    }

    public void setTutorialComplete(boolean isTutorialComplete) {
        setUpProcessRepository.setTutorialDone(isTutorialComplete);
    }
}
