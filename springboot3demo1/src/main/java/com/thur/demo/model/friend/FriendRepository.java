package com.thur.demo.model.friend;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thur.demo.model.post.Post;
import com.thur.demo.model.user.User;
import com.thur.demo.model.user.UserInfo;

public interface FriendRepository extends JpaRepository<Friend, FriendId> {

        // 找尋朋友
        List<Friend> findByIdSendIdOrIdReceiveId(UUID sendId, UUID receiveId);

        // 找尋共同朋友(已經為好友狀態)
        @Query("SELECT f1.receiver.userId FROM Friend f1 " +
                        "JOIN Friend f2 ON f1.receiver.userId = f2.receiver.userId " +
                        "WHERE f1.sender.userId = :userId1 " +
                        "AND f2.sender.userId = :userId2 " +
                        "AND f1.state >= 1 " +
                        "AND f2.state >= 1")
        List<UUID> findCommonFriends(@Param("userId1") UUID userId1, @Param("userId2") UUID userId2);

        // 推薦朋友 (用戶之間不為好友但有共同好友)
        @Query("SELECT DISTINCT f2.sender.userId FROM Friend f1 " +
                        "JOIN Friend f2 ON f1.receiver.userId = f2.receiver.userId " +
                        "WHERE f1.sender.userId = :userId " +
                        "AND f1.state >= 1 " +
                        "AND f2.state >= 1 " +
                        "AND f2.sender.userId NOT IN " +
                        "(SELECT f3.receiver.userId FROM Friend f3 " +
                        "WHERE f3.sender.userId = :userId " +
                        "AND f3.state >= 1) " +
                        "AND f2.sender.userId <> :userId")
        
        List<UUID> findRecommendedFriends(@Param("userId") UUID userId);
        
        // 查詢追蹤自身數量（關注）
        @Query("SELECT COUNT(f) FROM Friend f WHERE f.sender.userId = :sendId")
        Long countFollowing(@Param("sendId") UUID sendId);
        
        // 查詢追蹤者數量（粉絲）
        @Query("SELECT COUNT(f) FROM Friend f WHERE f.receiver.userId = :receiveId")
        Long countFollowers(@Param("receiveId") UUID receiveId);

        // 查詢關注名單receiveId
        @Query("SELECT f FROM Friend f JOIN FETCH f.receiver r JOIN FETCH r.userinfo WHERE f.id.sendId = :sendId")
        List<Friend> findFollowing(@Param("sendId") UUID sendId);

        // 查詢粉絲名單
        @Query("SELECT f FROM Friend f JOIN FETCH f.sender s JOIN FETCH s.userinfo WHERE f.id.receiveId = :receiveId")
        List<Friend> findFollowers(@Param("receiveId") UUID receiveId);
        
        @Query("SELECT u FROM UserInfo u WHERE u.id = :id")
        List<UserInfo> findById(@Param("id") Integer id);
        
        //找貼文，由新的到舊
        @Query("SELECT p FROM Post p WHERE p.poster.userId = :posterId ORDER BY p.id DESC")
        List<Post> findPostsByIdDesc(@Param("posterId") UUID posterId);
        //找貼文，由舊的到新
        @Query("SELECT p FROM Post p WHERE p.poster.userId = :posterId ORDER BY p.id ASC")
        List<Post> findPostsByIdAsc(@Param("posterId") UUID posterId);
        
        // 查詢貼文數量
        @Query("SELECT COUNT(p) FROM Post p WHERE p.poster.userId = :posterId")
        Long countPost(@Param("posterId") UUID posterId);
}
