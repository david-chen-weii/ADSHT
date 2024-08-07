package com.thur.demo.service;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thur.demo.model.post.Post;
import com.thur.demo.model.post.PostComment;
import com.thur.demo.model.post.PostCommentRepository;
import com.thur.demo.model.user.User;

@Service
public class PostCommentService {
    @Autowired
    private PostCommentRepository postCommentRepository;

    public PostComment addComment(JSONObject content, Post post, User user) {

        /*
         * 資訊 content
         */
        PostComment comment = new PostComment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content.getString("content"));
        PostComment res = postCommentRepository.save(comment);
        return res;
    }

    public void deleteComment(PostComment postComment) {

        postCommentRepository.delete(postComment);
    }

    public PostComment ediPostComment(PostComment postComment) {

        PostComment res = postCommentRepository.save(postComment);

        return res;
    }

    public PostComment editPostCommentByJson(Integer CommentId, JSONObject data) {
        String content = data.getString("content") != null ? data.getString("content") : null;
        Optional<PostComment> postCommentCheck = postCommentRepository.findById(CommentId);
        if (postCommentCheck.isEmpty()) {
            return null;
        }
        PostComment postComment = postCommentCheck.get();
        postComment.setContent(content);
        PostComment res = postCommentRepository.save(postComment);
        return res;
    }

    public List<PostComment> findAllPostComment(Post post) {
        List<PostComment> res = postCommentRepository.findAllByPostId(post.getId());

        return res;
    }

    public PostComment findOnePostCommentById(int id) {
        Optional<PostComment> postCommentCheck = postCommentRepository.findById(id);
        if (postCommentCheck.isEmpty()) {
            return null;
        }
        return postCommentCheck.get();
    }
}
