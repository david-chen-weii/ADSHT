package com.thur.demo.model.Notification;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thur.demo.model.user.User;

import jakarta.transaction.Transactional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserId(User user);
    
    List<Notification> findByUserIdAndIsRead(User userId, Boolean isRead);
    
    long countByUserIdAndIsRead(User userId, Boolean isRead);

    @Query("SELECT n FROM Notification n JOIN n.userId u JOIN UserInfo ui ON u.userId = ui.user.userId WHERE ui.id = :userInfoId")
    List<Notification> findByUserInfoId(@Param("userInfoId") Integer userInfoId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.isRead = true AND n.userId.userId = :userId")
    void deleteAllReadNotificationsByUserId(@Param("userId") UUID userId);
    
}
