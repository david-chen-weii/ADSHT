package com.thur.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thur.demo.model.friend.Friend;
import com.thur.demo.model.friend.FriendId;
import com.thur.demo.model.post.Post;
import com.thur.demo.model.user.User;
import com.thur.demo.model.user.UserInfo;
import com.thur.demo.service.FriendService;
import com.thur.demo.service.UserService;
import com.thur.demo.util.DefaultIcon;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/friends")
@CrossOrigin
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userService;

    @Autowired
    private DefaultIcon defaultIcon;

    // 查詢使用者資料
    @GetMapping("/searchUser")
    public Map<String, Object> searchUser(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("接收到查詢參數: " + name);

            List<UserInfo> userInfos = friendService.findUserInfoByName(name);
            if (userInfos.isEmpty()) {
                response.put("success", false);
                response.put("message", "找不到符合的用戶");
                return response;
            }

            List<Map<String, Object>> userInfoList = new ArrayList<>();
            for (UserInfo userInfo : userInfos) {
                Map<String, Object> userInfoMap = new HashMap<>();
                userInfoMap.put("id", userInfo.getId());
                userInfoMap.put("birth", userInfo.getBirth());
                userInfoMap.put("email", userInfo.getEmail());
                userInfoMap.put("gender", userInfo.getGender());
                userInfoMap.put("phone", userInfo.getPhone());
                userInfoMap.put("photo", userInfo.getPhoto());
                userInfoMap.put("selfContent", userInfo.getSelfContent());
                userInfoMap.put("userFirstName", userInfo.getUserFirstName());
                userInfoMap.put("userLastName", userInfo.getUserLastName());
                if (userInfo.getUser() != null) {
                    userInfoMap.put("userId", userInfo.getUser().getUserId());
                }
                userInfoList.add(userInfoMap);
            }

            response.put("success", true);
            response.put("userInfos", userInfoList);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
    }

    // 新增朋友
    // http://localhost:8080/api/friends/add
    @PostMapping("/add")
    public String addFriend(@RequestBody String body) {
        JSONObject responseBody = new JSONObject();
        JSONObject obj = new JSONObject(body);

        String senderIdStr = obj.optString("senderId", null);
        String receiverIdStr = obj.optString("receiverId", null);
        String stateStr = obj.optString("state", "1");// 默認 state 為 1

        if (senderIdStr == null || senderIdStr.isEmpty() || receiverIdStr == null || receiverIdStr.isEmpty()
                || stateStr.isEmpty()) {
            responseBody.put("success", false);
            responseBody.put("message", "senderId, receiverId 和 state 是必要欄位，且不能為空");
            return responseBody.toString();
        }

        try {
            UUID senderId = UUID.fromString(senderIdStr);
            UUID receiverId = UUID.fromString(receiverIdStr);
            Integer state = Integer.parseInt(stateStr); // 將 state 字符串轉換為整數

            // 檢查 sender 和 receiver 是否存在
            User sender = userService.findById(senderId).orElseThrow(() -> new RuntimeException("沒有該使用者"));
            User receiver = userService.findById(receiverId).orElseThrow(() -> new RuntimeException("沒有該使用者"));

            Friend friend = friendService.addFriend(senderId, receiverId, state);
            responseBody.put("success", true);
            responseBody.put("friend", "新增成功");
        } catch (IllegalArgumentException e) {
            responseBody.put("success", false);
            responseBody.put("message", "無效的UUID或state格式");
        } catch (RuntimeException e) {
            responseBody.put("success", false);
            responseBody.put("message", e.getMessage());
        }

        return responseBody.toString();
    }

    // 獲取所有朋友
    // http://localhost:8080/api/friends/getAllFriends
    @GetMapping("/getAllFriends")
    public List<Map<String, Object>> getAllFriends() {
        List<Friend> friends = friendService.getAllFriends();
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Friend friend : friends) {
            Map<String, Object> friendMap = new HashMap<>();

            friendMap.put("sendId", friend.getId().getSendId());
            friendMap.put("receiveId", friend.getId().getReceiveId());
            friendMap.put("state", friend.getState());
            friendMap.put("createTime", friend.getCreateTime());
            friendMap.put("senderuserAccount", friend.getSender().getUserAccount());
            friendMap.put("receiveruserAccount", friend.getReceiver().getUserAccount());

            responseList.add(friendMap);
        }

        return responseList;
    }

    // 更新朋友
    // http://localhost:8080/api/friends/update
    @PutMapping("/update")
    public String updateFriend(@RequestBody String body) {
        JSONObject responseBody = new JSONObject();
        JSONObject obj = new JSONObject(body);

        JSONObject idObj = obj.optJSONObject("id");
        String senderIdStr = idObj != null ? idObj.optString("sendId", null) : null;
        String receiverIdStr = idObj != null ? idObj.optString("receiveId", null) : null;
        String stateStr = obj.optString("state", null);

        if (senderIdStr == null || senderIdStr.isEmpty() || receiverIdStr == null || receiverIdStr.isEmpty()
                || receiverIdStr == null || stateStr == "") {
            responseBody.put("success", false);
            responseBody.put("message", "senderId, receiverId 和 state 是必要欄位，且不能為空");
            return responseBody.toString();
        }

        try {
            UUID senderId = UUID.fromString(senderIdStr);
            UUID receiverId = UUID.fromString(receiverIdStr);
            Integer state = Integer.parseInt(stateStr);

            // 檢查 sender 和 receiver 是否存在
            User sender = userService.findById(senderId).orElseThrow(() -> new RuntimeException("沒有該使用者"));
            User receiver = userService.findById(receiverId).orElseThrow(() -> new RuntimeException("沒有該使用者"));

            FriendId friendId = new FriendId(senderId, receiverId);
            Friend friend = new Friend(friendId, state);
            friendService.saveOrUpdateFriend(friend);
            responseBody.put("success", true);
            responseBody.put("message", "更新成功");
        } catch (IllegalArgumentException e) {
            responseBody.put("success", false);
            responseBody.put("message", "無效的 UUID、state  格式");
        } catch (RuntimeException e) {
            responseBody.put("success", false);
            responseBody.put("message", e.getMessage());
        }

        return responseBody.toString();
    }

    // 刪除朋友
    // http://localhost:8080/api/friends/delete
    @DeleteMapping("/delete")
    public String deleteFriend(@RequestBody String body) {
        JSONObject responseBody = new JSONObject();
        JSONObject obj = new JSONObject(body);

        String senderIdStr = obj.optString("senderId", null);
        String receiverIdStr = obj.optString("receiverId", null);

        if (senderIdStr == null || senderIdStr.isEmpty() || receiverIdStr == null || receiverIdStr.isEmpty()) {
            responseBody.put("success", false);
            responseBody.put("message", "senderId 和 receiverId 是必要欄位，且不能為空");
            return responseBody.toString();
        }

        try {
            UUID senderId = UUID.fromString(senderIdStr);
            UUID receiverId = UUID.fromString(receiverIdStr);

            // 檢查 sender 和 receiver 是否存在
            User sender = userService.findById(senderId).orElseThrow(() -> new RuntimeException("沒有該使用者"));
            User receiver = userService.findById(receiverId).orElseThrow(() -> new RuntimeException("沒有該使用者"));

            FriendId friendId = new FriendId(senderId, receiverId);
            friendService.deleteFriend(friendId);
            responseBody.put("success", true);
            responseBody.put("message", "删除成功");
        } catch (IllegalArgumentException e) {
            responseBody.put("success", false);
            responseBody.put("message", "無效的 UUID 格式");
        } catch (RuntimeException e) {
            responseBody.put("success", false);
            responseBody.put("message", e.getMessage());
        }

        return responseBody.toString();
    }

    // 查詢朋友(輸入username查對應所有朋友)
    @PostMapping("/findFriendsByUsername")
    public Map<String, Object> findFriendsByUsername(@RequestBody String body) {
        JSONObject obj = new JSONObject(body);
        String username = obj.optString("username", null);

        Map<String, Object> response = new HashMap<>();

        if (username == null || username.isEmpty()) {
            response.put("success", false);
            response.put("message", "username 是必要欄位，且不能為空");
            return response;
        }

        try {
            User user = friendService.findByUsername(username);
            if (user == null) {
                response.put("success", false);
                response.put("message", "找不到該用戶");
                return response;
            }

            UUID userId = user.getUserId();
            List<Friend> friends = friendService.findFriendsByUserId(userId);

            List<Map<String, Object>> friendMaps = new ArrayList<>();
            for (Friend friend : friends) {
                Map<String, Object> friendMap = new HashMap<>();
                friendMap.put("sendId", friend.getId().getSendId());
                friendMap.put("receiveId", friend.getId().getReceiveId());
                friendMap.put("state", friend.getState());
                friendMap.put("createTime", friend.getCreateTime());
                friendMap.put("senderUsername", friend.getSender().getUserAccount());
                friendMap.put("receiverUsername", friend.getReceiver().getUserAccount());
                friendMaps.add(friendMap);
            }

            response.put("success", true);
            response.put("friends", friendMaps);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
    }

    // 確認朋友關係(給聊天室使用)
    @PostMapping("/checkRelationship")
    public Map<String, Object> checkRelationship(@RequestBody String body) {
        JSONObject obj = new JSONObject(body);
        String user1IdStr = obj.optString("user1Id", null);
        String user2IdStr = obj.optString("user2Id", null);

        Map<String, Object> response = new HashMap<>();

        if (user1IdStr == null || user1IdStr.isEmpty() || user2IdStr == null || user2IdStr.isEmpty()) {
            response.put("success", false);
            response.put("message", "user1Id 和 user2Id 是必要欄位，且不能為空");
            return response;
        }

        try {
            UUID user1Id = UUID.fromString(user1IdStr);
            UUID user2Id = UUID.fromString(user2IdStr);

            int status = friendService.checkRelationshipStatus(user1Id, user2Id);

            response.put("success", true);
            response.put("status", status); // 確保這裡返回正確的狀態
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", "無效的 UUID 格式");
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
    }

    // 找尋共同朋友(已經為好友狀態)
    @PostMapping("/common")
    public List<UUID> getCommonFriends(@RequestBody String body) {
        JSONObject obj = new JSONObject(body);
        UUID userId1 = UUID.fromString(obj.getString("userId"));
        UUID userId2 = UUID.fromString(obj.getString("otherUserId"));
        System.out.println(userId1);
        System.out.println(userId2);
        return friendService.getCommonFriends(userId1, userId2);
    }

    // 推薦朋友 (不是好友但有共同朋友)
    @PostMapping("/recommendations")
    public List<Map<String, Object>> getFriendRecommendations(@RequestBody String body) {
        JSONObject obj = new JSONObject(body);
        UUID userId = UUID.fromString(obj.getString("userId"));
        List<UUID> recommendedFriendIds = friendService.getFriendRecommendations(userId);

        List<Map<String, Object>> recommendedFriends = new ArrayList<>();
        for (UUID friendId : recommendedFriendIds) {
            UserInfo userInfo = friendService.getUserInfoByUserId(friendId);
            if (userInfo != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", userInfo.getId());
                item.put("userId", friendId.toString());
                item.put("userFirstName", userInfo.getUserFirstName());
                item.put("userLastName", userInfo.getUserLastName());
                item.put("email", userInfo.getEmail());
                item.put("phone", userInfo.getPhone());
                item.put("photo", userInfo.getPhoto());
                recommendedFriends.add(item);
            }
        }

        return recommendedFriends;
    }

    // 個人頁面
    @PostMapping("/searchByEmail")
    public Map<String, Object> searchByEmail(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = request.get("email");
            List<UserInfo> userInfos = friendService.findUserInfoByEmail(email);
            if (userInfos.isEmpty()) {
                response.put("success", false);
                response.put("message", "找不到符合的用戶");
                return response;
            }

            List<Map<String, Object>> userInfoList = new ArrayList<>();
            for (UserInfo userInfo : userInfos) {
                Map<String, Object> userInfoMap = new HashMap<>();
                userInfoMap.put("userLastName", userInfo.getUserLastName());
                userInfoMap.put("photo", userInfo.getPhoto());
                userInfoMap.put("userFirstName", userInfo.getUserFirstName());
                userInfoMap.put("userId", userInfo.getUser().getUserId());
                userInfoMap.put("email", userInfo.getEmail());
                userInfoList.add(userInfoMap);
            }

            response.put("success", true);
            response.put("userInfos", userInfoList);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
    }

    // 個人頁面
    @PostMapping("/searchById")
    public Map<String, Object> searchById(@RequestBody Map<String, Integer> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer id = request.get("id");
            if (id == null) {
                response.put("success", false);
                response.put("message", "請提供有效的用戶 ID");
                return response;
            }

            List<UserInfo> userInfos = friendService.findUserInfoById(id);
            if (userInfos.isEmpty()) {
                response.put("success", false);
                response.put("message", "找不到符合的用戶");
                return response;
            }

            List<Map<String, Object>> userInfoList = new ArrayList<>();
            for (UserInfo userInfo : userInfos) {
                Map<String, Object> userInfoMap = new HashMap<>();
                userInfoMap.put("id", userInfo.getId());
                userInfoMap.put("birth", userInfo.getBirth());
                userInfoMap.put("email", userInfo.getEmail());
                userInfoMap.put("gender", userInfo.getGender());
                userInfoMap.put("phone", userInfo.getPhone());
                userInfoMap.put("photo", userInfo.getPhoto());
                userInfoMap.put("selfContent", userInfo.getSelfContent());
                userInfoMap.put("userFirstName", userInfo.getUserFirstName());
                userInfoMap.put("userLastName", userInfo.getUserLastName());
                userInfoMap.put("userId", userInfo.getUser().getUserId());

                userInfoList.add(userInfoMap);
            }

            response.put("success", true);
            response.put("userInfos", userInfoList);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
    }

    // 查詢追蹤者數量 (關注) 輸入sendId
    @PostMapping("/followers/count")
    public Long getFollowersCount(@RequestBody Map<String, String> request) {
        UUID userId = UUID.fromString(request.get("userId"));
        return friendService.getFollowersCount(userId);
    }

    // 查詢追蹤自身數量 (粉絲) 輸入receiveId
    @PostMapping("/following/count")
    public Long getFollowingCount(@RequestBody Map<String, String> request) {
        UUID userId = UUID.fromString(request.get("userId"));
        return friendService.getFollowingCount(userId);
    }

    // 查詢粉絲名單
    @PostMapping("/followers")
    public List<Map<String, Object>> getFollowers(@RequestBody Map<String, String> request) {
        UUID receiveId = UUID.fromString(request.get("receiveId"));
        List<Friend> followers = friendService.getFollowers(receiveId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Friend follower : followers) {
            Map<String, Object> followerInfo = new HashMap<>();
            User sender = follower.getSender();
            UserInfo senderInfo = sender.getUserinfo();

            followerInfo.put("sendId", follower.getId().getSendId());
            followerInfo.put("receiveId", follower.getId().getReceiveId());
            followerInfo.put("state", follower.getState());

            followerInfo.put("userId", sender.getUserId());
            if (senderInfo != null) {
                followerInfo.put("id", senderInfo.getId());
                followerInfo.put("userFirstName", senderInfo.getUserFirstName());
                followerInfo.put("userLastName", senderInfo.getUserLastName());
                followerInfo.put("email", senderInfo.getEmail());
                followerInfo.put("photo", senderInfo.getPhoto());
            } else {
                followerInfo.put("id", null);
                followerInfo.put("userFirstName", null);
                followerInfo.put("userLastName", null);
                followerInfo.put("email", null);
                followerInfo.put("photo", null);
            }

            result.add(followerInfo);
        }

        return result;
    }

    // 查詢關注名單
    @PostMapping("/following")
    public List<Map<String, Object>> getFollowing(@RequestBody Map<String, String> request) {
        UUID sendId = UUID.fromString(request.get("sendId"));
        List<Friend> following = friendService.getFollowing(sendId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Friend follow : following) {
            Map<String, Object> followInfo = new HashMap<>();
            User receiver = follow.getReceiver();
            UserInfo receiverInfo = receiver.getUserinfo();

            followInfo.put("sendId", follow.getId().getSendId());
            followInfo.put("receiveId", follow.getId().getReceiveId());
            followInfo.put("state", follow.getState());

            followInfo.put("userId", receiver.getUserId());
            if (receiverInfo != null) {
                followInfo.put("id", receiverInfo.getId());
                followInfo.put("userFirstName", receiverInfo.getUserFirstName());
                followInfo.put("userLastName", receiverInfo.getUserLastName());
                followInfo.put("email", receiverInfo.getEmail());
                followInfo.put("photo", receiverInfo.getPhoto());
            } else {
                followInfo.put("id", null);
                followInfo.put("userFirstName", null);
                followInfo.put("userLastName", null);
                followInfo.put("email", null);
                followInfo.put("photo", null);
            }

            result.add(followInfo);
        }

        return result;
    }

    // 找用戶貼文
    @PostMapping("/userPostsFind")
    public String findPostsByUser(@RequestBody String requestBody, HttpSession session,
            @RequestHeader("session-id") UUID id) {
        JSONObject response = new JSONObject();
        JSONArray items = new JSONArray();

        // 驗證用戶
        Optional<User> userCheck = userService.findById(id);
        if (userCheck.isEmpty()) {
            response.put("error", "沒有權限");
            return response.toString();
        }

        boolean asc = false;
        UUID userId = id; // 假設你要查詢當前用戶的貼文
        if (requestBody != null && !requestBody.isEmpty()) {
            JSONObject request = new JSONObject(requestBody);
            if (request.has("asc")) {
                asc = request.getBoolean("asc");
            }
            if (request.has("userId")) {
                userId = UUID.fromString(request.getString("userId"));
            }
        }

        List<Post> posts = friendService.findPostsByUserId(userId, asc);
        for (Post post : posts) {
            String photo = post.getPoster().getUserinfo().getPhoto();
            if (photo == null || photo.length() < 10) {
                photo = defaultIcon.defaultIcon();
            }
            JSONObject item = new JSONObject();
            String Fname = post.getPoster().getUserinfo().getUserFirstName() == null ? ""
                    : post.getPoster().getUserinfo().getUserFirstName();
            String Lname = post.getPoster().getUserinfo().getUserLastName() == null ? ""
                    : post.getPoster().getUserinfo().getUserLastName();
            item.put("postId", post.getId())
                    .put("content", post.getContent())
                    .put("image", post.getImage())
                    .put("likeCount", post.getLikeCount())
                    .put("addTime", post.getAddTime())
                    .put("posterId", post.getPoster().getUserId())
                    .put("posterName", Fname + " " + Lname)
                    .put("posterInfoId", post.getPoster().getUserinfo().getId())
                    .put("permission", post.getPermission())
                    .put("commentNum", post.getComment().size())
                    .put("posterPhoto", photo);
            items.put(item);
        }
        response.put("posts", items);
        return response.toString();
    }

    // 找照片
    @PostMapping("/searchPhotoById")
    public Map<String, Object> searchPhotoById(@RequestBody Map<String, Integer> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer id = request.get("id");
            if (id == null) {
                response.put("success", false);
                response.put("message", "請提供有效的用戶 ID");
                return response;
            }

            List<UserInfo> userInfos = friendService.findUserInfoById(id);
            if (userInfos.isEmpty()) {
                response.put("success", false);
                response.put("message", "找不到符合的用戶");
                return response;
            }

            List<Map<String, Object>> userInfoList = new ArrayList<>();
            for (UserInfo userInfo : userInfos) {
                Map<String, Object> userInfoMap = new HashMap<>();
                userInfoMap.put("id", userInfo.getId());
                userInfoMap.put("photo", userInfo.getPhoto());
                userInfoList.add(userInfoMap);
            }

            response.put("success", true);
            response.put("userInfos", userInfoList);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
    }
    
    // 查詢查詢貼文數量 輸入poster
    @PostMapping("/post/count")
    public Long getPostCount(@RequestBody Map<String, String> request) {
        UUID poster = UUID.fromString(request.get("userId"));
        return friendService.getPostCount(poster);
    }
    
    
    @PostMapping("/state")
    public ResponseEntity<?> getFriendState(@RequestBody Map<String, String> body) {
        String senderIdStr = body.get("sendId");
        String receiverIdStr = body.get("receiveId");

        if (senderIdStr == null || senderIdStr.isEmpty() || receiverIdStr == null || receiverIdStr.isEmpty()) {
            return ResponseEntity.badRequest().body("sendId 和 receiveId 是必要欄位，且不能為空");
        }

        try {
            UUID senderId = UUID.fromString(senderIdStr);
            UUID receiverId = UUID.fromString(receiverIdStr);
            Integer state = friendService.getFriendState(senderId, receiverId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("state", state);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("無效的 UUID 格式");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
