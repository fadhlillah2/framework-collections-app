package com.kbni.backendcode.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "SPRING_SESSION")
public class SpringSession implements Serializable {

    @Id
    private String primaryId;
    private String sessionId;
    @Column(name = "token",columnDefinition = "TEXT")
    private String token;
    @Column(name = "jwtToken",columnDefinition = "TEXT")
    private String jwtToken;
    @Column(name = "refresh_token",columnDefinition = "TEXT")
    private String refreshToken;
    private String creationTime;
    @UpdateTimestamp
    private LocalDateTime lastAccessTime;
    private Long maxInactiveInterval;
    private String expiryTime;
    @Column(unique = true)
    private String principalName;
    private Integer isLogin;
    private Integer isExpired;
    @Column(unique = true)
    private String principalId;

    public SpringSession() {
        this.creationTime = String.valueOf(LocalDateTime.now());
        this.expiryTime = String.valueOf(calculateExpiryTime(LocalDateTime.parse(creationTime)));
    }

    private LocalDateTime calculateExpiryTime(LocalDateTime creationTime) {
        return creationTime.plusMinutes(55);

    }


}
