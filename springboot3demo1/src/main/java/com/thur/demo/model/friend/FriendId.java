package com.thur.demo.model.friend;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public class FriendId implements Serializable {
    private UUID sendId;
    private UUID receiveId;

    // Constructors, Getters, Setters, equals, and hashCode methods

    public FriendId() {
    }

    public FriendId(UUID sendId, UUID receiveId) {
        this.sendId = sendId;
        this.receiveId = receiveId;
    }

    public UUID getSendId() {
        return sendId;
    }

    public void setSendId(UUID sendId) {
        this.sendId = sendId;
    }

    public UUID getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(UUID receiveId) {
        this.receiveId = receiveId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        FriendId friendId = (FriendId) o;

        if (!sendId.equals(friendId.sendId))
            return false;
        return receiveId.equals(friendId.receiveId);
    }

    @Override
    public int hashCode() {
        int result = sendId.hashCode();
        result = 31 * result + receiveId.hashCode();
        return result;
    }
}
