package com.thur.demo.model.Notification;

import java.util.Date;
import java.util.UUID;

public class NotificationDTO {
    private Integer id;
    private String type;
    private String content;
    private String targetId;
    private Boolean isRead;
    private UUID  userId;
    private UUID  senderId;
    private Date createdAt;
    private String userFirstName;
    private String userLastName;
    private String photo;
    private Integer userInfoId;
    // Default constructor
    public NotificationDTO() {
    }

    // Parameterized constructor
    public NotificationDTO(Integer id, UUID userId, UUID senderId, String type, String content, String targetId, Boolean isRead, Date createdAt, String userFirstName, String userLastName,String photo, Integer userInfoId) {
        this.id = id;
        this.userId = userId;
        this.senderId = senderId;
        this.type = type;
        this.content = content;
        this.targetId = targetId;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.photo = photo;
        this.userInfoId = userInfoId;
    }
    
    
    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Integer getUserInfoId() {
		return userInfoId;
	}

	public void setUserInfoId(Integer userInfoId) {
		this.userInfoId = userInfoId;
	}
    
}
