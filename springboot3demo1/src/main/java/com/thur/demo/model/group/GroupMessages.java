package com.thur.demo.model.group;

import com.thur.demo.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "groupMessages")
public class GroupMessages {
    
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer message_id;


    @ManyToOne
    @JoinColumn(name ="groupId",referencedColumnName = "groupId")
    private Groups groups;

    @ManyToOne
    @JoinColumn(name ="sender_id",referencedColumnName = "userId")
    private User user;

    @Column(name = "content")
    private String content;

    @Lob
    private byte[] image;

    
    @Column(name = "sent_at", columnDefinition = "DateTime")
    private java.util.Date sent_at;

    
}
