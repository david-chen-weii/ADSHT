package com.thur.demo.model.subscriptions;

import com.thur.demo.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table( name = "SubServices")
public class SubServices {

	@EmbeddedId
	private SubServicesId subServicesId;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("serviceId")
	private ServicesInfo servicesInfo;

	@Column(name = "service_name")
	private String serviceName;
}