package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findFirstByEmailAndCodeAndUsedFalse(String email,String code);
}
