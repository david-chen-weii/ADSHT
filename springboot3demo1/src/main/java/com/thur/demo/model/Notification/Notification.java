package com.thur.demo.model.Notification;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.thur.demo.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(50)", name = "type", nullable = true)
    private String type;

    @Column(columnDefinition = "TEXT", name = "content")
    private String content;

    @Column(name = "target_id")
    private String targetId;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId" , insertable = true, updatable = false, nullable = false)
    private User userId;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "userId", insertable = true, updatable = false)
    private User senderId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "DATETIME", name = "create_at")
    private Date createdAt;

    @PrePersist
    private void fileCreationDate() {
        if (createdAt == null) {
            createdAt = new Timestamp(System.currentTimeMillis());
        }
    }

}
