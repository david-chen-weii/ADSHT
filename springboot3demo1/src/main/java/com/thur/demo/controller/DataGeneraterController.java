package com.thur.demo.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thur.demo.model.post.Post;
import com.thur.demo.model.post.PostComment;
import com.thur.demo.model.post.PostCommentRepository;
import com.thur.demo.model.post.SubCommentRepository;
import com.thur.demo.model.user.User;
import com.thur.demo.service.FriendService;
import com.thur.demo.service.PostCommentService;
import com.thur.demo.service.PostService;
import com.thur.demo.service.SubCommentService;
import com.thur.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequestMapping("/ajax/data-generater")
public class DataGeneraterController {
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    PostCommentService postCommentService;
    @Autowired
    SubCommentService subCommentService;
    @Autowired
    FriendService friendService;
    @Autowired
    SubCommentRepository subCommentRepository;
    @Autowired
    PostCommentRepository postCommentRepository;

    String[] userList = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N" };
    int r = 0;
    int r1 = 0;
    Map<String, String> map = new HashMap<>();

    @GetMapping("/GO")
    public String getMethodName(HttpSession session) {
        userBuilder(session);
        postBuilder(session);
        commentBuilder(session);
        subCommentBuilder(session);

        return "All done";
    }

    @GetMapping("/user")
    public String userBuilder(HttpSession session) {
        for (String s : userList) {
            map.put("userName", s + "@gmail.com");
            map.put("password", s);
            map.put("lastName", s + 123);
            JSONObject data = new JSONObject(map);
            System.out.println(data);

            userService.addUsers(data, session);
        }
        return "user Done";
    }

    @GetMapping("/post")
    public String postBuilder(HttpSession session) {

        for (int i = 0; i < 100; i++) {
            r = (int) (Math.random() * userList.length);
            map.put("userName", userList[r] + "@gmail.com");
            map.put("password", userList[r]);
            map.put("content", userList[r] + i);
            map.put("permission", String.valueOf(r));

            JSONObject data = new JSONObject(map);
            User user = userService.login(data, session);
            if (user != null) {
                postService.addPost(data, user, session);
            } else {
                System.out.println("user 錯誤");
            }

        }
        return "post done";
    }

    @GetMapping("firend")
    public String friendBuilder(HttpSession session) {

        for (int i = 0; i < userList.length; i++) {

        }

        return "friend done";
    }

    @GetMapping("/comment")
    public String commentBuilder(HttpSession session) {
        for (int i = 0; i < userList.length; i++) {
            r = (int) (Math.random() * 20);
            for (int j = 0; j < r; j++) {
                map.put("userName", userList[i] + "@gmail.com");
                map.put("password", userList[i]);
                map.put("content", userList[i] + " comment" + j);
                JSONObject data = new JSONObject(map);
                User user = userService.login(data, session);

                r1 = (int) (Math.random() * postCommentRepository.count());
                Post post = postService.findOnePost(r1);
                postCommentService.addComment(data, post, user);
            }

        }
        return "comment done";
    }

    @GetMapping("/subComment")
    public String subCommentBuilder(HttpSession session) {
        for (int i = 0; i < userList.length; i++) {
            r = (int) (Math.random() * 20);
            for (int j = 0; j < r; j++) {
                map.put("userName", userList[i] + "@gmail.com");
                map.put("password", userList[i]);
                map.put("content", userList[i] + " subComment" + j);
                JSONObject data = new JSONObject(map);
                User user = userService.login(data, session);

                r1 = (int) (Math.random() * subCommentRepository.count());
                PostComment postComment = postCommentService.findOnePostCommentById(r1);
                subCommentService.addSubComment(data, postComment, user);
            }

        }
        return "subComment done";
    }

}
