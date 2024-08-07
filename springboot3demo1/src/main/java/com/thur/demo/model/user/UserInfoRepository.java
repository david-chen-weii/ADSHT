package com.thur.demo.model.user;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thur.demo.model.shopping.Product;
import com.thur.demo.model.shopping.ProductImage;
import com.thur.demo.model.shopping.Shop;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer>, UserInfoRepositoryDao {

	@Query("SELECT u FROM UserInfo u WHERE CONCAT(u.userFirstName, u.userLastName) LIKE CONCAT('%', :name, '%') " +
			"OR u.userFirstName LIKE CONCAT('%', :name, '%') " +
			"OR u.userLastName LIKE CONCAT('%', :name, '%')")
	List<UserInfo> findByUserFirstNameOrUserLastNameOrFullNameContaining(@Param("name") String name);

	@Query("SELECT u FROM UserInfo u WHERE " +
			"(LOWER(u.userFirstName) LIKE LOWER(CONCAT('%', :firstName, '%')) AND LOWER(u.userLastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) "
			+
			"OR LOWER(CONCAT(u.userFirstName, u.userLastName)) LIKE LOWER(CONCAT('%', :fullName, '%')) " +
			"OR LOWER(CONCAT(u.userFirstName, ' ', u.userLastName)) LIKE LOWER(CONCAT('%', :fullName, '%')) " +
			"OR (u.userFirstName IS NULL AND LOWER(u.userLastName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
			"OR (LOWER(u.userFirstName) LIKE LOWER(CONCAT('%', :name, '%')) AND u.userLastName IS NULL)")
	List<UserInfo> findByUserFirstNameAndUserLastNameContaining(@Param("firstName") String firstName,
			@Param("lastName") String lastName,
			@Param("fullName") String fullName,
			@Param("name") String name);

	// 給個人頁面
	@Query("SELECT u FROM UserInfo u WHERE u.email = :email")
	List<UserInfo> findByEmail(@Param("email") String email);

	@Query("SELECT u FROM UserInfo u WHERE u.user = :user")
	UserInfo findByUser(@Param("user") User user);
	
    @Query("SELECT u FROM UserInfo u WHERE u.user.userId = :userId")
    UserInfo findByUserId(@Param("userId") UUID userId);
    
    //對應商城
    @Query("SELECT s FROM Shop s WHERE s.owner.userId = :userId")
    List<Shop> findShopsByUserId(@Param("userId") UUID userId);
    //商城對應商品
    @Query("SELECT p FROM Product p WHERE p.shop.shopId IN :shopIds")
    List<Product> findProductsByShopIds(@Param("shopIds") List<Integer> shopIds);
    //商品照片
    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.productId IN :productIds")
    List<ProductImage> findProductImagesByProductIds(@Param("productIds") List<Integer> productIds);

}
