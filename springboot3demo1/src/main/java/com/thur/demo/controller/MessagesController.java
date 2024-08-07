package com.thur.demo.controller;


import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thur.demo.service.GroupService;
import com.thur.demo.service.MessageService;

@RestController
@RequestMapping("/ajax/pages")
@CrossOrigin
public class MessagesController {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private GroupService groupService;
	
	@PostMapping("/Message/tmesges")
	public String addMessage(@RequestBody String body) {
		
		JSONObject responseBody = new JSONObject();
		JSONObject response = new JSONObject();
		JSONObject obj = new JSONObject(body);
		String imageBase64 = obj.isNull("image") ? null : obj.getString("image");
		String content = obj.isNull("content") ? null : obj.getString("content");
		
		if( ( imageBase64 == null ) && ( content == null ) ) {
														
																responseBody.put("success", false);
																responseBody.put("message", "沒有訊息");
				
																}
														else {	
																messageService.addMessage(obj);
																response = groupService.findmemberNameAndImage(UUID.fromString(obj.getString("sender_id")), obj.getInt("groupId"));
																responseBody.put("success", response);
																responseBody.put("message", "訊息傳送成功");
																}
		
		return responseBody.toString();
	}
	
	@PostMapping("/Message/findtmesges")
	public String findByAllmessages (@RequestBody String body) {
		JSONObject responseBody = new JSONObject();
		JSONObject obj = new JSONObject(body);
		Integer id = obj.isNull("groupId")? null:obj.getInt("groupId");
		
		responseBody = messageService.findByAllMessage(id);
		return responseBody.toString();
	}
	
}
