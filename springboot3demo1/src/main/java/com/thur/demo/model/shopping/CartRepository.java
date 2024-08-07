package com.thur.demo.model.shopping;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import com.thur.demo.model.user.User;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    List<Cart> findByBuyer(User buyer);

    List<Cart> findByBuyerAndStatus(User buyer, Integer status);
}
