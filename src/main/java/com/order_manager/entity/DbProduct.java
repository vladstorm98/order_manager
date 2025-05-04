package com.order_manager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "products")
public class DbProduct {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true, nullable=false)
    private String name;

    private String description;

    private BigDecimal price;
}
