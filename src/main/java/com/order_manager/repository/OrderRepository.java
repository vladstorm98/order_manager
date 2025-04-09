package com.order_manager.repository;

import com.order_manager.entity.DbOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<DbOrder, Long> {
    List<DbOrder> findByUserName(String name);
}
