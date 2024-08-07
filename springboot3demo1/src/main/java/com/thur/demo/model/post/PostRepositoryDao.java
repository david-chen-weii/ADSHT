package com.thur.demo.model.post;

import java.util.List;

import com.thur.demo.model.friend.Friend;

public interface PostRepositoryDao {

    public List<Post> findPostByContent(String[] keyWord, Integer page);

    public List<Post> findPostByFollow(List<Friend> keyWord, Integer page);

    public List<Post> findPostByHot(List<String> keyWord);

    public List<Post> findAllPost(Integer page);
}
