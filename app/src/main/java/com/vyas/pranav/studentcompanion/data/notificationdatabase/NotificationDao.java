package com.vyas.pranav.studentcompanion.data.notificationdatabase;
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
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.vyas.pranav.studentcompanion.data.notificationdatabase.firestore.NotificationFirestoreModel;

import java.util.List;

@Dao
public interface NotificationDao {

    @Query("SELECT * FROM Notifications")
    LiveData<List<NotificationFirestoreModel>> getAllNotifications();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotification(NotificationFirestoreModel notificationFirestoreModel);

    @Insert()
    void insertAllNotifications(List<NotificationFirestoreModel> allNotifications);

    @Query("DELETE FROM Notifications WHERE _ID = :id")
    void deleteNotification(String id);

    @Query("SELECT COUNT(dateInMillis) FROM Notifications WHERE dateInMillis >= :dateInMillis")
    LiveData<Integer> getCurrentNotificationCount(long dateInMillis);

    @Query("SELECT * FROM Notifications WHERE _ID = :ID")
    LiveData<NotificationFirestoreModel> getNotificationById(String ID);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNotification(NotificationFirestoreModel notificationFirestoreModel);

    @Query("SELECT * FROM Notifications WHERE _ID = :ID")
    NotificationFirestoreModel getNotificationByIdInForeground(String ID);

    @Query(("SELECT * FROM Notifications WHERE type = :type"))
    List<NotificationFirestoreModel> getNotificationByTypeInForeground(int type);

    @Delete
    void deleteNotifications(List<NotificationFirestoreModel> notifications);

    @Query("DELETE FROM Notifications")
    void deleteAllNotifications();

}
