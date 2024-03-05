package com.masterPool.MasterPoolBiiling.Repository;

import com.masterPool.MasterPoolBiiling.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,String> {
  	
}
