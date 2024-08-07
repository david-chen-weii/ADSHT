package com.thur.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thur.demo.model.post.Post;
import com.thur.demo.model.post.PostComment;
import com.thur.demo.model.user.User;
import com.thur.demo.service.PostService;
import com.thur.demo.service.UserService;
import com.thur.demo.util.DefaultIcon;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequestMapping("/ajax/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private DefaultIcon defaultIcon;

    @PostMapping("/newPost")
    public String addNewPost(@RequestBody String body, HttpSession session, @RequestHeader("session-id") UUID id) {
        JSONObject responseBody = new JSONObject();
        // User user = userService.authorizationCheck(session);
        System.out.println(id);
        Optional<User> userCheck = userService.findById(id);
        if (userCheck.isEmpty()) {
            // 未登入
            responseBody.put("error", "沒有權限");
            return responseBody.toString();
        }
        User user = userCheck.get();
        // 處理資訊

        JSONObject newPost = new JSONObject(body);
        if (newPost.getString("content") == "") {
            responseBody.put("error", "訊息不可為空");
            return responseBody.toString();
        }

        Post post = postService.addPost(newPost, user, session);

        if (post == null) {
            responseBody.put("error", "意外錯誤");
            return responseBody.toString();
        }

        responseBody.put("success", "OK")
                .put("postId", post.getId());

        return responseBody.toString();
    }

    @DeleteMapping("/deletePost/{postId}")
    public String postDelete(@PathVariable int postId, HttpSession session, @RequestHeader("session-id") UUID id) {
        JSONObject response = new JSONObject();
        // 获取当前用户
        User user = userService.findById(id).orElse(null);
        if (user == null) {
            response.put("error", "用戶未登錄");
            return response.toString();
        }

        // 获取帖子
        Post post = postService.findOnePost(postId);
        if (post == null) {
            response.put("error", "貼文不存在");
            return response.toString();
        }

        // 假查權限
        if (user.getUserinfo().getStatus() == 2 || post.getPoster().getUserId() == user.getUserId()) {
            // 超级用户或者帖子拥有者可以删除帖子
            postService.deletePost(post);
            response.put("success", "删除成功");
        } else {
            // 無權限
            response.put("error", "没有權限");
        }

        return response.toString();
    }

    @PutMapping("editPost/{postId}")
    public String putMethodName(@PathVariable int postId, @RequestBody String request, HttpSession session,
            @RequestHeader("session-id") UUID id) {
        JSONObject response = new JSONObject();
        // 權限確認
        User user = userService.findById(id).get();
        Post post = postService.findOnePost(postId);
        if (user == null || post == null || post.getPoster().getUserId() != user.getUserId()) {
            response.put("error", "沒有權限");
            return response.toString();
        }
        // 修改
        JSONObject body = new JSONObject(request);
        if (body.getString("content") == null || body.getString("content") == "") {
            response.put("error", "內容不能為空");
            return response.toString();
        }

        post.setContent(body.getString("content"));
        try {
            post.setPermission(body.getInt("permission"));
        } catch (Exception e) {

        }
        try {
            post.setImage(body.getString("image"));
        } catch (Exception e) {

        }

        Post check = postService.editPost(post);
        if (check == null) {
            response.put("error", "Something go wrong");
            return response.toString();
        }
        response.put("success", "修改成功")
                .put("content", post.getContent())
                .put("image", post.getImage())
                .put("permission", post.getPermission())
                .put("postId", post.getId());
        return response.toString();
    }

    @PostMapping("/allPost")
    public String searchAllPost(@RequestBody String request) {
        JSONObject response = new JSONObject();
        JSONObject body = new JSONObject(request);

        List<Post> posts = postService.findAllPost(body.getInt("page"));

        JSONArray items = new JSONArray();
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

            ;
            items.put(item);
        }
        response.put("posts", items);
        return response.toString();
    }

    @PostMapping("/searchPost")
    public String searchPostbyWord(@RequestBody String keyWord) {
        JSONObject response = new JSONObject();
        JSONArray items = new JSONArray();
        List<Post> posts = postService.searchPostByWord(new JSONObject(keyWord).getString("sentence").split(" "), 1);
        for (Post post : posts) {
            String photo = post.getPoster().getUserinfo().getPhoto();
            if (photo == null || photo.length() < 10) {
                photo = defaultIcon.defaultIcon();
            }
            String Fname = post.getPoster().getUserinfo().getUserFirstName() == null ? ""
                    : post.getPoster().getUserinfo().getUserFirstName();
            String Lname = post.getPoster().getUserinfo().getUserLastName() == null ? ""
                    : post.getPoster().getUserinfo().getUserLastName();
            JSONObject item = new JSONObject();

            item.put("image", post.getImage())
                    .put("posterId", post.getPoster().getUserinfo().getId())
                    .put("content", post.getContent())
                    .put("permission", post.getPermission())
                    .put("posterName", Fname + " " + Lname)
                    .put("posterPhoto", photo)
                    .put("postId", post.getId());

            items.put(item);
        }
        response.put("posts", items);
        return response.toString();
    }

    @PostMapping("/searchFollowPost")
    public String searchFollowPost(@RequestBody String request, HttpSession session,
            @RequestHeader("Session-id") UUID id) {
        JSONObject response = new JSONObject();
        JSONArray items = new JSONArray();
        JSONObject body = new JSONObject(request);
        System.out.println(body);
        User user = userService.findById(id).get();

        List<Post> posts = postService.searchPostByFollower(user, body.getInt("page"));

        for (Post post : posts) {
            JSONObject item = new JSONObject();
            item.put("content", post.getContent())
                    .put("addTime", post.getAddTime())
                    .put("postId", post.getId())
                    .put("permission", post.getPermission())
                    .put("image", post.getImage())
                    .put("userId", post.getPoster().getUserId())
                    .put("userInfoId", post.getPoster().getUserinfo().getId())
                    .put("posterPhoto", post.getPoster().getUserinfo().getPhoto())
                    .put("commentNum", post.getComment().size());
            items.put(item);
        }

        response.put("posts", items);
        return response.toString();
    }

    @GetMapping("/findOnePost/{postId}")
    public String onePostAndComments(@PathVariable int postId) {

        JSONObject response = new JSONObject();
        JSONArray items = new JSONArray();
        JSONObject postData = new JSONObject();
        // Post

        Post post = postService.findOnePost(postId);
        String photo = post.getPoster().getUserinfo().getPhoto();
        if (photo == null || photo.length() < 10) {
            photo = defaultIcon.defaultIcon();
        }
        String Fname = post.getPoster().getUserinfo().getUserFirstName() == null ? ""
                : post.getPoster().getUserinfo().getUserFirstName();
        String Lname = post.getPoster().getUserinfo().getUserLastName() == null ? ""
                : post.getPoster().getUserinfo().getUserLastName();
        postData.put("content", post.getContent())
                .put("image", post.getImage())
                .put("likeCount", post.getLikeCount())
                .put("permission", post.getPermission())
                .put("addTime", post.getAddTime())
                .put("posterId", post.getPoster().getUserinfo().getId())
                .put("posterPhoto", photo)
                .put("posterName", Fname + " " + Lname)
                .put("postId",post.getId());
        // 加入response
        response.put("post", postData);
        // comment
        for (PostComment comment : post.getComment()) {
            String CFname = comment.getUser().getUserinfo().getUserFirstName() == null ? ""
                    : comment.getUser().getUserinfo().getUserFirstName();
            String CLname = comment.getUser().getUserinfo().getUserLastName() == null ? ""
                    : comment.getUser().getUserinfo().getUserLastName();
            JSONObject item = new JSONObject();
            String photoComment = comment.getUser().getUserinfo().getPhoto();
            if (photoComment == null || photoComment.length() < 10) {
                photoComment = defaultIcon.defaultIcon();
            }
            item.put("content", comment.getContent())
                    .put("commentId", comment.getId())
                    .put("subCommentSize", comment.getSubComment().size())
                    .put("addTime", comment.getAddTime())
                    .put("userInfoId", comment.getUser().getUserinfo().getId())
                    .put("posterPhoto", photoComment)
                    .put("posterName", CFname + " " + CLname);
            items.put(item);
        }
        // 加入response
        response.put("comments", items);
        return response.toString();
    }

}
