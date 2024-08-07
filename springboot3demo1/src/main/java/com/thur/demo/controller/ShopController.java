package com.thur.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thur.demo.model.shopping.Cart;
import com.thur.demo.model.shopping.Product;
import com.thur.demo.model.shopping.Shop;
import com.thur.demo.model.user.User;
import com.thur.demo.model.user.UserInfo;
import com.thur.demo.service.ShopService;
import com.thur.demo.service.UserInfoService;
import com.thur.demo.service.UserService;
import com.thur.demo.util.DefaultIcon;

import jakarta.persistence.TypedQuery;

@RestController
@CrossOrigin
@RequestMapping("/ajax/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private DefaultIcon defaultIcon;

    @PostMapping("/newShop")
    public String openShop(@RequestBody String request, @RequestHeader("session-id") UUID userId) {
        JSONObject response = new JSONObject();
        JSONObject body = new JSONObject(request);
        // 使用者檢查
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            response.put("error", "無使用者");
            return response.toString();
        }
        // 結果
        Shop shop = shopService.openNewShop(body, user.get());
        if (shop == null) {
            response.put("error", "意外錯誤");
            return response.toString();
        }
        response.put("success", "成功");

        return response.toString();
    }

    @DeleteMapping("/closeShop/{shopId}")
    public String closeShop(@PathVariable int shopId, @RequestHeader("session-id") UUID userId) {
        JSONObject response = new JSONObject();
        // 使用者檢查
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            response.put("error", "無使用者");
            return response.toString();
        }

        // 商店檢查
        Optional<Shop> shop = shopService.findOneShopById(shopId);
        if (shop.isEmpty()) {
            response.put("error", "無此商店");
            return response.toString();
        }
        // 結果
        shopService.closeShop(shop.get());
        response.put("success", "關閉成功");
        return response.toString();
    }

    @PutMapping("/editShop/{shopId}")
    public String editShop(@PathVariable int shopId, @RequestBody String request,
            @RequestHeader("session-id") UUID userId) {
        JSONObject response = new JSONObject();
        // 使用者檢查
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            response.put("error", "無使用者");
            return response.toString();
        }

        JSONObject body = new JSONObject(request);
        // 商店檢查
        Optional<Shop> shop = shopService.findOneShopById(shopId);
        if (shop.isEmpty()) {
            response.put("error", "無此商店");
            return response.toString();
        }
        // 結果
        Shop res = shopService.editShop(shop.get(), body);
        if (res == null) {
            response.put("fail", "意外錯誤");
            return response.toString();
        }
        response.put("success", "成功");
        return response.toString();
    }

    @PostMapping("{shopId}/newProduct")
    public String openSho(@RequestBody String request, @RequestHeader("session-id") UUID userId,
            @PathVariable int shopId) {
        JSONObject response = new JSONObject();
        JSONObject body = new JSONObject(request);
        // 使用者檢查
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            response.put("error", "無使用者");
            return response.toString();
        }
        // 商店檢查
        Optional<Shop> shop = shopService.findOneShopById(shopId);
        if (shop.isEmpty()) {
            response.put("error", "無此商店");
            return response.toString();
        }
        // 結果
        Product product = shopService.addNewProduct(body, shop.get());
        if (product == null) {
            response.put("error", "意外錯誤");
            return response.toString();
        }
        response.put("success", "成功");
        return response.toString();
    }

    @DeleteMapping("/{shopId}/deleteProduct/{productId}")
    public String deleteProduct(@RequestHeader("session-id") UUID userId, @PathVariable int shopId,
            @PathVariable int productId) {
        JSONObject response = new JSONObject();
        // 使用者檢查
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            response.put("error", "無使用者");
        }

        // 商店檢查
        Optional<Shop> shop = shopService.findOneShopById(shopId);
        if (shop.isEmpty()) {
            response.put("error", "無此商店");
            return response.toString();
        }

        // 商品檢查
        Optional<Product> product = shopService.findOneProductById(productId);
        if (product.isEmpty()) {
            response.put("error", "無此商品");
            return response.toString();
        }

        // 結果
        shopService.deleteProduct(product.get());
        response.put("success", "刪除成功");
        return response.toString();
    }

    @PutMapping("{shopId}/editProduct/{productId}")
    public String editProduct(@RequestBody String request, @RequestHeader("session-id") UUID userId,
            @PathVariable int shopId,
            @PathVariable int productId) {

        JSONObject response = new JSONObject();
        JSONObject body = new JSONObject(request);
        // 使用者檢查
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            response.put("error", "無使用者");
        }

        // 商店檢查
        Optional<Shop> shop = shopService.findOneShopById(shopId);
        if (shop.isEmpty()) {
            response.put("error", "無此商店");
            return response.toString();
        }

        // 產品檢查
        Optional<Product> product = shopService.findOneProductById(productId);
        if (product.isEmpty()) {
            response.put("error", "無此商品");
            return response.toString();
        }

        // 結果
        Product res = shopService.editProduct(product.get(), body);
        if (res == null) {
            response.put("error", "意外錯誤");
            return response.toString();
        }

        response.put("success", "成功");
        return response.toString();

    }

    @PostMapping("/addToCart/{productId}")
    public String addToCart(@RequestHeader("session-id") UUID userId, @PathVariable int productId) {
        JSONObject response = new JSONObject();
        // 使用者檢查
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            response.put("error", "無使用者");
        }

        // 產品檢查
        Optional<Product> product = shopService.findOneProductById(productId);
        if (product.isEmpty()) {
            response.put("error", "無此商品");
            return response.toString();
        }

        // 結果
        Cart cart = shopService.addProductToCart(product.get(), user.get());
        if (cart == null) {
            response.put("error", "意外錯誤");
            return response.toString();
        }

        response.put("success", "加入成功");
        return response.toString();
    }

    @DeleteMapping("/deleteCartProduct/{cartId}")
    public String deleteCartProduct(@RequestHeader("session-id") UUID userId, @PathVariable int cartId) {
        JSONObject response = new JSONObject();
        // 使用者檢查
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            response.put("error", "無使用者");
        }

        // 購物車檢查
        Optional<Cart> cart = shopService.findCartById(cartId);
        if (cart.isEmpty()) {
            response.put("error", "無此商品");
            return response.toString();
        }
        // 結果
        shopService.delete(cart.get());
        response.put("success", "刪除成功");
        return response.toString();

    }

    @PostMapping("/closeOrder")
    public String closeOrder(@RequestBody String request, @RequestHeader("session-id") UUID userId) {
        JSONObject response = new JSONObject();
        JSONObject body = new JSONObject(request);
        // 使用者檢查
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            response.put("error", "無使用者");
        }
        // 購物車檢查
        List<Cart> carts = new ArrayList<>();
        JSONArray bodyArray = body.getJSONArray("carts");
        JSONArray numberArray = body.getJSONArray("price");
        System.out.println(bodyArray);
        System.out.println(numberArray.get(0));

        for (int i = 0; i < bodyArray.length(); i++) {
            Optional<Cart> cart = shopService.findCartById(bodyArray.getInt(i));
            if (cart.isPresent() && cart.get().getStatus() == 0) {
                cart.get().setNumber(numberArray.getInt(i));
                // 改變存貨數量
                carts.add(cart.get());
            }
        }
        // 結果
        if (carts.size() <= 0) {
            response.put("error", "無商品");
            return response.toString();
        }
        return shopService.buyThing(carts).toString();
    }

    @PostMapping("/shop")
    public String findAllShop(@RequestHeader("session-user-info-id") int session) {

        JSONObject response = new JSONObject();
        List<Shop> shops = shopService.findAllShop();
        try {
            UserInfo user = userInfoService.findUserInfoByid(session);
            if (user == null) {
                response.put("user", "null");
            } else {
                Shop userShop = shopService.findShopByOwner(user.getUser());
                if (userShop != null) {
                    response.put("ownShop", userShop.getShopId());
                }
            }
        } catch (Exception e) {
            // notLogin
            response.put("user", "null");
        }
        JSONArray shopArray = new JSONArray();

        for (Shop shop : shops) {
            JSONObject shopObj = new JSONObject();
            shopObj.put("image", shop.getImage())
                    .put("shopName", shop.getShopName())
                    .put("description", shop.getShopDescription())
                    .put("shopId", shop.getShopId())
                    .put("owner", shop.getOwner().getUserId());
            shopArray.put(shopObj);
        }
        response.put("shops", shopArray);
        return response.toString();
    }

    @GetMapping("")
    public String getShop(@RequestParam int shopId) {
        JSONObject response = new JSONObject();

        Optional<Shop> shop = shopService.findOneShopById(shopId);
        if (shop.isEmpty()) {
            response.put("error", "沒有此商店");
            return response.toString();
        }

        response.put("image", shop.get().getImage())
                .put("shopId", shop.get().getShopId())
                .put("description", shop.get().getShopDescription())
                .put("shopName", shop.get().getShopName())
                .put("owner", shop.get().getOwner().getUserId());

        JSONArray products = new JSONArray();

        for (Product product : shop.get().getProducts()) {
            JSONObject item = new JSONObject();
            String photo = product.getProductImage().size() > 0 ? product.getProductImage().get(0).getImage()
                    : defaultIcon.defaultProductIcon();
            item.put("productName", product.getProductName())
                    .put("productDescription", product.getProductDescribe())
                    .put("image", photo)
                    .put("price", product.getProductPrice())
                    .put("stock", product.getStock())
                    .put("productId", product.getProductId());
            products.put(item);
        }
        response.put("products", products);
        return response.toString();
    }

    @PostMapping("/cart")
    public String getMethodName(@RequestHeader("session-id") UUID session) {
        // 使用者確認
        Optional<User> user = userService.findById(session);
        JSONObject response = new JSONObject();
        if (user.isEmpty()) {
            response.put("error", "無使用者");
            return response.toString();
        }
        // 結果
        List<Cart> carts = shopService.findCartByUserAndStaus(user.get(), 0);
        JSONArray items = new JSONArray();
        for (Cart cart : carts) {
            JSONObject item = new JSONObject();
            if (cart.getStatus() == 1) {
                continue;
            }
            String image = cart.getProduct().getProductImage().isEmpty() ? defaultIcon.defaultProductIcon()
                    : cart.getProduct().getProductImage().get(0).getImage();
            item.put("number", cart.getNumber())
                    .put("image", image)
                    .put("name", cart.getProduct().getProductName())
                    .put("id", cart.getProduct().getProductId())
                    .put("price", cart.getProduct().getProductPrice())
                    .put("stock", cart.getProduct().getStock())
                    .put("cartId", cart.getCartId());
            items.put(item);
        }
        response.put("product", items);
        return response.toString();
    }

    @PostMapping("/findShops")
    public String findShopsByKeyword(@RequestBody String request) {
        JSONObject response = new JSONObject();
        TypedQuery<Shop> shops = shopService.findShopByKeyword(new JSONObject(request));
        JSONArray items = new JSONArray();

        for (Shop shop : shops.getResultList()) {
            JSONObject item = new JSONObject();
            item.put("image", shop.getImage())
                    .put("shopId", shop.getShopId())
                    .put("description", shop.getShopDescription())
                    .put("shopName", shop.getShopName())
                    .put("owner", shop.getOwner().getUserId());
            items.put(item);
        }
        response.put("shop", items);
        return response.toString();
    }

    @PostMapping("/findProducts")
    public String findProductsByKeyword(@RequestBody String request) {
        JSONObject response = new JSONObject();
        TypedQuery<Product> products = shopService.findProductByKeyword(new JSONObject(request));
        JSONArray items = new JSONArray();

        for (Product product : products.getResultList()) {
            JSONObject item = new JSONObject();
            String image = product.getProductImage().isEmpty() ? defaultIcon.defaultProductIcon()
                    : product.getProductImage().get(0).getImage();
            item.put("image", image)
                    .put("shopId", product.getShop().getShopId())
                    .put("description", product.getProductDescribe())
                    .put("shopName", product.getProductName())
                    .put("owner", product.getShop().getOwner().getUserId())
                    .put("price", product.getProductPrice());

            items.put(item);
        }
        response.put("products", items);
        return response.toString();
    }
}
