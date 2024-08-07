package com.thur.demo.model.group;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupsRepository extends JpaRepository<Groups, Integer> {
	boolean existsByGroupName(String name);
<<<<<<< HEAD
	
	
	List<Groups> findByGroupName(String groupName);
	
	boolean existsById(Integer groupId);
	
//	找自己創建的朋友群組Id
	@Query("SELECT g.groupId FROM Groups g WHERE g.user.userId = :createrId and g.status = 'FRIENDS' and g.groupName = :groupName")
	String findMyfriendGroupId(@Param("createrId")UUID createrId,@Param("groupName") String groupName);
	
//	找是否為朋友群組的創建者 要拿來抓群組名用的
	@Query("SELECT COUNT(g) > 0 FROM Groups g WHERE g.groupId = :groupId AND g.user.userId = :createrId")
	boolean existsByCreaterId(@Param("groupId") Integer groupId, @Param("createrId") UUID createrId);
	
	@Query("SELECT g.status FROM Groups g WHERE g.groupId = :groupId")
	String findStatusByGroupId(@Param("groupId")Integer groupId);
	
	
=======

	List<Groups> findByGroupName(String groupName);

	boolean existsById(Integer groupId);

	// 找自己創建的朋友群組Id
	@Query("SELECT g.groupId FROM Groups g WHERE g.user.userId = :createrId and g.status = 'FRIENDS' and g.groupName = :groupName")
	String findMyfriendGroupId(@Param("createrId") UUID createrId, @Param("groupName") String groupName);

	// 找是否為朋友群組的創建者 要拿來抓群組名用的
	@Query("SELECT COUNT(g) > 0 FROM Groups g WHERE g.groupId = :groupId AND g.user.userId = :createrId")
	boolean existsByCreaterId(@Param("groupId") Integer groupId, @Param("createrId") UUID createrId);

	@Query("SELECT g.status FROM Groups g WHERE g.groupId = :groupId")
	String findStatusByGroupId(@Param("groupId") Integer groupId);

>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
}
