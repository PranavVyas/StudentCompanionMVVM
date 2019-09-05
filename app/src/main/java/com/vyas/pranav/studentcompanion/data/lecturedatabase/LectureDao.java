package com.vyas.pranav.studentcompanion.data.lecturedatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LectureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LectureEntry lectureEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LectureEntry> lectures);

    @Query("SELECT * FROM Lectures")
    List<LectureEntry> getAll();
}
