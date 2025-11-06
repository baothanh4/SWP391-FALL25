package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.Users;
import com.example.SWP391_FALL25.Enum.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByPhone(String phone);
    Optional<Users> findByEmail(String email);
    List<Users> findByRole(Role role);
    Optional<Users> findByIdAndRole(Long id, Role role);
    Optional<Users> findByFullnameAndRole(String fullname, Role role);
}
