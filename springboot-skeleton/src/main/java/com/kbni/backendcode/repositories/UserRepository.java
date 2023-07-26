package com.kbni.backendcode.repositories;

import com.kbni.backendcode.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findUsersByUsername(String username);

    @Query(
            value = "SELECT * FROM users u where  u.username ilike concat('%', :query, '%')",
            nativeQuery = true
    )
    Page<Users> findAllUser(@Param("query") String query, Pageable pageable);
}
