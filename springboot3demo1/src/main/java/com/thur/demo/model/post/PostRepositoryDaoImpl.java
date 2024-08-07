package com.thur.demo.model.post;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.thur.demo.model.friend.Friend;
import com.thur.demo.model.friend.FriendRepository;
import com.thur.demo.model.user.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class PostRepositoryDaoImpl implements PostRepositoryDao {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private FriendRepository friendRepository;

    @Override
    public List<Post> findPostByContent(String[] keyWord, Integer page) {
        if (page == null) {
            page = 1;
        }
        // criteriaBuilder 設定
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> query = cb.createQuery(Post.class);
        Root<Post> root = query.from(Post.class);
        // 條件暫存區
        List<Predicate> predicates = new ArrayList<>();
        for (String s : keyWord) {
            String pattern = "%" + s + "%";
            predicates.add(cb.like(root.get("content"), pattern));
        }

        query.where(cb.or(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Post> findPostByFollow(List<Friend> friends, Integer page) {
        // criteriaBuilder 設定
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> query = cb.createQuery(Post.class);
        Root<Post> root = query.from(Post.class);
        // 條件暫存區
        if (page == null) {
            page = 1;
        }
        List<Predicate> predicates = new ArrayList<>();
        for (Friend f : friends) {

            User pattern = f.getReceiver();

            predicates.add(cb.equal(root.get("poster"), pattern));
        }

        query.where(cb.or(predicates.toArray(new Predicate[0])));
        query.orderBy(cb.desc(root.get("addTime")));

        List<Post> res = entityManager.createQuery(query).setFirstResult((page - 1) * 10).setMaxResults(page * 10 - 1)
                .getResultList();

        return res;
    }

    @Override
    public List<Post> findPostByHot(List<String> keyWord) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> query = cb.createQuery(Post.class);
        Root<Post> root = query.from(Post.class);

        return null;
    }

    @Override
    public List<Post> findAllPost(Integer page) {
        if (page == null) {
            page = 1;
        }
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> query = cb.createQuery(Post.class);
        Root<Post> root = query.from(Post.class);

        query.orderBy(cb.desc(root.get("addTime")));

        return entityManager.createQuery(query).setFirstResult((page - 1) * 10).setMaxResults(page * 10 - 1)
                .getResultList();
    }
}
