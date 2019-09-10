package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import com.vyas.pranav.studentcompanion.data.lecturedatabase.LectureDao;
import com.vyas.pranav.studentcompanion.data.lecturedatabase.LectureEntry;
import com.vyas.pranav.studentcompanion.data.maindatabase.MainDatabase;

import java.util.ArrayList;
import java.util.List;

public class LectureRepository {

    //    private static final Object LOCK = new Object();
//    private static LectureRepository instance;
    private LectureDao lectureDao;

    public LectureRepository(Context context) {
        lectureDao = MainDatabase.getInstance(context).lectureDao();
    }

//    public static LectureRepository getInstance(Context context) {
//        if (instance == null) {
//            synchronized (LOCK) {
//                instance = new LectureRepository(context);
//            }
//        }
//        return instance;
//    }

    public void insertLectures(List<Integer> startTimes, List<Integer> endTimes) {
        List<LectureEntry> lectures = new ArrayList<>();
        for (int i = 0; i < startTimes.size(); i++) {
            lectures.add(new LectureEntry(i, startTimes.get(i), endTimes.get(i)));
        }
        lectureDao.insertAll(lectures);
    }
}
