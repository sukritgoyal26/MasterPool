package com.masterPool.MasterPoolBiiling.Repository;

import com.masterPool.MasterPoolBiiling.Entity.Billing;
import com.masterPool.MasterPoolBiiling.Entity.CustomerInfo;
import com.masterPool.MasterPoolBiiling.Entity.Dues;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DuesRepository extends JpaRepository<Dues, Integer> {

    @Query("SELECT d FROM Dues d WHERE d.customerNumber = :customerNumber")
    List<Dues> findDuesByCustomerNumber(@Param("customerNumber") String customerNumber);

    @Query("SELECT d FROM Dues d WHERE d.billingBy = :billingBy")
    List<Dues> findDuesByCashierId(@Param("billingBy") String billingBy);

    
    
    
    
    @Query("SELECT d FROM Dues d WHERE d.billingBy = :billingBy AND d.date = :date AND d.status = 'due'")
    List<Dues> findDuesByCashierIdDate(@Param("billingBy") String billingBy, @Param("date") Date date);

    @Query("SELECT d FROM Dues d WHERE d.billingBy = :billingBy AND d.date = :date AND d.status = 'Repayment'")
    List<Dues> findRepaymentByCashierIdDate(@Param("billingBy") String billingBy, @Param("date") Date date);

    
    
    
    @Query("SELECT d FROM Dues d WHERE d.date = :date")
    List<Dues> findDuesByDate(@Param("date") Date date);

    @Query("SELECT d FROM Dues d WHERE d.customerNumber = :customerNumber")
    List<Dues> getCustomerDetailDues(@Param("customerNumber") String customerNumber);
    
    @Modifying
    @Query("DELETE FROM Dues d WHERE d.customerNumber = :customerNumber")
    void deleteByCustomerNumber(@Param("customerNumber") String customerNumber);

    


}
