package com.thur.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thur.demo.model.friend.Friend;
import com.thur.demo.model.friend.FriendId;
import com.thur.demo.model.friend.FriendRepository;
import com.thur.demo.model.post.Post;
import com.thur.demo.model.post.PostRepository;
import com.thur.demo.model.user.User;
import com.thur.demo.model.user.UserInfo;
import com.thur.demo.model.user.UserInfoRepository;
import com.thur.demo.model.user.UserRepository;

@Service
public class FriendService {

	@Autowired
	private FriendRepository friendRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private UserInfoRepository userInfoRepository;

	public List<UserInfo> findUserInfoByName(String name) {
		// 去除名稱兩端的空格並拆分名稱部分
		String trimmedName = name.trim();
		String[] parts = trimmedName.split("\\s+");
		List<UserInfo> userInfos;

		if (parts.length > 1) {
			String firstName = parts[0];
			String lastName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
			userInfos = userInfoRepository.findByUserFirstNameAndUserLastNameContaining(firstName, lastName,
					trimmedName, name);
		} else {
			// 嘗試尋找合併後的名稱
			userInfos = userInfoRepository.findByUserFirstNameAndUserLastNameContaining(trimmedName, "", trimmedName,
					name);
			if (userInfos.isEmpty()) {
				// 嘗試拆分名稱並分別查找
				for (int i = 1; i < trimmedName.length(); i++) {
					String firstName = trimmedName.substring(0, i);
					String lastName = trimmedName.substring(i);
					userInfos = userInfoRepository.findByUserFirstNameAndUserLastNameContaining(firstName, lastName,
							trimmedName, name);
					if (!userInfos.isEmpty()) {
						break;
					}
				}
			}
		}

		for (UserInfo userInfo : userInfos) {
			if (userInfo.getUser() != null) {
				System.out.println("查詢到的使用者: " + userInfo.getUserFirstName() + " " + userInfo.getUserLastName());
				System.out.println("查詢到的使用者id: " + userInfo.getUser().getUserId());
			} else {
				System.out.println(
						"查詢到的 UserInfo 但沒有關聯的 User: " + userInfo.getUserFirstName() + " " + userInfo.getUserLastName());
			}
		}
		return userInfos;
	}

	@Transactional
	public Friend addFriend(UUID senderId, UUID receiverId, Integer state) {
		// 檢查是否已經存在該朋友關係
		FriendId friendId = new FriendId(senderId, receiverId);
		if (friendRepository.existsById(friendId)) {
			throw new RuntimeException("朋友關係已經存在");
		}

		// 檢查是否添加自己為好友
		if (senderId.equals(receiverId)) {
			throw new RuntimeException("不能添加自己為好友");
		}

		User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("沒有該使用者"));
		User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("沒有該使用者"));

		Friend friend = new Friend(friendId, state);
		friend.setSender(sender);
		friend.setReceiver(receiver);

		// 創建通知
        notificationService.createNotification(
        		receiverId, 
        		senderId, 
        		"New Friend Request", "You have a new friend request from " +  
        		sender.getUserinfo().getUserLastName(), receiverId.toString());

		return friendRepository.save(friend);
	}

	public List<Friend> getAllFriends() {
		return friendRepository.findAll();
	}

	public List<Friend> getFriends(UUID userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		List<Friend> friends = new ArrayList<>();
		friends.addAll(user.getSentFriends());
		friends.addAll(user.getReceivedFriends());
		return friends;
	}

	@Transactional
	public void deleteFriend(FriendId friendId) {
		if (friendRepository.existsById(friendId)) {
			friendRepository.deleteById(friendId);
		} else {
			throw new RuntimeException("朋友關係不存在");
		}
	}

	@Transactional
	public Friend saveOrUpdateFriend(Friend friend) {
		FriendId friendId = friend.getId();
		Friend existingFriend = friendRepository.findById(friendId)
				.orElseThrow(() -> new RuntimeException("Friend not found"));
		existingFriend.setState(friend.getState());
		return friendRepository.save(existingFriend);
	}

	// 查詢朋友
	public User findByUsername(String username) {
		return userRepository.findByuserAccount(username);
	}

	// 查詢朋友
	public List<Friend> findFriendsByUserId(UUID userId) {
		return friendRepository.findByIdSendIdOrIdReceiveId(userId, userId);
	}

	// 確認朋友關係(給聊天室使用)
	public int checkRelationshipStatus(UUID user1, UUID user2) {
		boolean user1FollowsUser2 = friendRepository.existsById(new FriendId(user1, user2));
		boolean user2FollowsUser1 = friendRepository.existsById(new FriendId(user2, user1));

		if (user1FollowsUser2 && user2FollowsUser1) {
			return 3; // 雙向追蹤關係
		} else if (user1FollowsUser2 || user2FollowsUser1) {
			return 2; // 單向追蹤關係
		} else {
			return 1; // 不是追蹤關係
		}
	}

	// 找尋共同朋友(已經為好友狀態)
	public List<UUID> getCommonFriends(UUID userId1, UUID userId2) {
		return friendRepository.findCommonFriends(userId1, userId2);
	}

	// 推薦朋友(不是好友但有共同朋友)
    public List<UUID> getFriendRecommendations(UUID userId) {
        return friendRepository.findRecommendedFriends(userId);
    }

    public UserInfo getUserInfoByUserId(UUID userId) {
        return userInfoService.getUserInfoByUserId(userId);
    }
	
	//給個人頁面(Email)
    public List<UserInfo> findUserInfoByEmail(String email) {
        return userInfoRepository.findByEmail(email);
    }
    
    //給個人頁面(id)
    public List<UserInfo> findUserInfoById(Integer id) {
        return friendRepository.findById(id);
    }
    
    // 查詢追蹤者數量 (關注)
    public Long getFollowersCount(UUID userId) {
        return friendRepository.countFollowers(userId);
    }

    // 查詢追蹤自身數量 (粉絲)
    public Long getFollowingCount(UUID userId) {
        return friendRepository.countFollowing(userId);
    }
    
    // 查詢粉絲名單
    public List<Friend> getFollowers(UUID receiveId) {
        return friendRepository.findFollowers(receiveId);
    }

    // 查詢關注名單
    public List<Friend> getFollowing(UUID sendId) {
        return friendRepository.findFollowing(sendId);
    }
    
    //找用戶貼文
    public List<Post> findPostsByUserId(UUID userId, boolean isAsc) {
        if (isAsc) {
            return friendRepository.findPostsByIdAsc(userId);
        } else {
            return friendRepository.findPostsByIdDesc(userId);
        }
    }
    
    // 查詢貼文數量
    public Long getPostCount(UUID userId) {
        return friendRepository.countPost(userId);
    }
    
    public Integer getFriendState(UUID senderId, UUID receiverId) {
        FriendId friendId = new FriendId(senderId, receiverId);
        return friendRepository.findById(friendId)
                .map(Friend::getState)
                .orElse(0); // 如果沒有找到朋友關係，返回0表示沒有關係
    }

}
