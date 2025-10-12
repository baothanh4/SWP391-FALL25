package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.ServiceReportDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceReportDetailsRepository extends JpaRepository<ServiceReportDetails,Long> {
    @Query("SELECT SUM(d.totalCost) FROM ServiceReportDetails d WHERE d.report.id = :reportId")
    Double calculateTotalByReportId(@Param("reportId") Long reportId);

}
