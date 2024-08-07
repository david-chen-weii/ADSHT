package com.thur.demo.model.subscriptions;

import java.time.Instant;
import java.util.Date;

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
import lombok.Setter;

@Getter
@Setter
@Entity
@Table( name = "Subscriptions")
public class Subscriptions {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subscription_id")
	private Integer subscriptionId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "service_id")
	private ServicesInfo servicesInfo;
	
	@Column(name = "is_active")
	private boolean isActive;
	
	@Column(name = "level")
	private Integer level;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "update_at")
	private Date updateAt;

	@PrePersist
	protected void onCreate() {
		createdAt = java.util.Date.from(Instant.now());
	}

	@PreUpdate
	protected void onUpdate() {
		updateAt = new java.util.Date();

	}
}
