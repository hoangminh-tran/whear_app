package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.Notification;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
  @Modifying
  @Transactional
  @Query(value = "update notification set status = ?1 where notiid = ?2", nativeQuery = true)
  void un_readNotification(Boolean status, Integer notiID);

  @Query(value = "select * from notification where target_userid = ?1 order by date_time desc", nativeQuery = true)
  List<Notification> getAllByTargetUserID(String targetUserID);

  @Query(value = "select * from notification where target_userid = ?1 and status = false order by date_time desc", nativeQuery = true)
  List<Notification> getUnreadNotification(String targetUserID);

  @Modifying
  @Transactional
  @Query(value = "delete from notification where target_userid = ?1", nativeQuery = true)
  void deleteNotificationByUserID(String userID);
}
