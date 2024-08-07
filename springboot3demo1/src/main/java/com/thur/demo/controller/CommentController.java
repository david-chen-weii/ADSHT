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
import com.thur.demo.model.user.User;
import com.thur.demo.service.PostCommentService;
import com.thur.demo.service.PostService;
import com.thur.demo.service.SubCommentService;
import com.thur.demo.service.UserService;
import com.thur.demo.util.DefaultIcon;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequestMapping("/ajax/comments")
public class CommentController {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostCommentService postCommentService;
    @Autowired
    private SubCommentService subCommentService;
    @Autowired
    private UserService userService;
    @Autowired
    private DefaultIcon defaultIcon;

    @PostMapping("/newComment/{postId}")
    public String addNewComment(@PathVariable Integer postId, @RequestBody String content, HttpSession session,
            @RequestHeader("session-id") UUID id) {

        JSONObject response = new JSONObject();
        // 確認內容
        JSONObject body = new JSONObject(content);
        if (body.getString("content") == null || body.getString("content").length() == 0) {
            response.put("error", "文章內容為空");
            return response.toString();
        }

        // 確認文章
        Post post = postService.findOnePost(postId);
        if (post == null) {
            response.put("error", "文章不存在");
            return response.toString();
        }

        // 找使用者
        User user = userService.findById(id).get();
        if (user == null) {
            response.put("error", "沒有權限");
            return response.toString();
        }

        PostComment comment = postCommentService.addComment(body, post, user);
        if (comment == null) {
            response.put("erroe", "意外錯誤");
            return response.toString();
        }
        response.put("success", "留言成功")
                .put("content", comment.getContent())
                .put("commentId", comment.getId())
                .put("userId", user.getUserId())
                .put("userInfoId", user.getUserinfo().getId());

        return response.toString();
    }

    @SuppressWarnings("null")
    @PutMapping("/editComment/{postId}/{commentId}")
    public String putMethodName(@PathVariable int postId, @PathVariable int commentId, @RequestBody String request,
            HttpSession session, @RequestHeader("session-id") UUID id) {
        JSONObject response = new JSONObject();
        JSONObject req = new JSONObject(request);

        if (req.getString("content").isEmpty() || req.getString("content") == null) {
            response.put("error", "留言為空");
            return response.toString();
        }

        User user = userService.findById(id).get();
        if (user == null) {
            response.put("error", "沒有權限");
            return response.toString();
        }
        Optional<Post> postCheck = postRepository.findById(postId);
        if (postCheck.isEmpty()) {
            response.put("error", "文章不存在");
            return response.toString();
        }
        PostComment comment = postCommentService.findOnePostCommentById(commentId);
        if (comment == null) {
            response.put("error", "留言不存在");
            return response.toString();
        }
        comment.setContent(req.getString("content"));

        PostComment res = postCommentService.ediPostComment(comment);
        if (res == null) {
            response.put("error", "意外錯誤");
            return response.toString();
        }
        response.put("success", "修改成功")
                .put("content", comment.getContent())
                .put("commentId", comment.getId())
                .put("userId", user.getUserId())
                .put("userInfoId", user.getUserinfo().getId());
        return response.toString();
    }

    @DeleteMapping("/delete/{postId}/{commentId}")
    public String deleteComment(@PathVariable int postId, @PathVariable int commentId, HttpSession session,
            @RequestHeader("session-id") UUID id) {
        JSONObject response = new JSONObject();
        User user = userService.findById(id).get();
        if (user == null) {
            response.put("error", "沒有權限");
            return response.toString();
        }
        Optional<Post> postCheck = postRepository.findById(postId);
        if (postCheck.isEmpty()) {
            response.put("error", "文章不存在");
            return response.toString();
        }
        PostComment comment = postCommentService.findOnePostCommentById(commentId);
        if (comment == null) {
            response.put("error", "留言不存在");
            return response.toString();
        }

        postCommentService.deleteComment(comment);

        response.put("success", "刪除成功");
        return response.toString();
    }

    @PostMapping("/findPostComment/{postId}")
    public String findPostComment(@PathVariable Integer postId) {
        JSONObject response = new JSONObject();
        JSONArray items = new JSONArray();
        Post post = postService.findOnePost(postId);
        if (post == null) {
            response.put("error", "文章不存在");
        }
        List<PostComment> comments = postCommentService.findAllPostComment(post);

        for (PostComment comment : comments) {
            JSONObject item = new JSONObject();
            String photo = comment.getUser().getUserinfo().getPhoto();
            if (photo == null || photo.length() < 10) {
                photo = defaultIcon.defaultIcon();
            }
            item.put("addTime", comment.getAddTime())
                    .put("content", comment.getContent())
                    .put("user", comment.getUser().getUserId())
                    .put("userInfoId", comment.getUser().getUserinfo().getId())
                    .put("id", comment.getId())
                    .put("avatar", photo);
            items.put(item);
        }
        response.put("comments", items);
        return response.toString();
    }

}
