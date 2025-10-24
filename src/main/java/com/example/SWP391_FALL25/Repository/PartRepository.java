package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartRepository extends JpaRepository<Part,Long> {
    Optional<Part> findTopByPartType_Name(String partTypeName);
}
