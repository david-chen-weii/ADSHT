package com.thur.demo.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thur.demo.model.group.GroupMembers;
import com.thur.demo.model.group.GroupMembersRepository;
import com.thur.demo.model.group.GroupMessages;
import com.thur.demo.model.group.GroupMessagesRepository;
import com.thur.demo.model.group.Groups;
import com.thur.demo.model.group.GroupsRepository;
import com.thur.demo.model.user.User;
import com.thur.demo.model.user.UserRepository;

@Service
public class MessageServiceImpl implements MessageService{
	
	@Autowired
	private GroupMessagesRepository groupMessagesRep;
	
	@Autowired
	private GroupsRepository groupsRep;
	
	@Autowired
	private UserRepository userRep;
	
	 @Autowired
	private GroupMembersRepository gMembersRep;
	
<<<<<<< HEAD
	@Override
=======
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
	public String addMessage(JSONObject messages) {
		
		int groupId = messages.getInt("groupId");
		UUID senderId = UUID.fromString(messages.getString("sender_id"));
		String imageBase64 = messages.getString("image");
		String content = messages.getString("content");
		Groups group = groupsRep.findById(groupId).get();
		User user = userRep.findById(senderId).get();
		
//		image 做資料轉換避免過大
		byte[] originalData = Base64.getDecoder().decode(imageBase64);
		System.out.println(originalData);
		GroupMessages groupMessages =  new GroupMessages();
		groupMessages.setGroups(group);
		groupMessages.setUser(user);
		groupMessages.setContent(content);
		groupMessages.setSent_at(new Date());
		groupMessages.setImage(originalData);
		groupMessagesRep.save(groupMessages);
		
		return "成功";
	}
	
<<<<<<< HEAD
	@Override
	public JSONObject findByAllMessage(Integer id) {
	    List<GroupMessages> messages = groupMessagesRep.finyByGroupAllMessages(id);
	    JSONArray jsonArray = new JSONArray();

	    if (messages == null || messages.isEmpty()) {
	        // 如果沒有找到消息，記錄日誌或返回適當的響應
	        System.out.println("No messages found for group id: " + id);
	        return new JSONObject().put("messages", new JSONArray());
	    }
=======
	public JSONObject  findByAllMessage(Integer id) {
		
		List<GroupMessages> messages = groupMessagesRep.finyByGroupAllMessages(id);  // 這裡可以根據需求改成特定群組ID的查詢
		JSONArray jsonArray = new JSONArray();
		
//       if (optionalMember.isPresent()) {
//           GroupMembers member = optionalMember.get();
		
		for (GroupMessages message : messages) {
			JSONObject jsonObject = new JSONObject();
			Optional<GroupMembers> optionalMember = gMembersRep.findByGroupIdAndUserId(message.getGroups().getGroupId(),message.getUser().getUserId());
			jsonObject.put("memberName",optionalMember.get().getMemberName());
			
			String imageBA64 = Base64.getEncoder().encodeToString(optionalMember.get().getMemberImage());
			jsonObject.put("memberImage",imageBA64);
			jsonObject.put("groupId", message.getGroups().getGroupId());
			jsonObject.put("sender_id", message.getUser().getUserId());
			jsonObject.put("content", message.getContent());
			jsonObject.put("sent_at", message.getSent_at());
			String imageBase64 = Base64.getEncoder().encodeToString(message.getImage());
			jsonObject.put("image", imageBase64);
//			System.out.println("memeberImage"+imageBA64);
//			System.out.println("image"+imageBase64);
			jsonArray.put(jsonObject);
		}
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764

	    for (GroupMessages message : messages) {
	        JSONObject jsonObject = new JSONObject();
	        Optional<GroupMembers> optionalMember = gMembersRep.findByGroupIdAndUserId(message.getGroups().getGroupId(), message.getUser().getUserId());

	        if (optionalMember.isPresent()) {
	            GroupMembers member = optionalMember.get();
	            jsonObject.put("memberName", member.getMemberName());

	            String imageBA64 = Base64.getEncoder().encodeToString(member.getMemberImage());
	            jsonObject.put("memberImage", imageBA64);
	        } else {
	            // 如果找不到成員，則填充默認值或跳過
	            jsonObject.put("memberName", "Unknown");
	            jsonObject.put("memberImage", ""); // 或者提供一個默認圖像
	        }

	        jsonObject.put("groupId", message.getGroups().getGroupId());
	        jsonObject.put("sender_id", message.getUser().getUserId());
	        jsonObject.put("content", message.getContent());
	        jsonObject.put("sent_at", message.getSent_at());

	        if (message.getImage() != null) {
	            String imageBase64 = Base64.getEncoder().encodeToString(message.getImage());
	            jsonObject.put("image", imageBase64);
	        } else {
	            jsonObject.put("image", ""); // 或者不返回圖像字段
	        }

	        jsonArray.put(jsonObject);
	    }

	    JSONObject responseBody = new JSONObject();
	    responseBody.put("messages", jsonArray);

	    return responseBody;
	}

	
	
	
	
	
	
}
