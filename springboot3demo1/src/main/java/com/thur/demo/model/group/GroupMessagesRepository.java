package com.thur.demo.model.group;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupMessagesRepository extends JpaRepository<GroupMessages,Integer>{

	@Query("SELECT gms FROM GroupMessages gms WHERE gms.groups.groupId = :groupId")
    List<GroupMessages> finyByGroupAllMessages(@Param("groupId") Integer groupId);
}
