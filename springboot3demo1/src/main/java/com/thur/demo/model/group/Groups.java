package com.thur.demo.model.group;

import java.util.Date;
import java.util.Set;

import com.thur.demo.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "groups")
public class Groups {
    

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "groupId")
    private Integer groupId;

    @OneToMany(mappedBy = "groups")
    private Set<GroupMembers> groupMembers;


    @Column(name = "groupName")
    private String groupName;
    
    @Lob
    private byte[] image;
    
    @ManyToOne
    @JoinColumn(name ="createrId",referencedColumnName = "userId")
    private User user;
    
    
    public enum GroupStatus {
        PUBLIC,
        PRIVATE,
        FRIENDS
    }
        
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GroupStatus status; // 使用枚舉來表示群組狀態

    @Column(name = "create_at")
    private Date create_at;


}
