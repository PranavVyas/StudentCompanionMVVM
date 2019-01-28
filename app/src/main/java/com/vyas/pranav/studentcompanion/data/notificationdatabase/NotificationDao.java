package com.vyas.pranav.studentcompanion.data.notificationdatabase;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface NotificationDao {

    @Query("SELECT * FROM Notifications")
    LiveData<List<NotificationEntry>> getAllNotifications();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotification(NotificationEntry notificationEntry);

    @Query("SELECT * FROM Notifications WHERE date >= :date")
    LiveData<List<NotificationEntry>> getNotificationAfter(Date date);

    @Query("DELETE FROM Notifications WHERE _ID = :id")
    void deleteNotification(long id);

    @Query("SELECT COUNT(date) FROM Notifications WHERE date >= :date")
    LiveData<Integer> getCurrentNotificationCount(Date date);
}
