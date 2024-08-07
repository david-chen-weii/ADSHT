package com.thur.demo.model.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {

    public List<PostComment> findAllByPostId(Integer postId);

}
