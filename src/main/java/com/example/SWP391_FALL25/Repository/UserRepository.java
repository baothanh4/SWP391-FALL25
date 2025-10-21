package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByPhone(String phone);
    Optional<Users> findByEmail(String email);
}
