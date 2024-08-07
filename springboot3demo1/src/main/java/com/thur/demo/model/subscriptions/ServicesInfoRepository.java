package com.thur.demo.model.subscriptions;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServicesInfoRepository extends JpaRepository<ServicesInfo, Integer>, ServicesInfoDAO{

	
	@Query(
			value=    "SELECT SI "
					+ "FROM  SubServices SS "
					+ "JOIN  ServicesInfo SI "
					+ "ON SS.servicesInfo.serviceId = SI.serviceId "
					+ "WHERE SS.user.userId = :userId "
			)
	List<ServicesInfo> findMySubs(@Param("userId") UUID userId);
	

    @Query(
    		value = "SELECT S2 "
    		+ "FROM  Subscriptions S1 "
    		+ "JOIN  ServicesInfo S2 "
    		+ "ON S1.servicesInfo.serviceId = S2.serviceId "
    		+ "WHERE S1.user.userId = :userId "
    		)
    List<ServicesInfo> findCreater(@Param("userId") UUID userId);
    
//    @Query(
//    		value = "SELECT S1 FROM  Subscriptions S1 WHERE S1.user.userId = :userId "
//    		)
//    List<ServicesInfo> findCreater(@Param("userId") UUID userId);
}
