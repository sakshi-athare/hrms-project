package com.hrms.user.repository;

import com.hrms.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>,JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    
    long countByIsActiveTrue();
    long countByIsActiveFalse();
    long countByCreatedAtAfter(LocalDateTime date);

}