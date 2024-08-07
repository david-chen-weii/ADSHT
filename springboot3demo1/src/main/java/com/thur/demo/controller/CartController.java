package com.thur.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thur.demo.model.shopping.Cart;
import com.thur.demo.model.user.User;
import com.thur.demo.service.CartService;
import com.thur.demo.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin
@RequestMapping("/ajax/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;
    //post 請求
    @PostMapping("/orderlist")
        //泛型類 可以設置狀態碼 header等 //自訂方法  //請求header裡面提取session-id 並將其作為UUID的id
    public ResponseEntity<?> getMethodName(@RequestHeader("session-id") UUID id) {
        //                    //使用userService裡面的findById方法 來尋找userid
        Optional<User> user = userService.findById(id);
                            //使用cartService裡面的getCartsByUserId方法找到誰擁有購物車
        List<Cart> carts = cartService.getCartsByUserId(user.get());
        //創建一個JSONArray 可以用來存取多個購物車選項的JSON數據
        JSONArray cartArray = new JSONArray();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Random random = new Random();

        for (Cart cart : carts) {
            var cartlist = new JSONObject();

            // 生成訂單編號
            String formattedDate = dateFormat.format(cart.getPayDate());
            int randomNumber = random.nextInt(1000); // 生成0到999之間的隨機數字
            String orderId = String.format("ORD-%s-%03d", formattedDate, randomNumber);

            cartlist.put("cartId", orderId);
            cartlist.put("number", cart.getNumber());
            cartlist.put("product", cart.getProduct().getProductName());
            cartlist.put("payDate", dateFormat.format(cart.getPayDate()));
            cartlist.put("buyer", cart.getBuyer().getUserAccount());
            cartlist.put("price", cart.getProduct().getProductPrice());
            cartlist.put("seller", cart.getProduct().getShop().getOwner().getUserinfo().getUserLastName());

            cartArray.put(cartlist);

        }
        if (carts.isEmpty()) {
            JSONObject response = new JSONObject();
            response.put("error", "沒有訂單");
            return ResponseEntity.ok(response.toString());

        }
        return ResponseEntity.ok(cartArray.toString());
    }

}
