package com.vyas.pranav.studentcompanion.data.notificationdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

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

    @Query("SELECT * FROM Notifications WHERE date = :date AND name = :name AND short_info = :shortInfo AND url = :url AND venue = :venue")
    LiveData<NotificationEntry> getUniqueNotification(String date, String name, String shortInfo, String url, String venue);

    @Delete
    void deleteNotification(NotificationEntry notification);
}
