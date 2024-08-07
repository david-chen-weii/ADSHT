package com.thur.demo.model.post;

import java.util.List;

import com.thur.demo.model.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postId")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "poster", referencedColumnName = "userId")
    private User poster;

    @Column(name = "content")
    private String content;

    @Column(name = "image", columnDefinition = "VARCHAR(MAX)")
    private String image;

    @Column(name = "likeCount")
    private Integer likeCount;

    @Column(name = "addTime")
    private java.util.Date addTime;

    @Column(name = "permission")
    private Integer permission;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> comment;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.addTime = new java.util.Date();
    }
}
