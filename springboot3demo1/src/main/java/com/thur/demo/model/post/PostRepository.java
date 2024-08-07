package com.thur.demo.model.post;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer>, PostRepositoryDao {
    /*
     * @Query(value = "FROM post where content like %:key% ")
     * public List<Post> postFind(String key);
     */
}
