package com.thur.demo.model.group;

import java.util.Date;

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
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "groupmembers")
public class GroupMembers {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer member_id;

    @ManyToOne
    @JoinColumn(name ="groupId",referencedColumnName = "groupId")
    private Groups groups;

    @ManyToOne
    @JoinColumn(name ="userId",referencedColumnName = "userId")
    private User user;
    
    
    @Column(name = "is_admin")
    private boolean is_admin;
    
    @Column(name = "isCheck")
    private boolean isCheck;

    @Column(name = "join_at")
    private Date join_at;
    
    @Column(name = "memberName")
    private String memberName;
    
    @Lob
    private byte[] memberImage;

}
