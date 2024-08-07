package com.thur.demo.model.shopping;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer>, ProductDAO {

    List<Product> findByShop(Shop shop);
}
