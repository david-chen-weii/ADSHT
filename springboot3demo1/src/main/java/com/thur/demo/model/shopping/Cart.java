package com.thur.demo.model.shopping;

import java.util.List;

import com.thur.demo.model.user.User;

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
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartId")
    private Integer cartId;

    @ManyToOne
    @JoinColumn(name = "buyer", referencedColumnName = "userId")
    private User buyer;

    @Column(name = "number")
    private Integer number;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "[order]", referencedColumnName = "orderId")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product", referencedColumnName = "productId")
    private Product product;

    @Column(name = "payDay")
    private java.util.Date payDate;
}
