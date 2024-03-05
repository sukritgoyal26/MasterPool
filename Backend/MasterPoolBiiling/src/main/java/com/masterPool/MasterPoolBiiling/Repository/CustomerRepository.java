package com.masterPool.MasterPoolBiiling.Repository;

import com.masterPool.MasterPoolBiiling.Entity.Billing;
import com.masterPool.MasterPoolBiiling.Entity.CustomerInfo;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<CustomerInfo, String> {

    
    
}
