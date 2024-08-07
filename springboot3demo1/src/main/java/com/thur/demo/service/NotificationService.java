package com.thur.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thur.demo.model.Notification.Notification;
import com.thur.demo.model.Notification.NotificationDTO;
import com.thur.demo.model.Notification.NotificationRepository;
import com.thur.demo.model.user.User;
import com.thur.demo.model.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class NotificationService {
	
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public NotificationDTO createNotification(UUID userId, UUID senderId, String type, String content, String targetId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));

        Notification notification = new Notification();
        notification.setUserId(user);
        notification.setSenderId(sender);
        notification.setType(type);
        notification.setContent(content);
        notification.setTargetId(targetId);
        Notification savedNotification = notificationRepository.save(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(savedNotification.getId());
        notificationDTO.setType(savedNotification.getType());
        notificationDTO.setContent(savedNotification.getContent());
        notificationDTO.setTargetId(savedNotification.getTargetId());
        notificationDTO.setIsRead(savedNotification.getIsRead());
        notificationDTO.setUserId(savedNotification.getUserId().getUserId());
        notificationDTO.setSenderId(savedNotification.getSenderId().getUserId());
        notificationDTO.setCreatedAt(savedNotification.getCreatedAt());

        return notificationDTO;
    }
    
    public List<Notification> getNotificationByUserId(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.findByUserId(user);
    }
    
    @Transactional
    public void markAllAsRead(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Notification> notifications = notificationRepository.findByUserIdAndIsRead(user, false);
        for (Notification notification : notifications) {
            notification.setIsRead(true);
        }
        notificationRepository.saveAll(notifications);
    }
    
    @Transactional
    public void deleteNotification(Integer notificationId) {
        notificationRepository.deleteById(notificationId);
    }
    
    // 查詢未讀通知數量
    public long countUnreadNotifications(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.countByUserIdAndIsRead(user, false);
    }

    // 查詢所有未讀通知
    public List<Notification> getUnreadNotifications(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.findByUserIdAndIsRead(user, false);
    }
    
    //流水號找對應用戶通知
    public List<Notification> getNotificationsByUserInfoId(Integer userInfoId) {
        return notificationRepository.findByUserInfoId(userInfoId);
    }
    
    //刪除所有已讀通知
    @Transactional
    public void deleteAllReadNotifications(UUID userId) {
        notificationRepository.deleteAllReadNotificationsByUserId(userId);
    }
    

    // 查詢未讀通知數量通過用戶名
    public long countUnreadNotificationsByUsername(String username) {
        User user = userRepository.findByuserAccount(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return notificationRepository.countByUserIdAndIsRead(user, false);
    }

    // 查詢所有未讀通知通過用戶名
    public List<Notification> getUnreadNotificationsByUsername(String username) {
        User user = userRepository.findByuserAccount(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return notificationRepository.findByUserIdAndIsRead(user, false);
    }

}
