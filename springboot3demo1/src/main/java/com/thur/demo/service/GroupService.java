package com.thur.demo.service;

import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.thur.demo.model.friend.Friend;
import com.thur.demo.model.group.GroupMembers;
import com.thur.demo.model.group.Groups;
import com.thur.demo.model.group.Groups.GroupStatus;

public interface GroupService {

<<<<<<< HEAD
	String addGroupName(UUID id,String name,GroupStatus status,byte[] originalData);
=======
	String addGroupName(UUID id, String name, GroupStatus status, byte[] originalData);

>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
	boolean existsByGroupName(String name);

	List<Groups> findGroupName(String name);
<<<<<<< HEAD
	JSONObject findByMyGroup(UUID id);
	Boolean findGroupCreter(Integer id,UUID craterid);
	Boolean existsByGroupAdmain(Integer id,UUID craterid);
//	String findMember(Integer id);
	String findStatusByGroupId(Integer id);
	JSONObject addMember(JSONObject gmember);
	boolean existsByGroupId(Integer id);
	JSONObject findByAllmember(Integer gid);
	boolean checkPrivateAllow(UUID id,Integer gid);
=======

	JSONObject findByMyGroup(UUID id);

	Boolean findGroupCreter(Integer id, UUID craterid);

	Boolean existsByGroupAdmain(Integer id, UUID craterid);

	// String findMember(Integer id);
	String findStatusByGroupId(Integer id);

	JSONObject addMember(JSONObject gmember);

	boolean existsByGroupId(Integer id);

	JSONObject findByAllmember(Integer gid);

	boolean checkPrivateAllow(UUID id, Integer gid);

>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
	JSONObject findByGroupRequisition(Integer gid);

	// test
	JSONObject findByGroupsRequisition(Integer gid);

	JSONObject addminApprove(String list);
<<<<<<< HEAD
	
	boolean   existsByGroupMemberId(UUID id,Integer gid);
	boolean leaveGroup(UUID user,Integer gid);
	
	List<Friend> addAllFriends(UUID sendId);
	String addmemberNameAndImage(JSONObject gmember,UUID id);
	void printDataType(Object obj);
	JSONObject findmemberNameAndImage(UUID sendId,Integer gid);
=======

	boolean existsByGroupMemberId(UUID id, Integer gid);

	boolean leaveGroup(UUID user, Integer gid);

	List<Friend> addAllFriends(UUID sendId);

	String addmemberNameAndImage(JSONObject gmember, UUID id);

	void printDataType(Object obj);

	JSONObject findmemberNameAndImage(UUID sendId, Integer gid);
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
}
