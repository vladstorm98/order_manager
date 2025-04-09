package com.order_manager.repository;

import com.order_manager.entity.DbProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<DbProduct, Long> {
}
