package com.thur.demo.model.shopping;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.thur.demo.model.post.Post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ProductDAOImpl implements ProductDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public TypedQuery<Product> findBykeyWord(JSONObject keywords) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        List<Predicate> predicates = new ArrayList<>();

        for (String s : keywords.getString("content").split(" ")) {
            String pattern = "%" + s + "%";
            predicates.add(cb.like(root.get("productDescribe"), pattern));
            predicates.add(cb.like(root.get("productName"), pattern));
        }

        query.where(cb.or(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query);
    }

    public TypedQuery<Product> findByProductName(String productName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        List<Predicate> predicates = new ArrayList<>();
        String pattern = "%" + productName + "%";

        predicates.add(cb.like(root.get("productName"), pattern));

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query);
    }

    public TypedQuery<Product> findByProductDescription(String productDescription) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        List<Predicate> predicates = new ArrayList<>();
        String pattern = "%" + productDescription + "%";

        predicates.add(cb.like(root.get("productDescribe"), pattern));

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query);
    }

    public TypedQuery<Product> findByproductPrice(int lowPrice, int highPrice) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.between(root.get("price"), lowPrice, highPrice));

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query);
    }

    public TypedQuery<Product> findByAllCondition(String keyWord, int lowPrice, int highPrice, String order) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        List<Predicate> predicates = new ArrayList<>();

        String pattern = "%" + keyWord + "%";

        // Adding conditions
        Predicate keywordPredicate = cb.or(
                cb.like(root.get("productDescribe"), pattern),
                cb.like(root.get("productName"), pattern));
        predicates.add(keywordPredicate);

        Predicate pricePredicate = cb.between(root.get("price"), lowPrice, highPrice);
        predicates.add(pricePredicate);

        // Combining predicates
        query.where(predicates.toArray(new Predicate[0]));

        // Ordering
        if (order.equalsIgnoreCase("ASC")) {
            query.orderBy(cb.asc(root.get("price")));
        } else {
            query.orderBy(cb.desc(root.get("price")));
        }

        return entityManager.createQuery(query);
    }
}
