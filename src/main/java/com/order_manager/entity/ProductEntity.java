package com.order_manager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true, nullable=false)
    private String name;

    private String description;

    private double price;
}
