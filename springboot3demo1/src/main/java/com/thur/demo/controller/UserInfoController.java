package com.thur.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thur.demo.model.shopping.Shop;
import com.thur.demo.model.user.User;
import com.thur.demo.model.user.UserInfo;
import com.thur.demo.service.UserInfoService;
import com.thur.demo.service.UserService;

@RestController
@RequestMapping("/api/userInfo")
@CrossOrigin
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserService userService;

    @PutMapping("/updatePhoto")
    public String updateUserPhoto(@RequestBody String request, @RequestHeader("session-id") UUID id) {
        JSONObject response = new JSONObject();
        JSONObject body = new JSONObject(request);
        String base64Image = body.getString("image");

        if (base64Image == null || base64Image.isEmpty()) {
            response.put("success", false).put("message", "No image provided");
            return response.toString();
        }

        // Find the user by session-id
        User user = userService.findById(id).orElse(null);
        if (user == null) {
            response.put("success", false).put("message", "User not found or no permission");
            return response.toString();
        }

        // Update user photo
        UserInfo updatedInfo = userInfoService.updateUserPhoto(user, base64Image);
        if (updatedInfo == null) {
            response.put("success", false).put("message", "Update failed");
            return response.toString();
        }

        response.put("success", true).put("message", "Update successful");
        return response.toString();
    }

    @PutMapping("/updateInfo")
    public String updateUserInfo(@RequestBody String request, @RequestHeader("session-id") UUID id) {
        JSONObject response = new JSONObject();
        JSONObject body = new JSONObject(request);

        // Find the user by session-id
        User user = userService.findById(id).orElse(null);
        if (user == null) {
            response.put("success", false).put("message", "User not found or no permission");
            return response.toString();
        }

        // Update user info
        UserInfo updatedInfo = userInfoService.editUserInfoByJsonSave(user, body, null);
        if (updatedInfo == null) {
            response.put("success", false).put("message", "Update failed");
            return response.toString();
        }

        response.put("success", true).put("message", "Update successful");
        return response.toString();
    }

    // 新增的方法
    @PostMapping("/findUserInfoByUserId")
    public Map<String, Object> findUserInfoByUserId(@RequestBody String body) {
        JSONObject obj = new JSONObject(body);
        String userIdStr = obj.optString("userId", null);

        Map<String, Object> response = new HashMap<>();

        if (userIdStr == null || userIdStr.isEmpty()) {
            response.put("success", false);
            response.put("message", "userId 是必要欄位，且不能為空");
            return response;
        }

        try {
            UUID userId = UUID.fromString(userIdStr);
            User user = userService.findById(userId).orElse(null);
            if (user == null) {
                response.put("success", false);
                response.put("message", "找不到該用戶");
                return response;
            }

            UserInfo userInfo = userInfoService.findUserInfoByUser(user);

            if (userInfo == null) {
                response.put("success", false);
                response.put("message", "UserInfo not found");
                return response;
            }

            Map<String, Object> userInfoMap = new HashMap<>();
            userInfoMap.put("id", userInfo.getId());
            userInfoMap.put("birth", userInfo.getBirth());
            userInfoMap.put("email", userInfo.getEmail());
            userInfoMap.put("gender", userInfo.getGender());
            userInfoMap.put("phone", userInfo.getPhone());
            userInfoMap.put("photo", userInfo.getPhoto());
            userInfoMap.put("selfContent", userInfo.getSelfContent());
            userInfoMap.put("userFirstName", userInfo.getUserFirstName());
            userInfoMap.put("userLastName", userInfo.getUserLastName());
            userInfoMap.put("userId", userInfo.getUser().getUserId());

            response.put("success", true);
            response.put("userInfo", userInfoMap);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
    }

    // 找商城及商品
    @PostMapping("/getUserShopsAndProducts")
    public Map<String, Object> getUserShopsAndProducts(@RequestBody String request) {
        Map<String, Object> response = new HashMap<>();
        JSONObject body = new JSONObject(request);
        String userIdStr = body.optString("userId", null);

        if (userIdStr == null || userIdStr.isEmpty()) {
            response.put("success", false);
            response.put("message", "userId 是必要欄位，且不能為空");
            return response;
        }

        try {
            UUID userId = UUID.fromString(userIdStr);
            User user = userService.findById(userId).orElse(null);
            if (user == null) {
                response.put("success", false);
                response.put("message", "找不到該用戶");
                return response;
            }

            List<Shop> shops = userInfoService.findShopsAndProductsByUserId(userId);

            if (shops == null || shops.isEmpty()) {
                response.put("success", false);
                response.put("message", "找不到任何商店");
                return response;
            }

            List<Map<String, Object>> shopList = shops.stream().map(shop -> {
                Map<String, Object> shopMap = new HashMap<>();
                shopMap.put("shopId", shop.getShopId());
                shopMap.put("shopName", shop.getShopName());
                shopMap.put("shopDescription", shop.getShopDescription());
                shopMap.put("shopImage", shop.getImage());

                List<Map<String, Object>> productList = shop.getProducts().stream().map(product -> {
                    Map<String, Object> productMap = new HashMap<>();
                    productMap.put("productId", product.getProductId());
                    productMap.put("productName", product.getProductName());
                    productMap.put("productDescribe", product.getProductDescribe());
                    productMap.put("productPrice", product.getProductPrice());
                    productMap.put("stock", product.getStock());

                    List<Map<String, Object>> productImages = product.getProductImage().stream().map(image -> {
                        Map<String, Object> imageMap = new HashMap<>();
                        imageMap.put("productImageId", image.getProductImageId());
                        imageMap.put("image", image.getImage());
                        return imageMap;
                    }).collect(Collectors.toList());

                    productMap.put("productImages", productImages);
                    return productMap;
                }).collect(Collectors.toList());

                shopMap.put("products", productList);
                return shopMap;
            }).collect(Collectors.toList());

            response.put("success", true);
            response.put("shops", shopList);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
    }

    @PostMapping("/lastname")
    public List<Map<String, Object>> getEmailAndLastName() {
        return userInfoService.getEmailAndLastName();
    }

    @PutMapping("/activate/{userId}")
    public ResponseEntity<String> activateUser(@PathVariable Integer userId) {
        userInfoService.activateUser(userId); // 确保 userId 是 Integer 类型
        return ResponseEntity.ok("User activated");
    }

    @PutMapping("/deactivate/{userId}")
    public ResponseEntity<String> deactivateUser(@PathVariable Integer userId) {
        userInfoService.deactivateUser(userId); // 确保 userId 是 Integer 类型
        return ResponseEntity.ok("User deactivated");
    }

}
