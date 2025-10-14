package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.PartType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartTypeRepository extends JpaRepository<PartType, Long> {
}