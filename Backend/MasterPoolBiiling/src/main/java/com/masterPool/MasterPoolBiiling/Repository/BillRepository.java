package com.masterPool.MasterPoolBiiling.Repository;

import com.masterPool.MasterPoolBiiling.Entity.Billing;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillRepository extends JpaRepository<Billing, Integer> {

    @Query("SELECT b FROM Billing b WHERE b.billingBy = :billingBy AND b.date = :date")
    List<Billing> findByBillingByAndDate(@Param("billingBy") String billingBy,
                                         @Param("date") Date date);

    @Query("SELECT b FROM Billing b WHERE b.billingBy = :billingBy AND b.date BETWEEN :startDate AND :endDate")
    List<Billing> findByBillingByAndDateRange(@Param("billingBy") String billingBy,
                                              @Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate);

    
    @Query("SELECT b FROM Billing b WHERE  b.date BETWEEN :startDate AND :endDate")
    List<Billing> findBillCustomDate(@Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate);
    
    
      @Query("SELECT b FROM Billing b WHERE b.date = :date")
     List<Billing> getTodayBills(@Param("date") Date date);

      @Query(value = "SELECT * FROM Billing ORDER BY DATE(date) DESC LIMIT 5", nativeQuery = true)
      List<Billing> findTop5ByOrderByDateDesc();
    
    
}
