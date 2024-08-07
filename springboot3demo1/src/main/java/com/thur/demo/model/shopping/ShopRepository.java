package com.thur.demo.model.shopping;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.thur.demo.model.user.User;

public interface ShopRepository extends JpaRepository<Shop, Integer>, ShopDAO {

    Shop findByOwner(User owner);

}
