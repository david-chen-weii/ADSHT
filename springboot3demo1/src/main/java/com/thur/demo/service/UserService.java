package com.thur.demo.service;

import java.util.Optional;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thur.demo.model.user.AccountLockedException;
import com.thur.demo.model.user.User;
import com.thur.demo.model.user.UserInfo;
import com.thur.demo.model.user.UserInfoRepository;
import com.thur.demo.model.user.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private PasswordEncoder pwdEncoder;

	// 檢查帳戶存在
	public boolean chackIfUserNameExist(String userAccount) {

		if (userRepository.findByuserAccount(userAccount) != null) {
			return true;
		}
		return false;
	}

	// 註冊

	public User addUsers(JSONObject data, HttpSession session) {
		// 帳戶資訊
		UserInfo userInfo = new UserInfo();
		userInfo.setUserLastName(data.getString("lastName"));
		userInfo.setEmail(data.getString("userName"));
		userInfo = userInfoRepository.save(userInfo);
		// 帳戶
		User user = new User();
		userInfo.setUser(user);
		user.setUserinfo(userInfo);
		user.setUserAccount(data.getString("userName"));
		user.setUserPassword(pwdEncoder.encode(data.getString("password")));
		// 設定Session
		session.setAttribute("loginId", user.getUserId());
		userRepository.save(user);
		userInfoRepository.save(userInfo);
		return user;

	}

	// 使用者登入 資料庫抓帳號 在對密碼 目前密碼已經加密過了! 在密碼的部分可以改成byte 做資料定義 方便作加密
	public User login(JSONObject data, HttpSession session) {

		User user = userRepository.findByuserAccount(data.getString("userName"));
		if (user != null) {
			if (user.getUserinfo().getStatus() == 0) {
				// 返回或抛出账号被封锁的错误消息
				throw new AccountLockedException("此帳號已被封鎖");
			}
			if (pwdEncoder.matches(data.getString("password"), user.getUserPassword())) {
				// 設定Session
				session.setAttribute("loginId", user.getUserId());
				userRepository.save(user);
				return user;
			}
		}

		return null;
	}

	public void logout(HttpSession session) {
		session.invalidate();
	}

	public User authorizationCheck(HttpSession session) {
		UUID userId = (UUID) session.getAttribute("loginId");
		if (userId == null) {
			return null;
		}
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) {
			return null;
		}

		return user.get();
	}

	public Optional<User> findById(UUID id) {
		return userRepository.findById(id);
	}

	public User forgetPassword(String mail) {

		return userRepository.findByuserAccount(mail);
	}

	public User changePassword(User user, String newPassword) {
		user.setUserPassword(pwdEncoder.encode(newPassword));
		return userRepository.save(user);

	}
}
