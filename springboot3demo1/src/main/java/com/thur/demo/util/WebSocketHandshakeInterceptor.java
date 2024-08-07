package com.thur.demo.util;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

public class WebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            org.springframework.web.socket.WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        String query = request.getURI().getQuery();
        if (query != null && query.contains("groupId=")) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("groupId=")) {
                    try {
                        int groupId = Integer.parseInt(param.substring("groupId=".length()));
                        attributes.put("groupId", groupId);
                    } catch (NumberFormatException e) {
                        response.setStatusCode(HttpStatus.BAD_REQUEST);
                        return false;
                    }
                }
            }
        } else {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return false;
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            org.springframework.web.socket.WebSocketHandler wsHandler, Exception ex) {
        // 無需額外操作
    }
}
