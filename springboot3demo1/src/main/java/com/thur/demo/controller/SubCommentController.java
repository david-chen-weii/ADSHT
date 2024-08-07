package com.thur.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thur.demo.model.post.Post;
import com.thur.demo.model.post.PostComment;
import com.thur.demo.model.post.PostRepository;
import com.thur.demo.model.post.SubComment;
import com.thur.demo.model.user.User;
import com.thur.demo.model.user.UserRepository;
import com.thur.demo.service.PostCommentService;
import com.thur.demo.service.SubCommentService;
import com.thur.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequestMapping("/ajax/comments")
public class SubCommentController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostCommentService postCommentService;
    @Autowired
    private SubCommentService subCommentService;
    @Autowired
    private UserService userService;

    @PostMapping("/newSubComment/{postId}/{commentId}")
    public String addNewComment(@PathVariable Integer postId, @PathVariable Integer commentId,
            @RequestBody String content, HttpSession session, @RequestHeader("session-id") UUID id) {

        JSONObject response = new JSONObject();
        // 確認內容
        JSONObject body = new JSONObject(content);
        if (body.getString("content") == null || body.getString("content").length() == 0) {
            response.put("error", "文章內容為空");
            return response.toString();
        }

        // 找使用者
        User user = userService.findById(id).get();
        if (user == null) {
            response.put("error", "沒有權限");
            return response.toString();
        }

        // 確認文章
        Optional<Post> postCheck = postRepository.findById(postId);
        if (postCheck.isEmpty()) {
            response.put("error", "文章不存在");
            return response.toString();
        }
        // 取得Post
        Post post = postCheck.get();

        // 取得留言
        PostComment postComment = postCommentService.findOnePostCommentById(commentId);
        if (postComment == null) {
            response.put("error", "留言不存在");
            return response.toString();
        }

        // 新增子留言
        SubComment subComment = subCommentService.addSubComment(body, postComment, user);
        if (subComment == null) {
            response.put("erroe", "意外錯誤");
            return response.toString();
        }
        response.put("success", "留言成功")
                .put("content", subComment.getContent())
                .put("subCommentId", subComment.getId())
                .put("userId", user.getUserId())
                .put("userInfoId", user.getUserinfo().getId());
        ;

        return response.toString();
    }

    @SuppressWarnings("null")
    @PutMapping("/editComment/{postId}/{commentId}/{subCommentId}")
    public String putMethodName(@PathVariable Integer postId, @PathVariable Integer commentId,
            @PathVariable Integer subCommentId,
            @RequestBody String content, HttpSession session, @RequestHeader("session-id") UUID id) {

        JSONObject response = new JSONObject();
        // 確認內容
        JSONObject body = new JSONObject(content);
        if (body.getString("content") == null || body.getString("content").length() == 0) {
            response.put("error", "文章內容為空");
            return response.toString();
        }

        // 找使用者
        User user = userService.findById(id).get();
        if (user == null) {
            response.put("error", "沒有權限");
            return response.toString();
        }

        // 確認文章
        Optional<Post> postCheck = postRepository.findById(postId);
        if (postCheck.isEmpty()) {
            response.put("error", "文章不存在");
            return response.toString();
        }
        // 取得Post
        Post post = (Post) postCheck.get();

        // 取得留言
        PostComment postComment = postCommentService.findOnePostCommentById(commentId);
        if (postComment == null) {
            response.put("error", "留言不存在");
            return response.toString();
        }

        // 修改子留言
        SubComment subComment = subCommentService.findOneSubComment(subCommentId);
        subComment.setContent(body.getString("content"));
        SubComment res = subCommentService.ediPostComment(subComment);
        if (res == null) {
            response.put("erroe", "意外錯誤");
            return response.toString();
        }
        response.put("success", "修改成功")
                .put("content", subComment.getContent())
                .put("subCommentId", subComment.getId())
                .put("userId", user.getUserId())
                .put("userInfoId", user.getUserinfo().getId());

        return response.toString();
    }

    @DeleteMapping("/delete/{postId}/{commentId}/{subCommentId}")
    public String deleteComment(@PathVariable Integer postId, @PathVariable Integer commentId,
            @PathVariable Integer subCommentId,
            @RequestBody String content, HttpSession session, @RequestHeader("session-id") UUID id) {

        JSONObject response = new JSONObject();
        // 確認內容
        JSONObject body = new JSONObject(content);
        if (body.getString("content") == null || body.getString("content").length() == 0) {
            response.put("error", "文章內容為空");
            return response.toString();
        }

        // 找使用者
        User user = userService.findById(id).get();
        if (user == null) {
            response.put("error", "沒有權限");
            return response.toString();
        }

        // 確認文章
        Optional<Post> postCheck = postRepository.findById(postId);
        if (postCheck.isEmpty()) {
            response.put("error", "文章不存在");
            return response.toString();
        }
        // 取得Post
        Post post = (Post) postCheck.get();

        // 取得留言
        PostComment postComment = postCommentService.findOnePostCommentById(commentId);
        if (postComment == null) {
            response.put("error", "留言不存在");
            return response.toString();
        }

        // 刪除子留言
        subCommentService.deleteCommentById(subCommentId);
        response.put("success", "刪除成功");
        return response.toString();
    }

    @PostMapping("/findSubComment/{commentId}")
    public String postMethodName(@PathVariable int commentId) {
        JSONObject response = new JSONObject();
        JSONArray items = new JSONArray();

        PostComment comment = postCommentService.findOnePostCommentById(commentId);
        if (comment == null) {
            response.put("error", "留言不存在");
        }

        List<SubComment> subComments = comment.getSubComment();

        for (SubComment subComment : subComments) {
            JSONObject item = new JSONObject();
            item.put("id", subComment.getId())
                    .put("content", subComment.getContent())
                    .put("addTime", subComment.getAddTime())
                    .put("userId", subComment.getUser().getUserId())
                    .put("userInfoId", subComment.getUser().getUserinfo().getId());
            items.put(item);
        }
        response.put("subComments", items);
        return response.toString();
    }

}
