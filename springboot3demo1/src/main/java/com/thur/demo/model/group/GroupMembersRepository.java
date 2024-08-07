package com.thur.demo.model.group;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thur.demo.model.group.Groups.GroupStatus;

<<<<<<< HEAD
public interface GroupMembersRepository extends JpaRepository<GroupMembers,Integer > {
	
//	@Query("SELECT gm.user.userId , gm.groups.groupId FROM GroupMembers gm WHERE gm.user.userId = :userId1 AND gm.groups.groupId = :groupId1")
//	List<Boolean> existsByGroupIdAndUserId(@Param("userId1") UUID userId1,@Param("groupId") Integer groupId1);
	
//	查詢朋友群組裡是否存在這2個成員
	@Query("SELECT gm.groups.groupId FROM GroupMembers gm " +
		       "JOIN gm.groups g " +
		       "WHERE g.status = 'FRIENDS' " +
		       "AND gm.user.userId IN (:userId1, :userId2) " +
		       "GROUP BY gm.groups.groupId " +
		       "HAVING COUNT(gm.user.userId) = 2")
	List<Integer> findGroupIdByStatusAndTwoUserIds( @Param("userId1") UUID userId1,@Param("userId2") UUID userId2);
	
	@Query("SELECT gm FROM GroupMembers gm WHERE gm.groups.groupId = :groupId AND gm.is_admin = :is_admin")
    Optional<GroupMembers> findByfriendImage(@Param("groupId") Integer groupId, @Param("is_admin") Boolean is_admin);
	
	@Query("SELECT gm FROM GroupMembers gm WHERE gm.groups.groupId = :groupId AND gm.user.userId = :userId")
    Optional<GroupMembers> findByGroupIdAndUserId(@Param("groupId") Integer groupId, @Param("userId") UUID userId);
	
	@Query("SELECT gm.user.userId FROM GroupMembers gm WHERE gm.groups.groupId = :groupId AND gm.is_admin = true")
    Optional<UUID> findByAdmin(@Param("groupId") Integer groupId);
	
	@Query("SELECT gm FROM GroupMembers gm JOIN FETCH gm.groups WHERE gm.user.userId = :userId AND gm.isCheck = :isCheck")
    List<GroupMembers> finyByMyGroupsId(@Param("userId") UUID userId, @Param("isCheck") Boolean isCheck);
	
=======
public interface GroupMembersRepository extends JpaRepository<GroupMembers, Integer> {

	// @Query("SELECT gm.user.userId , gm.groups.groupId FROM GroupMembers gm WHERE
	// gm.user.userId = :userId1 AND gm.groups.groupId = :groupId1")
	// List<Boolean> existsByGroupIdAndUserId(@Param("userId1") UUID
	// userId1,@Param("groupId") Integer groupId1);

	// 查詢朋友群組裡是否存在這2個成員
	@Query("SELECT gm.groups.groupId FROM GroupMembers gm " +
			"JOIN gm.groups g " +
			"WHERE g.status = 'FRIENDS' " +
			"AND gm.user.userId IN (:userId1, :userId2) " +
			"GROUP BY gm.groups.groupId " +
			"HAVING COUNT(gm.user.userId) = 2")
	List<Integer> findGroupIdByStatusAndTwoUserIds(@Param("userId1") UUID userId1, @Param("userId2") UUID userId2);

	@Query("SELECT gm FROM GroupMembers gm WHERE gm.groups.groupId = :groupId AND gm.is_admin = false")
	Optional<GroupMembers> findByfriendImage(@Param("groupId") Integer groupId);

	@Query("SELECT gm FROM GroupMembers gm WHERE gm.groups.groupId = :groupId AND gm.user.userId = :userId")
	Optional<GroupMembers> findByGroupIdAndUserId(@Param("groupId") Integer groupId, @Param("userId") UUID userId);

	@Query("SELECT gm.user.userId FROM GroupMembers gm WHERE gm.groups.groupId = :groupId AND gm.is_admin = true")
	Optional<UUID> findByAdmin(@Param("groupId") Integer groupId);

	@Query("SELECT gm FROM GroupMembers gm JOIN FETCH gm.groups WHERE gm.user.userId = :userId AND gm.isCheck = :isCheck")
	List<GroupMembers> finyByMyGroupsId(@Param("userId") UUID userId, @Param("isCheck") Boolean isCheck);

>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
	@Query("SELECT COUNT(gm) > 0 FROM GroupMembers gm WHERE gm.groups.groupId = :groupId AND gm.user.userId = :userId")
	boolean existsByGroupIdAndUserId(@Param("groupId") Integer groupId, @Param("userId") UUID userId);

	@Query("SELECT gm.member_id FROM GroupMembers gm WHERE gm.groups.groupId = :groupId AND gm.user.userId = :userId")
	Integer findByMemberId(@Param("groupId") Integer groupId, @Param("userId") UUID userId);

	@Query("SELECT gm.user.userId, gm.isCheck  FROM GroupMembers gm WHERE gm.groups.groupId = :groupId AND gm.isCheck = :isCheck")
	List<Object[]> finyByGroupId(@Param("groupId") Integer groupId, @Param("isCheck") Boolean isCheck);

	@Query("SELECT gm FROM GroupMembers gm WHERE gm.groups.groupId = :groupId AND gm.isCheck = :isCheck")
	List<GroupMembers> finyByGroupsId(@Param("groupId") Integer groupId, @Param("isCheck") Boolean isCheck);
}
