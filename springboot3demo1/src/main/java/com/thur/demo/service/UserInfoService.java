package com.thur.demo.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thur.demo.model.shopping.Product;
import com.thur.demo.model.shopping.ProductImage;
import com.thur.demo.model.shopping.Shop;
import com.thur.demo.model.user.User;
import com.thur.demo.model.user.UserInfo;
import com.thur.demo.model.user.UserInfoRepository;
import com.thur.demo.util.DatetimeConverter;

import jakarta.servlet.http.HttpSession;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    public UserInfo addUserInfo(JSONObject data, User user, HttpSession session) {
        UserInfo info = user.getUserinfo();
        Date birth = data.getString("birth") != null ? Date.valueOf(data.getString("birth")) : null;
        String email = data.getString("email") != null ? data.getString("email") : null;
        Integer gender = data.getString("gender") != null ? Integer.valueOf(data.getString("gender")) : null;
        String phone = data.getString("phone") != null ? data.getString("phone") : null;
        String selfContent = data.getString("selfContent") != null ? data.getString("selfContent") : null;
        String firstName = data.getString("firstName") != null ? data.getString("firstName") : null;
        String lastName = data.getString("lastName") != null ? data.getString("lastName") : null;

        info.setBirth(birth);
        info.setEmail(email);
        info.setGender(gender);
        info.setPhone(phone);
        info.setSelfContent(selfContent);
        info.setUserFirstName(firstName);
        info.setUserLastName(lastName);
        UserInfo res = userInfoRepository.save(info);
        return res;
    }

    public UserInfo editUserInfo(UserInfo info) {
        UserInfo res = userInfoRepository.save(info);
        return res;
    }

    public UserInfo editUserInfoByJson(User user, JSONObject data, HttpSession session) {
        UserInfo info = user.getUserinfo();
        Date birth = data.getString("birth") != null ? Date.valueOf(data.getString("birth")) : null;
        String email = data.getString("email") != null ? data.getString("email") : null;
        Integer gender = data.getString("gender") != null ? Integer.valueOf(data.getString("gender")) : null;
        String phone = data.getString("phone") != null ? data.getString("phone") : null;
        String selfContent = data.getString("selfContent") != null ? data.getString("selfContent") : null;
        String firstName = data.getString("firstName") != null ? data.getString("firstName") : null;
        String lastName = data.getString("lastName") != null ? data.getString("lastName") : null;

        info.setBirth(birth);
        info.setEmail(email);
        info.setGender(gender);
        info.setPhone(phone);
        info.setSelfContent(selfContent);
        info.setUserFirstName(firstName);
        info.setUserLastName(lastName);
        userInfoRepository.save(info);
        return info;
    }

    public void deleteUserInfo(UserInfo info) {

        userInfoRepository.delete(info);
    }

    public void deleteUserInfoById(User user, HttpSession session) {
        userInfoRepository.deleteById(user.getUserinfo().getId());
    }

    public UserInfo findUserInfo(User user, HttpSession session) {
        UserInfo userInfo = user.getUserinfo();
        return userInfo;
    }

    public UserInfo findUserInfoByid(Integer id) {
        Optional<UserInfo> userInfoCheck = userInfoRepository.findById(id);
        if (userInfoCheck.isEmpty()) {
            return null;
        }
        return userInfoCheck.get();
    }

    // 給個人頁面修改除圖片、email資料
    public UserInfo editUserInfoByJsonSave(User user, JSONObject data, HttpSession session) {
        UserInfo info = user.getUserinfo();
        try {
            if (data.has("birth")) {
                String birthStr = data.getString("birth");
                System.out.println("Received birth date: " + birthStr);
                Date birthDate = new java.sql.Date(DatetimeConverter.parse(birthStr, "yyyy-MM-dd").getTime());
                System.out.println("Parsed birth date: " + birthDate);
                info.setBirth(birthDate);
            }
            if (data.has("gender")) {
                // 確保 gender 是字符串，然後轉換為整數
                String genderStr = data.getString("gender");
                info.setGender(Integer.valueOf(genderStr));
            }
            if (data.has("phone")) {
                info.setPhone(data.getString("phone"));
            }
            if (data.has("selfContent")) {
                info.setSelfContent(data.getString("selfContent"));
            }
            if (data.has("userFirstName")) {
                info.setUserFirstName(data.getString("userFirstName"));
            }
            if (data.has("userLastName")) {
                info.setUserLastName(data.getString("userLastName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return userInfoRepository.save(info);
    }

    // 給個人頁面修改圖片
    public UserInfo updateUserPhoto(User user, String base64Image) {
        UserInfo info = user.getUserinfo();
        info.setPhoto(base64Image);
        return userInfoRepository.save(info);
    }

    public UserInfo findUserInfoByUser(User user) {
        return userInfoRepository.findByUser(user);
    }

    // 給推薦好友
    public UserInfo getUserInfoByUserId(UUID userId) {
        return userInfoRepository.findByUserId(userId);
    }

    // 找商城及商品
    public List<Shop> findShopsAndProductsByUserId(UUID userId) {
        List<Shop> shops = userInfoRepository.findShopsByUserId(userId);

        if (shops.isEmpty()) {
            return shops;
        }

        List<Integer> shopIds = shops.stream().map(Shop::getShopId).collect(Collectors.toList());
        List<Product> products = userInfoRepository.findProductsByShopIds(shopIds);

        if (products.isEmpty()) {
            return shops;
        }

        List<Integer> productIds = products.stream().map(Product::getProductId).collect(Collectors.toList());
        List<ProductImage> productImages = userInfoRepository.findProductImagesByProductIds(productIds);

        // 将产品和图片添加到相应的商店和产品中
        products.forEach(product -> {
            List<ProductImage> images = productImages.stream()
                    .filter(image -> image.getProduct().getProductId().equals(product.getProductId()))
                    .collect(Collectors.toList());
            product.setProductImage(images);
        });

        shops.forEach(shop -> {
            List<Product> shopProducts = products.stream()
                    .filter(product -> product.getShop().getShopId().equals(shop.getShopId()))
                    .collect(Collectors.toList());
            shop.setProducts(shopProducts);
        });

        return shops;
    }

    // 後台管理系統
    public List<Map<String, Object>> getEmailAndLastName() {
        List<UserInfo> userInfos = userInfoRepository.findAll();
        return userInfos.stream()
                .map(userInfo -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("id", userInfo.getId());
                    map.put("email", userInfo.getEmail());
                    map.put("userLastName", userInfo.getUserLastName());
                    map.put("status", userInfo.getStatus());
                    return map;
                })
                .collect(Collectors.toList());
    }

    // 激活用户
    public void activateUser(Integer userId) {
        UserInfo userInfo = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userInfo.setStatus(1);
        ; // 1 表示激活
        userInfoRepository.save(userInfo);
    }

    // 禁用用户
    public void deactivateUser(Integer userId) {
        UserInfo userInfo = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userInfo.setStatus(0); // 0 表示禁用
        userInfoRepository.save(userInfo);
    }
}
