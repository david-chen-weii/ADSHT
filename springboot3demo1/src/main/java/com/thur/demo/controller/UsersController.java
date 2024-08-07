package com.thur.demo.controller;

import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thur.demo.model.user.User;
import com.thur.demo.model.user.UserInfo;
import com.thur.demo.service.UserInfoService;
import com.thur.demo.service.UserService;
import com.thur.demo.util.DefaultIcon;
import com.thur.demo.util.JsonWebTokenUtility;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/ajax/pages")
@CrossOrigin
public class UsersController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	JsonWebTokenUtility tokenUtility;
	@Autowired
	private DefaultIcon defaultIcon;

	// Email唯一帳號
	// 使用者註冊
	@PostMapping("/users")
	public String registerUsers(@RequestBody String body, HttpSession session) {

		JSONObject responseBody = new JSONObject();

		JSONObject obj = new JSONObject(body);

		String username = obj.isNull("userName") ? null : obj.getString("userName");

		if (username == null) {
			responseBody.put("success", false)
					.put("message", "帳號是必要欄位");
			return responseBody.toString();
		} else {
			if (userService.chackIfUserNameExist(username)) {
				responseBody.put("success", false)
						.put("message", "帳號已存在");
			} else {
				System.out.println("有找到");
				User user = userService.addUsers(obj, session);
				if ((user == null)) {
					responseBody.put("success", false)
							.put("message", "新增失敗");
				} else {
					responseBody.put("success", true)
							.put("message", "新增成功")
							.put("session", user.getUserId());
					session.setAttribute("loginId", user.getUserId());
				}
			}
		}

		return responseBody.toString();
	}

	// 使用者登入
	@PostMapping("/login")
	public String login(@RequestBody String body, HttpSession session) {
		JSONObject responseBody = new JSONObject();
		JSONObject obj = new JSONObject(body);
		String username = obj.isNull("userName") ? null : obj.getString("userName");
		String password = obj.isNull("password") ? null : obj.getString("password");

		if (username == null || username.length() == 0 || password == null || password.length() == 0) {
			responseBody.put("message", "請輸入帳號密碼")
					.put("success", false);
			return responseBody.toString();
		}

		User bean = userService.login(obj, session);
		if (bean == null) {
			responseBody.put("message", "登入失敗")
					.put("success", false);
			return responseBody.toString();
		}
		// 登入資訊OK
		session.setAttribute("loginId", bean.getUserId());

		responseBody.put("message", "登入成功")
				.put("success", true)
				.put("session", bean.getUserId())
				.put("userinfo_id", bean.getUserinfo().getId())
				.put("status", bean.getUserinfo().getStatus());
		return responseBody.toString();
	}

	@GetMapping("/path")
	public String existsByName(@PathVariable(name = "name") String name) {
		JSONObject responseBody = new JSONObject();

		if (name == null || name.length() == 0) {
			responseBody.put("success", false)
					.put("message", "帳號是必要欄位");
		} else {
			boolean exists = userService.chackIfUserNameExist(name);
			if (exists) {
				responseBody.put("success", false)
						.put("message", "帳號已存在");
			} else {
				responseBody.put("success", true)
						.put("message", "帳號名稱可以使用");
			}
		}

		return responseBody.toString();
	}

	@SuppressWarnings("null")
	@GetMapping("/profile/{id}")
	public String profilePage(@PathVariable(name = "id") int id, HttpSession session) {
		JSONObject response = new JSONObject();
		UserInfo userInfo = userInfoService.findUserInfoByid(id);
		if (userInfo == null) {
			response.put("error", "錯誤");
		}
		String photo;
		if (userInfo.getPhoto() == null) {
			photo = defaultIcon.defaultIcon();
		} else {
			photo = null;
		}

		response.put("gender", userInfo.getGender())
				.put("email", userInfo.getEmail())
				.put("phone", userInfo.getPhone())
				.put("content", userInfo.getSelfContent())
				.put("firstName", userInfo.getUserFirstName())
				.put("lastName", userInfo.getUserLastName())
				.put("birth", userInfo.getBirth())
				.put("photo", photo)
				.put("id", userInfo.getId());
		return response.toString();
	}

	@PostMapping("/logout")
	public String postMethodName(HttpSession session) {
		// TODO: process POST request
		JSONObject response = new JSONObject();
		session.invalidate();
		response.put("success", "登出");
		return response.toString();
	}

	@PostMapping("/resetPasswordCheck")
	public String postMethodName() {

		return "aaa";
	}

	@PostMapping("/resetPasswordCheck/reset")
	public String postMethodName(@RequestBody String request, @RequestHeader("authorization") String token) {
		JSONObject response = new JSONObject();
		JSONObject body = new JSONObject(request);
		if (body.getString("password").equals(body.getString("passwordCheck"))) {
			System.out.println("Step1");
			JSONObject data = new JSONObject(tokenUtility.validateEncryptedToken(token.substring(7)));
			User user = userService.forgetPassword(data.getString("account"));
			if (user != null) {
				User change = userService.changePassword(user, body.getString("password"));
				if (change != null) {
					response.put("success", "密碼更改成功");
					return response.toString();
				}
			}
			response.put("error", "密碼更改失敗");
			return response.toString();
		}
		response.put("error", "密碼更改失敗");
		return response.toString();
	}

}
