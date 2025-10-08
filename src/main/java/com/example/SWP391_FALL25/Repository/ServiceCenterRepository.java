package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.ServiceCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ServiceCenterRepository extends JpaRepository<ServiceCenter,Long> {
}
