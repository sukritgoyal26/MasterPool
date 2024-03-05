package com.masterPool.MasterPoolBiiling.Service;

import com.masterPool.MasterPoolBiiling.Entity.Billing;
import com.masterPool.MasterPoolBiiling.Entity.Dues;
import com.masterPool.MasterPoolBiiling.Entity.TableInfo;
import com.masterPool.MasterPoolBiiling.Repository.BillRepository;
import com.masterPool.MasterPoolBiiling.Repository.DuesRepository;
import com.masterPool.MasterPoolBiiling.Repository.TableRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillingService {

    @Autowired
    private BillRepository billRepo;

    @Autowired
    private TableRepository tableRepo;

    @Autowired
    private DuesRepository dueRepo;

    public String createBill(Billing bill) {
        try {
            billRepo.save(bill);
            return "Bill Created";
        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to generate Bill";
        }
    }

    public String createBillDue(Dues bill) {

        try {
            dueRepo.save(bill);
            return "Dues updated";
        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to update due";
        }
    }

    public int calculateBill(int time, int tableId) {

        Optional<TableInfo> opt = tableRepo.findById(tableId);
        int price = 0;
        if (opt.isPresent()) {
            TableInfo tableInfo = opt.get();
            int pricePerMin = (tableInfo.getPricePerHours()) / 60;

            if (time <= 5) {
                price = 10;
                return price;
            } else if (time > 5 && time <= 30) {

                price = pricePerMin * 30;

            } else {
                price = time * pricePerMin;
            }

            price = (int) (Math.ceil(price / 5) * 5);

        } else {
            System.out.println("Table ID not found");

        }

        System.out.println("Table Id:" + tableId);
        System.out.println("Time: " + time);
        System.out.println("Price:" + price);
        return price;
    }

    // Billing
    public List<Billing> getTodaysCollectionById(String cashierId) {
        Date date = new Date();
        List<Billing> bills = billRepo.findByBillingByAndDate(cashierId, date);
        return bills;
    }

    public List<Billing> getBillByIdWithDateRange(String cashierId, Date startDate, Date endDate) {

        List<Billing> bills = billRepo.findByBillingByAndDateRange(cashierId, startDate, endDate);
        return bills;
    }
    
    public List<Billing> getTodaysCollection() {
        Date date = new Date();
        List<Billing> bills = billRepo.getTodayBills(date);
        return bills;
    }

    public List<Billing> findBillCustomDate(Date startDate, Date endDate) {
        List<Billing> bills = billRepo.findBillCustomDate(startDate, endDate);
        return bills;
    }
    
    // Dues Billing
    public List<Dues> getTodaysDuesByCashierId(String cashierId) {

        List<Dues> dues = dueRepo.findDuesByCashierIdDate(cashierId, new Date());
        return dues;
    }

    public List<Dues> getDuesByCashierId(String cashierId) {

        List<Dues> dues = dueRepo.findDuesByCashierId(cashierId);
        return dues;
    }

    public List<Dues> getTotalDues() {
        List<Dues> dues = dueRepo.findAll();
        return dues;
    }

    public List<Dues> getTodaysDues() {
        List<Dues> dues = dueRepo.findDuesByDate(new Date());
        return dues;
    }

    public List<Dues> getCustomerTotalDues(String customerNumber) {
        System.out.println("Customer detail:" + customerNumber);
        List<Dues> dues = dueRepo.getCustomerDetailDues(customerNumber);
        return dues;
    }
    
    public String clearDues(String customerNumber) {
        try {
            dueRepo.deleteByCustomerNumber(customerNumber);
            return "Success";

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }
    
    public List<Dues> getRepaymentDetails(String userId) {
        List<Dues> dues = dueRepo.findRepaymentByCashierIdDate(userId, new Date());
        return dues;
    }
    public int customerTotalDues(String customerNumber)
    {
     List<Dues> dues=getCustomerTotalDues(customerNumber);
     int totalDues=0;
     
     for(Dues due:dues)
     {
       totalDues=totalDues+due.getAmount();
     }
     
     return totalDues;
        
        
    }
    public List<Billing> getRecentBills()
    {
     List<Billing> lst=billRepo.findTop5ByOrderByDateDesc();
     return lst;
    }
    
}
