package com.thur.demo.util;

import java.util.Enumeration;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.thur.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * config SpringConfig 設定監聽
 */
@Component
public class Handler implements HandlerInterceptor {
	@Autowired
	UserService userService;

	@Autowired
	JsonWebTokenUtility tokenUtility;

	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler) throws Exception {

		String method = request.getMethod(); // 確認方法 POST OR GET ...
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			System.out.println("headerName: " + headerName + "headerValue: " + headerValue);
		}
		// 如果是非同域政策
		if (!"OPTION".equalsIgnoreCase(method)) {
			String auth = request.getHeader("Authorization");
			JSONObject result = processAuthorizationHeader(auth);
			System.out.println("auth = " + auth);
			if (result == null || result.length() == 0) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.setHeader("Access-Control-Allow-Credentials", "true");
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Header", "*");
				System.out.println("notAllow");
				return false;
			}
		}
		return true;
	}

	private JSONObject processAuthorizationHeader(String auth) {
		// 驗證JWT 並取得使用者資料
		System.out.println("procseeAuthor : " + auth);

		if (auth != null && auth.length() != 0) {

			auth = auth.substring(7);
			String res = tokenUtility.validateEncryptedToken(auth);
			// 使用者資料
			if (res != null) {

				return new JSONObject(res);
			}
		}
		return null;
	}

}
