package com.vyas.pranav.studentcompanion.viewmodels;

import android.app.Application;

import com.vyas.pranav.studentcompanion.repositories.SetUpProcessRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class TutorialViewModel extends AndroidViewModel {

    private SetUpProcessRepository setUpProcessRepository;

    public TutorialViewModel(@NonNull Application application) {
        super(application);
        setUpProcessRepository = new SetUpProcessRepository(application);
    }

    public void setTutorialComplete(boolean isTutorialComplete) {
        setUpProcessRepository.setTutorialDone(isTutorialComplete);
    }
}
