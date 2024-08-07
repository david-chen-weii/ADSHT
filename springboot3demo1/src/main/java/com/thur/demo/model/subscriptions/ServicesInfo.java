package com.thur.demo.model.subscriptions;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table( name = "ServicesInfo")
public class ServicesInfo {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "service_name", nullable = false)
	private String serviceName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "price")
	private Integer price;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "update_at")
	private Date updateAt;
	
    @Column(name = "servicesImage", columnDefinition = "VARCHAR(MAX)")
    private String image;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "servicesInfo")
	private List<Subscriptions> subscriptions;
	
	@PrePersist
	protected void onCreate() {
		createdAt = java.util.Date.from(Instant.now());
	}

	@PreUpdate
	protected void onUpdate() {
		updateAt = new java.util.Date();

	}
}
