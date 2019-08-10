package com.example.securityrest2.repositories;

import com.example.securityrest2.domain.Role;
import com.example.securityrest2.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT user.roles FROM User user join user.roles WHERE user.id = ?1")
    Set<Role> findUserWithRoles(Long id);
}
