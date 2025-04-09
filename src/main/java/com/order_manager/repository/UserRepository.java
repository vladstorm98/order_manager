package com.order_manager.repository;

import com.order_manager.entity.DbUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<DbUser, Long> {
    Optional<DbUser> findByName(String name);
}
