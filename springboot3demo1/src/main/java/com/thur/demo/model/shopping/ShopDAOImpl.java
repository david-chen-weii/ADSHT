package com.thur.demo.model.shopping;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ShopDAOImpl implements ShopDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Shop> findByShopName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Shop> query = cb.createQuery(Shop.class);
        Root<Shop> root = query.from(Shop.class);
        return null;
    }

    public List<Shop> findByShopDescription(String description) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Shop> query = cb.createQuery(Shop.class);
        Root<Shop> root = query.from(Shop.class);
        return null;
    }

    public List<Shop> findByOwnerName(String ownerName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Shop> query = cb.createQuery(Shop.class);
        Root<Shop> root = query.from(Shop.class);

        List<Predicate> predicates = new ArrayList<>();

        String pattern = "%" + ownerName + "%";
        predicates.add(cb.like(root.get("content"), pattern));

        query.where(cb.or(predicates.toArray(new Predicate[0])));
        TypedQuery a = entityManager.createQuery(query);

        return null;

    }

    public TypedQuery<Shop> findBykeyWord(JSONObject keywords) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Shop> query = cb.createQuery(Shop.class);
        Root<Shop> root = query.from(Shop.class);
        List<Predicate> predicates = new ArrayList<>();

        for (String s : keywords.getString("content").split(" ")) {
            String pattern = "%" + s + "%";
            predicates.add(cb.like(root.get("shopName"), pattern));
            predicates.add(cb.like(root.get("shopDescription"), pattern));
        }
        query.where(cb.or(predicates.toArray(new Predicate[0])));
        TypedQuery<Shop> a = entityManager.createQuery(query);
        return a;
    }
}
