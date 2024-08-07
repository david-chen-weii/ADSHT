package com.thur.demo.model.shopping;

import java.util.List;

import com.thur.demo.model.user.User;

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
@Table(name = "shop")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shopId")
    private Integer shopId;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "userId")
    private User owner;

    @Column(name = "shopName")
    private String shopName;

    @Column(name = "shopDescription")
    private String shopDescription;

    @Column(name = "shopImage", columnDefinition = "VARCHAR(MAX)")
    private String image;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
}
