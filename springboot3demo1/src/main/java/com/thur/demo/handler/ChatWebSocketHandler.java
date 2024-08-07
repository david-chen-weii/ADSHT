package com.thur.demo.handler;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thur.demo.model.group.GroupMembers;
import com.thur.demo.model.group.GroupMembersRepository;
import com.thur.demo.model.group.ImageData;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<Integer, List<WebSocketSession>> groupSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
//    @Autowired
//	private GroupMembersRepository gMembersRep;
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 只需要 groupId
        Integer groupId = (Integer) session.getAttributes().get("groupId");
        System.out.println("WS通...");
        if (groupId != null) {
            groupSessions.computeIfAbsent(groupId, k -> new ArrayList<>()).add(session);
            session.sendMessage(new TextMessage("Welcome to group " + groupId + "!"));
            System.out.println("WS開了...");
        } else {
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	System.out.println("有通...");
    	
    	String payload = message.getPayload();
        ImageData imageData = objectMapper.readValue(payload, ImageData.class);
        int groupId = imageData.getGroupId();
        ImageData newMessagePayload = new ImageData();
//        newMessagePayload.setMessage("User " + imageData.getSender_id() + ": " + imageData.getMessage());
        newMessagePayload.setImage(imageData.getImage());
        newMessagePayload.setContent(imageData.getContent());
        System.out.println("有文字訊息");
//        Optional<GroupMembers> optionalMember = gMembersRep.findByGroupIdAndUserId(groupId,imageData.getSender_id());
//        if (optionalMember.isPresent()) {
//            GroupMembers member = optionalMember.get();
            newMessagePayload.setMemberName(imageData.getMemberName());
//            String imageBase64 = Base64.getEncoder().encodeToString();
            newMessagePayload.setMemberImage(imageData.getMemberImage());
//            }
        newMessagePayload.setGroupId(groupId);
        newMessagePayload.setSender_id(imageData.getSender_id());
        System.out.println("往前端中...");
        String newMessage = objectMapper.writeValueAsString(newMessagePayload);
        System.out.println("準備中...");
        List<WebSocketSession> sessions = groupSessions.get(groupId);
        if (sessions != null) {
            for (WebSocketSession webSocketSession : sessions) {
                if (webSocketSession.isOpen()) {
                    webSocketSession.sendMessage(new TextMessage(newMessage));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Integer groupId = (Integer) session.getAttributes().get("groupId");

        if (groupId != null) {
            List<WebSocketSession> sessions = groupSessions.get(groupId);
            if (sessions != null) {
            	System.out.println("WS切斷...");
                sessions.remove(session);
                if (sessions.isEmpty()) {
                	System.out.println("任務退出...");
                    groupSessions.remove(groupId);
                }
            }
        }
    }
}
