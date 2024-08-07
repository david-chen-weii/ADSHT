package com.thur.demo.service;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thur.demo.model.friend.Friend;
import com.thur.demo.model.friend.FriendRepository;
import com.thur.demo.model.post.Post;
import com.thur.demo.model.post.PostRepository;
import com.thur.demo.model.user.User;
import com.thur.demo.model.user.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository usersRepository;
    @Autowired
    private FriendRepository friendRepository;

    public Post addPost(JSONObject post, User user, HttpSession session) {
        /*
         * 傳送 內容(content), 相片(image)
         */
        // 預設值
        String image = null;
        Integer permission = 0;
        // 賦值
        String content = post.getString("content") != null ? post.getString("content") : null;
        try {
            image = post.getString("image") != null ? post.getString("image") : null;
        } catch (Exception e) {
            image = null;
        }
        try {
            permission = post.getString("permission") != null ? post.getInt("permission") : 0;
        } catch (Exception e) {
            permission = 0;
        }
        // 新增
        Post newPost = new Post();
        System.out.println("inside3");

        newPost.setPoster(user);
        newPost.setContent(content);
        newPost.setPermission(permission);
        newPost.setImage(image);
        newPost.setLikeCount(0);

        postRepository.save(newPost);
        return newPost;
    }

    public Post editPost(Post post) {
        Post editPost = postRepository.save(post);

        return editPost;
    }

    public void deletePost(Post post) {
        postRepository.delete(post);
    }

    public List<Post> findAllPost(Integer page) {

        return postRepository.findAllPost(page);

    }

    public List<Post> searchPostByWord(String[] keyWord, Integer page) {
        return postRepository.findPostByContent(keyWord, 1);

    }

    public List<Post> searchPostByHot() {

        return null;
    }

    public List<Post> searchPostByFollower(User user, Integer page) {
        List<Friend> friends = user.getSentFriends();

        List<Post> posts = postRepository.findPostByFollow(friends, page);

        return posts;
    }

    public Post findOnePost(int id) {
        Optional<Post> postCheck = postRepository.findById(id);
        if (postCheck.isPresent()) {
            return postCheck.get();
        }
        return null;
    }
}
