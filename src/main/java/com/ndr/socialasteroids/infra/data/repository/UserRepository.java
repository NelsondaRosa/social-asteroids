package com.ndr.socialasteroids.infra.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ndr.socialasteroids.domain.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    @Query(value = "SELECT * FROM users u WHERE LOWER(u.username) LIKE '%' || LOWER(:search) || '%'", nativeQuery = true)
    List<User> searchByUsername(@Param("search") String search);
}
