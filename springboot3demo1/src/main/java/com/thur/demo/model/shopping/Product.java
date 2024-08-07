package com.thur.demo.model.shopping;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId")
    private Integer productId;

    @ManyToOne
    @JoinColumn(name = "shop", referencedColumnName = "shopId")
    private Shop shop;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "productName")
    private String productName;

    @Column(name = "productPrice")
    private Double productPrice;

    @Column(name = "productDescribe")
    private String productDescribe;

    @OneToMany(mappedBy = "productImageId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImage;

}
