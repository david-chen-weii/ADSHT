package com.thur.demo.service;

import org.json.JSONObject;

public interface MessageService {
	
	String addMessage(JSONObject Messages);
	JSONObject findByAllMessage(Integer id);
}
