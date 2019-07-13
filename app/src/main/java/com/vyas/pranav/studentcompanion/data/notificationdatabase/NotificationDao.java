package com.vyas.pranav.studentcompanion.data.notificationdatabase;

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
