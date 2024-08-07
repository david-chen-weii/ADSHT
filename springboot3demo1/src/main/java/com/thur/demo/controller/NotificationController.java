package com.thur.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thur.demo.model.Notification.Notification;
import com.thur.demo.model.Notification.NotificationDTO;
import com.thur.demo.service.NotificationService;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin
public class NotificationController {
	
	@Autowired
	private NotificationService notificationService;
	
	@PostMapping("/create")
    public ResponseEntity<?> createNotification(@RequestBody String body) {
        JSONObject responseBody = new JSONObject();
        JSONObject obj = new JSONObject(body);
        //UUID userId, UUID senderId, String type, String content, String targetId

        String userIdStr = obj.optString("userId", null);
        String senderIdStr = obj.optString("senderId", null);
        String type = obj.optString("type", null);
        String content = obj.optString("content", null);
        String targetId = obj.optString("targetId", null);

        if (userIdStr == null || userIdStr.isEmpty() || senderIdStr == null || senderIdStr.isEmpty() || 
            type == null || content == null || targetId == null) {
            responseBody.put("success", false);
            responseBody.put("message", "所有欄位都是必要的，且不能為空");
            return ResponseEntity.badRequest().body(responseBody.toString());
        }

        try {
            UUID userId = UUID.fromString(userIdStr);
            UUID senderId = UUID.fromString(senderIdStr);

            NotificationDTO notification = notificationService.createNotification(userId, senderId, type, content, targetId);
            return ResponseEntity.ok(notification); // 返回序列化的 NotificationDTO 對象
        } catch (IllegalArgumentException e) {
            responseBody.put("success", false);
            responseBody.put("message", "無效的UUID格式");
            return ResponseEntity.badRequest().body(responseBody.toString());
        } catch (RuntimeException e) {
            responseBody.put("success", false);
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(500).body(responseBody.toString());
        }
    }
    
    @PutMapping("/markAllAsRead")
    public ResponseEntity<?> markAllAsRead(@RequestBody String body) {
        JSONObject responseBody = new JSONObject();
        JSONObject obj = new JSONObject(body);

        String userIdStr = obj.optString("userId", null);
        if (userIdStr == null || userIdStr.isEmpty()) {
            responseBody.put("success", false);
            responseBody.put("message", "User ID is required");
            return ResponseEntity.badRequest().body(responseBody.toString());
        }

        try {
            UUID userId = UUID.fromString(userIdStr);
            notificationService.markAllAsRead(userId);
            responseBody.put("success", true);
            return ResponseEntity.ok(responseBody.toString());
        } catch (IllegalArgumentException e) {
            responseBody.put("success", false);
            responseBody.put("message", "Invalid User ID format");
            return ResponseEntity.badRequest().body(responseBody.toString());
        } catch (RuntimeException e) {
            responseBody.put("success", false);
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(500).body(responseBody.toString());
        }
    }
    
    @GetMapping("/unreadCount")
    public ResponseEntity<?> getUnreadNotificationCount(@RequestParam UUID userId) {
        try {
            long count = notificationService.countUnreadNotifications(userId);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/unread")
    public ResponseEntity<?> getUnreadNotifications(@RequestParam UUID userId) {
        try {
            List<Notification> unreadNotifications = notificationService.getUnreadNotifications(userId);
            
            if (unreadNotifications.isEmpty()) {
                JSONObject response = new JSONObject();
                response.put("success", true);
                response.put("message", "No unread notifications");
                return ResponseEntity.ok(response.toString());
            }
            
            List<NotificationDTO> unreadNotificationDTOs = unreadNotifications.stream()
                    .map(notification -> new NotificationDTO(
                            notification.getId(),
                            notification.getUserId().getUserId(),
                            notification.getSenderId().getUserId(),
                            notification.getType(),
                            notification.getContent(),
                            notification.getTargetId(),
                            notification.getIsRead(),
                            notification.getCreatedAt(),
                            notification.getSenderId().getUserinfo().getUserFirstName(),
                            notification.getSenderId().getUserinfo().getUserLastName(),
                            notification.getSenderId().getUserinfo().getPhoto(),
                            notification.getSenderId().getUserinfo().getId()
                        ))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(unreadNotificationDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    
    @PostMapping("/byUserInfoId")
    public ResponseEntity<?> getNotificationsByUserInfoId(@RequestBody Map<String, Integer> request) {
        try {
            Integer userInfoId = request.get("userInfoId");
            if (userInfoId == null) {
                return ResponseEntity.badRequest().body("userInfoId is required");
            }

            List<Notification> notifications = notificationService.getNotificationsByUserInfoId(userInfoId);
            if (notifications.isEmpty()) {
                return ResponseEntity.ok().body("No notifications found for the user");
            }

            List<NotificationDTO> notificationDTOs = notifications.stream()
                .map(notification -> new NotificationDTO(
                    notification.getId(),
                    notification.getUserId().getUserId(),
                    notification.getSenderId().getUserId(),
                    notification.getType(),
                    notification.getContent(),
                    notification.getTargetId(),
                    notification.getIsRead(),
                    notification.getCreatedAt(),
                    notification.getSenderId().getUserinfo().getUserFirstName(),
                    notification.getSenderId().getUserinfo().getUserLastName(),
                    notification.getSenderId().getUserinfo().getPhoto(),
                    notification.getSenderId().getUserinfo().getId()
                ))
                .collect(Collectors.toList());

            return ResponseEntity.ok(notificationDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    
    //刪除所有已讀通知
    @DeleteMapping("/deleteRead")
    public ResponseEntity<?> deleteReadNotifications(@RequestBody String body) {
        JSONObject responseBody = new JSONObject();
        JSONObject obj = new JSONObject(body);

        String userIdStr = obj.optString("userId", null);
        if (userIdStr == null || userIdStr.isEmpty()) {
            responseBody.put("success", false);
            responseBody.put("message", "User ID is required");
            return ResponseEntity.badRequest().body(responseBody.toString());
        }

        try {
            UUID userId = UUID.fromString(userIdStr);
            notificationService.deleteAllReadNotifications(userId);
            responseBody.put("success", true);
            responseBody.put("message", "All read notifications have been deleted");
            return ResponseEntity.ok(responseBody.toString());
        } catch (IllegalArgumentException e) {
            responseBody.put("success", false);
            responseBody.put("message", "Invalid User ID format");
            return ResponseEntity.badRequest().body(responseBody.toString());
        } catch (RuntimeException e) {
            responseBody.put("success", false);
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(500).body(responseBody.toString());
        }
    }
    
}
