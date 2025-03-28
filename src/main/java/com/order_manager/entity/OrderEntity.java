package com.order_manager.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<ProductEntity> products = new ArrayList<>();

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public OrderEntity(UserEntity user, List<ProductEntity> products, Integer quantity, OrderStatus status) {
        this.user = user;
        this.products = products;
        this.quantity = quantity;
        this.status = status;
    }
}
