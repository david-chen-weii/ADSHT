package com.thur.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thur.demo.model.shopping.Cart;
import com.thur.demo.model.shopping.CartRepository;
import com.thur.demo.model.shopping.Order;
import com.thur.demo.model.shopping.OrderRepository;
import com.thur.demo.model.shopping.Product;
import com.thur.demo.model.shopping.ProductImage;
import com.thur.demo.model.shopping.ProductImageRepositroy;
import com.thur.demo.model.shopping.ProductRepository;
import com.thur.demo.model.shopping.Shop;
import com.thur.demo.model.shopping.ShopRepository;
import com.thur.demo.model.user.User;

import jakarta.persistence.TypedQuery;

@Service
public class ShopService {
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepositroy productImageRepositroy;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;

    public Shop openNewShop(JSONObject body, User user) {
        Shop newShop = new Shop();
        newShop.setShopName(body.getString("shopName"));
        try {
            newShop.setImage(body.getString("image"));
        } catch (Exception e) {
            newShop.setImage(null);
        }

        newShop.setShopDescription(body.getString("description"));
        newShop.setOwner(user);
        Shop res = shopRepository.save(newShop);

        return res;
    }

    public void closeShop(Shop shop) {
        shopRepository.delete(shop);

    }

    public Shop editShop(Shop shop, JSONObject request) {
        shop.setImage(request.getString("image"));
        shop.setShopDescription(request.getString("description"));
        shop.setShopName(request.getString("shopName"));
        Shop res = shopRepository.save(shop);

        return res;
    }

    public Product addNewProduct(JSONObject body, Shop shop) {
        Product newProduct = new Product();
        newProduct.setProductDescribe(body.getString("description"));
        newProduct.setProductName(body.getString("productName"));
        newProduct.setStock(body.getInt("stock"));
        newProduct.setProductPrice(body.getDouble("price"));
        newProduct.setShop(shop);
        Product res = productRepository.save(newProduct);
        // 圖片
        try {
            String[] images = body.getString("image").trim().split(" ");
            for (String image : images) {
                ProductImage newProductImage = new ProductImage();
                newProductImage.setImage(image);
                newProductImage.setProduct(res);
                productImageRepositroy.save(newProductImage);

            }

        } catch (Exception e) {

        }

        return res;

    }

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    public Product editProduct(Product product, JSONObject request) {
        product.setProductDescribe(request.getString("productDescription"));
        product.setProductPrice(request.getDouble("price"));
        product.setStock(request.getInt("stock"));
        product.setProductName(request.getString("productName"));
        // 圖片
        ProductImage image = product.getProductImage().isEmpty() ? new ProductImage()
                : product.getProductImage().get(0);
        String images = request.getString("image");
        image.setImage(images);
        image.setProduct(product);
        productImageRepositroy.save(image);
        return productRepository.save(product);
    }

    public Cart addProductToCart(Product product, User user) {
        Cart newCart = new Cart();
        newCart.setBuyer(user);
        newCart.setProduct(product);
        newCart.setStatus(0);
        newCart.setNumber(1);
        Cart res = cartRepository.save(newCart);
        return res;
    }

    public Cart editCart(Cart cart) {

        Cart res = cartRepository.save(cart);
        return res;
    }

    public String delete(Cart cart) {
        if (cart.getOrder() != null) {
            return "以結帳";
        }
        cartRepository.delete(cart);
        return "刪除";
    }

    public JSONObject buyThing(List<Cart> carts) {
        JSONObject response = new JSONObject();
        Order order = new Order();
        Order newOrder = orderRepository.save(order);

        for (Cart cart : carts) {
            cart.setPayDate(new java.util.Date());
            cart.setStatus(1);
            cart.setOrder(newOrder);
            Product product = cart.getProduct();
            product.setStock(product.getStock() - cart.getNumber());
            productRepository.save(product);
            cartRepository.save(cart);
        }
        response.put("success", "結帳成功");
        return response;
    }

    public List<Shop> findAllShop() {
        return shopRepository.findAll();
    }

    public Optional<Shop> findOneShopById(int id) {

        return shopRepository.findById(id);
    }

    public TypedQuery<Shop> findShopByKeyword(JSONObject keyword) {

        return shopRepository.findBykeyWord(keyword);
    }

    public Optional<Product> findOneProductById(int id) {
        return productRepository.findById(id);
    }

    public TypedQuery<Product> findProductByKeyword(JSONObject keyword) {
        return productRepository.findBykeyWord(keyword);
    }

    public Optional<Cart> findCartById(int id) {
        return cartRepository.findById(id);
    }

    public List<Cart> findCartByUserAndStaus(User user, Integer status) {
        return cartRepository.findByBuyerAndStatus(user, status);
    }

    public Shop findShopByOwner(User user) {
        return shopRepository.findByOwner(user);
    }
}
