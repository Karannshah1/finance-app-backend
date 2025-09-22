package com.karan.finance.repository;

import com.karan.finance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

}