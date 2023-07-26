package com.kbni.backendcode.repositories;

import com.kbni.backendcode.entities.SpringSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringSessionRepository extends JpaRepository<SpringSession, String> {

  SpringSession findByPrimaryIdAndPrincipalName(String id,String name);

  @Query("SELECT s FROM SpringSession s WHERE s.sessionId = :sessionId AND s.principalName = :name AND s.isExpired = 0" )
  SpringSession cekCekSessionFound(@Param("sessionId") String sessionId,@Param("name") String name);

  SpringSession findBySessionIdAndPrincipalName(String sessionId,String name);
  @Query(value = "SELECT e.* FROM spring_session e WHERE CAST(e.token AS VARCHAR(MAX)) = :token AND e.is_expired = 0", nativeQuery = true)
  SpringSession findByToken(@Param("token") String token);

  SpringSession findByPrincipalName(String username);

  @Query("SELECT s FROM SpringSession s WHERE s.principalName = :name AND s.isExpired = 0" )
  SpringSession findByPrincipalNames(String name);

}
