package com.order_manager.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "orders")
public class DbOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private DbUser user;

    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<DbProduct> products = new ArrayList<>();

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public DbOrder(DbUser user, List<DbProduct> products, Integer quantity, OrderStatus status) {
        this.user = user;
        this.products = products;
        this.quantity = quantity;
        this.status = status;
    }
}
