package com.thur.demo.model.user;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "UserInfo")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "userFirstName")
    private String userFirstName;

    @Column(name = "userLastName", nullable = false)
    private String userLastName;

    @Column(name = "birth", columnDefinition = "Date")
    private Date birth;

    @Column(name = "gender", columnDefinition = "smallint")
    private Integer gender;

    @Column(name = "email")
    private String email;

    @Column(name = "phone", length = 10)
    private String phone;

    @Column(name = "selfContent")
    private String selfContent;

    @Column(name = "photo", columnDefinition = "VARCHAR(MAX)")
    private String photo;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @Column(name = "status", columnDefinition = "smallint")
    private Integer status; // 1是活躍 0是註銷

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = 1; // Default to active when the entity is first created
        }
    }

}