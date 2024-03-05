package com.masterPool.MasterPoolBiiling.Service;

import com.masterPool.MasterPoolBiiling.Entity.*;
import com.masterPool.MasterPoolBiiling.Repository.*;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TableRepository tableRepo;

    @Autowired
    private CustomerRepository customerRepo;
  
  
    
    
    public String createUser(User user) {

        String status = "";
        try {
            userRepo.save(user);

            return "User added successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to Create User";
        }

    }

    public User getUser(String userId) {
        Optional<User> opt = userRepo.findById(userId);
        if (opt.isPresent()) {
            User user = opt.get();
            return user;

        }
        return null;

    }
    
    
    

    public User validateUser(String userId, String pass) {
        Optional<User> opt = userRepo.findById(userId);
        if (opt.isPresent()) {
            User user = opt.get();
            String userPassword = user.getPassword();
            if (userPassword.equals(pass)) {
                return user;
            } else {
                return null;
            }
        }
        return null;
    }

    public String addCustomer(CustomerInfo customer) {

        try {
            String customerId=customer.getCustomerMobileNo();
            Optional<CustomerInfo> opt=customerRepo.findById(customerId);
            if(opt.isPresent())
            {
              return "Customer Already Exists";
            }
            
            customerRepo.save(customer);
            return "Customer added successfully";
            

        } catch (Exception e) {

            e.printStackTrace();
            return "Unable to add customer";

        }

    }

    public CustomerInfo getCustomer(String customerId) {
        Optional<CustomerInfo> opt = customerRepo.findById(customerId);
        if (opt.isPresent()) {
            CustomerInfo customer = opt.get();
            return customer;

        }
        return null;

    }
    
    
    
    
    public String createTable(TableInfo tableInfo) {
        try {
            tableInfo.setTableStaus("Free");
            tableRepo.save(tableInfo);
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to Create Table Missing Info";
        }
    }

    public String deleteTable(int tableId) {
        try {
            tableRepo.deleteById(tableId);
            return "Success";

        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to delete Table Missing Info";
        }
    }

    public List<TableInfo> viewTables() {
        List<TableInfo> totalTable = tableRepo.findAll();
        return totalTable;
    }

    public String updateTableStatus(int tableId, String status) {
        Optional<TableInfo> opt = tableRepo.findById(tableId);
        if (opt.isPresent()) {
            TableInfo tableInfo = opt.get();
            tableInfo.setTableStaus(status);
            return "Success";
        } else {
            return "Failed";
        }
    }

    public TableInfo viewTableById(int tableId) {
        Optional<TableInfo> opt = tableRepo.findById(tableId);
        if (opt.isPresent()) {
            TableInfo tableInfo = opt.get();
            return tableInfo;
        }
        return null;
    }
    
    public List<CustomerInfo> viewAllCustomer()
    {
        try{
                
         List<CustomerInfo> lst=customerRepo.findAll();
         return lst;
         
        
        }catch(Exception e)
        {
          e.printStackTrace();
          return null;
        }
     
    }
    
    public List<User> getAllUser()
    {
      List<User> lst=userRepo.findAll();
      return lst;
    }
    

}
