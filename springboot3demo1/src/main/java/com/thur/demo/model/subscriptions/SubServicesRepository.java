package com.thur.demo.model.subscriptions;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubServicesRepository extends JpaRepository<SubServices, SubServicesId>  {

	@Query(value="select s from SubServices s where s.subServicesId.userId = :userId and s.subServicesId.serviceId = :serviceId")
	SubServices findByUsersIdAndServiceId(@Param("userId") UUID userId, @Param("serviceId") Integer serviceId);

	
	@Query(value="select s from SubServices s where s.subServicesId.userId = :userId")
	List<SubServices> findByUsersId(@Param("userId") UUID userId);
}
