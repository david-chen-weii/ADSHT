package com.thur.demo.model.shopping;

import java.util.List;

import org.json.JSONObject;

import jakarta.persistence.TypedQuery;

public interface ShopDAO {
    List<Shop> findByShopName(String name);

    List<Shop> findByShopDescription(String description);

    List<Shop> findByOwnerName(String ownerName);

    TypedQuery<Shop> findBykeyWord(JSONObject keywords);
}
