package com.thur.demo.model.shopping;

import java.util.List;

import org.json.JSONObject;

import jakarta.persistence.TypedQuery;

public interface ProductDAO {
    TypedQuery<Product> findBykeyWord(JSONObject keyWord);

    TypedQuery<Product> findByProductName(String productName);

    TypedQuery<Product> findByProductDescription(String productDescription);

    TypedQuery<Product> findByproductPrice(int lowPrice, int highPrice);

    TypedQuery<Product> findByAllCondition(String keyWord, int lowPrice, int highPrice, String order);
}
