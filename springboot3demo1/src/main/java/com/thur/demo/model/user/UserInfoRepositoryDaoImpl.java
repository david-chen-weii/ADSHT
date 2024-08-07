package com.thur.demo.model.user;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class UserInfoRepositoryDaoImpl implements UserInfoRepositoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserInfo> findWithCustomCriteria(String friend) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserInfo> query = cb.createQuery(UserInfo.class);
        Root<UserInfo> root = query.from(UserInfo.class);

        Predicate predicate = cb.equal(root.get("myField"), friend);
        query.select(root).where(predicate);

        return entityManager.createQuery(query).getResultList();
    }

}
