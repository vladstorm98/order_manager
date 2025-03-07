package com.order_manager.repository;

import com.order_manager.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByIdIn(List<Long> listOfProductId);

    Optional<ProductEntity> findByName(String name);
}
