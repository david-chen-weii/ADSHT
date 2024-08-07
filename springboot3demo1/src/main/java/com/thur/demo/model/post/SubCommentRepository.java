package com.thur.demo.model.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubCommentRepository extends JpaRepository<SubComment, Integer> {

    @Query(value = "SELECT sc FROM SubComment sc WHERE sc.postComment = :id ORDER BY sc.addTime DESC")
    public List<SubComment> findAllByPostCommentId(Integer id);

}
