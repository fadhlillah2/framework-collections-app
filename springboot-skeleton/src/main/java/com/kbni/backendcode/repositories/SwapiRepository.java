package com.kbni.backendcode.repositories;

import com.kbni.backendcode.entities.Swapis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SwapiRepository extends JpaRepository<Swapis,Long> {

    @Query(
            value = "SELECT * FROM swapis s where  s.name ilike concat('%', :query, '%')",
            nativeQuery = true
    )
    Page<Swapis> findAllSwapi(@Param("query") String query, Pageable pageable);
}
