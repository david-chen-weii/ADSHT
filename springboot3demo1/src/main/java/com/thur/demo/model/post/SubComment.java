package com.thur.demo.model.post;

import com.thur.demo.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "subComment")
public class SubComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subCommentId")
    private Integer id;

    @Column(name = "content")
    private String content;

    @Column(name = "addTime")
    private java.util.Date addTime;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.addTime = new java.util.Date();
    }

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "commentId", referencedColumnName = "commentId")
    private PostComment postComment;
}
