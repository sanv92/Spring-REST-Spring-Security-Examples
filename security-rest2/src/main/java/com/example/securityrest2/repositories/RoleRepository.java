package com.example.securityrest2.repositories;

import com.example.securityrest2.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(Role.Type type);


    // @Query("select s.id as serviceId, s.name as serviceName, us.rating.id as ratingId from UserService us join us.service s where us.user.id = ?1")
    // @Query("select '*' from UserService us join us.service s where us.user.id = ?1")
//    @Query(value = "SELECT '*' FROM Role role join role. users WHERE users.id = ?1")
//    List<Role> findRolesByUserId(Long id);
}
