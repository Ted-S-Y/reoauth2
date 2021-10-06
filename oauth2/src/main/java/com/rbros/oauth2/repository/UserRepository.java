package com.rbros.oauth2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rbros.oauth2.entity.User;

/**
 * DAO (Data Access Object) 클래스
 * 
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
 
    Optional<User> findByMobile(String mobl);
 
    Boolean existsByMobile(String mobl);
    
    Optional<User> findByMobl(String mobl);
    
    Boolean existsByMobl(String mobl);
}