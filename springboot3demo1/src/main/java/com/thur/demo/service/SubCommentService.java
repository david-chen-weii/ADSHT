package com.thur.demo.service;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thur.demo.model.post.Post;
import com.thur.demo.model.post.PostComment;
import com.thur.demo.model.post.SubComment;
import com.thur.demo.model.post.SubCommentRepository;
import com.thur.demo.model.user.User;

@Service
public class SubCommentService {
    @Autowired
    private SubCommentRepository SubCommentRepository;

    public SubComment addSubComment(JSONObject content, PostComment postComment, User user) {

        /*
         * 資訊 content
         */
        SubComment comment = new SubComment();
        comment.setPostComment(postComment);
        comment.setUser(user);
        comment.setContent(content.getString("content"));
        SubComment res = SubCommentRepository.save(comment);
        return res;
    }

    public void deleteComment(SubComment subComment) {

        SubCommentRepository.delete(subComment);
    }

    public void deleteCommentById(int id) {
        Optional<SubComment> subCommentCheck = SubCommentRepository.findById(id);
        if (subCommentCheck.isPresent()) {
            SubCommentRepository.delete(subCommentCheck.get());
        }
    }

    public SubComment ediPostComment(SubComment subComment) {

        SubComment res = SubCommentRepository.save(subComment);

        return res;
    }

    public List<SubComment> findPostSubComment(PostComment postComment) {
        List<SubComment> res = SubCommentRepository.findAllByPostCommentId(postComment.getId());

        return res;
    }

    public SubComment findOneSubComment(int id) {
        Optional<SubComment> subCommentCheck = SubCommentRepository.findById(id);
        if (subCommentCheck.isEmpty()) {
            return null;
        }
        return subCommentCheck.get();
    }
}
