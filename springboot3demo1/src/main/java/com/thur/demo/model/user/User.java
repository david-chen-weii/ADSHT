package com.thur.demo.model.user;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.thur.demo.model.Notification.Notification;
import com.thur.demo.model.friend.Friend;
import com.thur.demo.model.group.GroupMembers;
import com.thur.demo.model.group.GroupMessages;
import com.thur.demo.model.group.Groups;
import com.thur.demo.model.post.Post;
import com.thur.demo.model.post.PostComment;
import com.thur.demo.model.post.SubComment;
import com.thur.demo.model.shopping.Shop;
import com.thur.demo.model.subscriptions.Subscriptions;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "userId")
	private UUID userId;

	@Column(name = "userAccount", nullable = false, unique = true)
	private String userAccount;

	@Column(name = "userPassword", nullable = false)
	private String userPassword;

	@OneToMany(mappedBy = "user")
	private Set<GroupMembers> groupMembers;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Groups> groups = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private Set<GroupMessages> groupMessages;

	@Column(name = "establishTime", columnDefinition = "DateTime")
	private java.util.Date establishTime;

	@Column(name = "lastLogin", columnDefinition = "DateTime")
	private java.util.Date lastLogin;

	// 修改
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private UserInfo userinfo;

	@OneToMany(mappedBy = "poster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Post> posts;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostComment> comments;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SubComment> subComments;

	// shop
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Shop> shopOwner;

	// for friend
	@OneToMany(mappedBy = "sender")
	private List<Friend> sentFriends;
	// for friend
	@OneToMany(mappedBy = "receiver")
	private List<Friend> receivedFriends;

	// for Notifications
	@OneToMany(mappedBy = "senderId")
	private List<Notification> notificationSender;
	// for Notifications
	@OneToMany(mappedBy = "userId")
	private List<Notification> notificationUser;

	// for Subscriptions
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Subscriptions> subscriptions;

	@PrePersist
	protected void onCreate() {
		establishTime = java.util.Date.from(Instant.now());
	}

	@PreUpdate
	protected void onUpdate() {
		lastLogin = new java.util.Date();

	}

	@Override
	public String toString() {

		return "{\"account\":\"" +
				this.getUserAccount() +
				"\",\"password\":\"" +
				this.getUserPassword()
				+ "\",userId:\""
				+ this.getUserId() + "\"}";
	}

}
