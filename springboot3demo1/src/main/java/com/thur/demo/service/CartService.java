package com.thur.demo.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thur.demo.model.shopping.Cart;
import com.thur.demo.model.shopping.CartRepository;
import com.thur.demo.model.user.User;
import com.thur.demo.model.user.UserRepository;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public Cart getCartById(Integer cartId) {
        return cartRepository.findById(cartId).orElse(null);
    }
        // 返回類型是List<Cart>        接收一個User型別user參數
    public List<Cart> getCartsByUserId(User user) {
        // 获取所有购物车项                  自訂方法在Repository裡面找尋買家跟狀態
        List<Cart> allCarts = cartRepository.findByBuyerAndStatus(user, 1);
        return allCarts;
        //返回符合條件的購物車列表

    }
}
