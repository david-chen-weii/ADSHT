package com.thur.demo.model.subscriptions;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@Embeddable
public class SubServicesId implements Serializable {
	
	private UUID userId;
	
	private Integer serviceId;

	public SubServicesId() {
	}
	

// 複合主鍵，必須重新定義equals()與hashCode()
	@Override
	public int hashCode() {
		return Objects.hash(serviceId, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubServicesId other = (SubServicesId) obj;
		return Objects.equals(serviceId, other.serviceId) && Objects.equals(userId, other.userId);
	}
	
}