package com.thur.demo.model.friend;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.thur.demo.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "friend")
public class Friend {

    @EmbeddedId
    private FriendId id;

    @Column(columnDefinition = "SMALLINT", nullable = false)
    private Integer state;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss EEEE")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "DATETIME", name = "create_at")
    private Date createTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("sendId")
    @JoinColumn(name = "sendId", referencedColumnName = "userId", insertable = false, updatable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("receiveId")
    @JoinColumn(name = "receiveId", referencedColumnName = "userId", insertable = false, updatable = false)
    private User receiver;

    @PrePersist
    private void fileCreationDate() {
        if (createTime == null) {
            createTime = new Timestamp(System.currentTimeMillis());
        }
    }

    // Constructors
    public Friend() {
    }

    public Friend(FriendId id, Integer state) {
        this.id = id;
        this.state = state;
    }

    // Getters and Setters
    public FriendId getId() {
        return id;
    }

    public void setId(FriendId id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
