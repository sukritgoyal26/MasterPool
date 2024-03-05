package com.masterPool.MasterPoolBiiling.Controller;

import com.masterPool.MasterPoolBiiling.Entity.Billing;
import com.masterPool.MasterPoolBiiling.Entity.CustomerInfo;
import com.masterPool.MasterPoolBiiling.Entity.Dues;
import com.masterPool.MasterPoolBiiling.Entity.RequestBilling;
import com.masterPool.MasterPoolBiiling.Entity.TableInfo;
import com.masterPool.MasterPoolBiiling.Entity.User;
import com.masterPool.MasterPoolBiiling.Service.BillingService;
import com.masterPool.MasterPoolBiiling.Service.UserService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillingController {

    @Autowired
    private BillingService billService;

    @Autowired
    private UserService service;

    //User
    @GetMapping("/user/getCustomerDetail")
    public ResponseEntity<CustomerInfo> getCustomerDetail(@RequestParam("customerMobile") String customerMobile) {
        String status;
        try {
            CustomerInfo customer = service.getCustomer(customerMobile);
            if (customer.getCustomerMobileNo().length() > 0) {
                status = "Success";
                return ResponseEntity.status(HttpStatus.OK).header("Billing-Status", status).body(customer);
            } else {
                status = "Customer does not exists";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Billing-Status", status).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();

            status = "Unable to fetch customer please try again later";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Billing-Status", status).body(null);
        }
    }

    //URL: http://localhost:8080/login/validateUser
    @PostMapping("/login/validateUser")
    public User validateLogin(@RequestParam("userId") String userId,
            @RequestParam("password") String password,
            Model model) {
        User user = service.validateUser(userId, password);
        String status;
        if (user == null) {
            status = "Invalid User Id or Password";
            model.addAttribute("msg", status);
        } else {
            List<TableInfo> lst = service.viewTables();
            model.addAttribute("user", user);
            model.addAttribute("tableList", lst);
            return user;
        }
        return null;
    }

    //URL: http://localhost:8080/user/validate/createAccount
    @PostMapping("/user/validate/createAccount")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        String status;
        try {

            status = service.createUser(user);

            return ResponseEntity.status(HttpStatus.OK).header("Status", status).body(status);

        } catch (Exception e) {
            e.printStackTrace();
            status = "Unable to Add user try with different user id";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Status", status).body(status);
        }
    }

    @GetMapping("/login/userSession/{userId}")
    public String userInfo(@PathVariable("userId") String userId, Model model) {
        User user = service.getUser(userId);
        String status;
        if (user == null) {
            status = "Invalid User Id or Password";
            model.addAttribute("msg", status);
            return "status";
        } else {
            List<TableInfo> lst = service.viewTables();
            model.addAttribute("user", user);
            model.addAttribute("tableList", lst);
            return "userDashboard";
        }
    }

    // URL:http://localhost:8080/billing/admin/addTable
    @PostMapping("/billing/admin/addTable")
    public String addTable(@RequestParam("tableName") String tableName,
            @RequestParam("price") int price,
            @RequestParam("userId") String userId) {

        User user = service.getUser(userId);
        String userType = user.getUserType();
        String status = "";
        if (userType.equalsIgnoreCase("admin")) {
            TableInfo table = new TableInfo();
            table.setTableInfo(tableName);
            table.setPricePerHours(price);
            status = service.createTable(table);
            return status;
        } else {
            status = "You dont have permission to add table";
        }
        return status;
    }

    // URL:http://localhost:8080/billing/admin/deleteTable
    @PostMapping("/billing/admin/deleteTable")
    public String deleteable(@RequestParam("tableId") String tableId1,
            @RequestParam("userId") String userId) {

        User user = service.getUser(userId);
        int tableId = Integer.parseInt(tableId1);
        String userType = user.getUserType();
        String status = "";
        if (userType.equalsIgnoreCase("admin")) {
            status = service.deleteTable(tableId);
            return status;
        } else {
            status = "You dont have permission to delete table";
        }
        return status;
    }

    // URL:http://localhost:8080/user/addCustomer
    @PostMapping("/user/addCustomer")
    public ResponseEntity<String> addCustomer(@RequestBody CustomerInfo customer) {
        String status;
        try {
            status = service.addCustomer(customer);
            return ResponseEntity.status(HttpStatus.OK).header("Status", status).body(status);
        } catch (Exception e) {
            e.printStackTrace();
            status = "Unable to add customer try again after some time";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Status", status).body(status);
        }
    }

    // URL:http://localhost:8080/user/getCustomer  
    @GetMapping("/user/getCustomer")
    public ResponseEntity<List<CustomerInfo>> getCustomer() {
        String status;
        try {
            List<CustomerInfo> customerList = service.viewAllCustomer();
            status = "customer list succesfully fetched";
            return ResponseEntity.status(HttpStatus.OK).header("Status", status).body(customerList);
        } catch (Exception e) {
            e.printStackTrace();
            status = "Unable to fetch customer try again after some time";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Status", status).body(null);
        }
    }

    //URL:http://localhost:8080/club/tables
    @GetMapping("/club/tables")
    public List<TableInfo> getAllTables(Model model) {
        List<TableInfo> lst = service.viewTables();
        if (lst != null && lst.size() > 0) {
            System.out.println(lst.size());
            return lst;
        }
        return null;
    }

    // Billing
    @PostMapping("/bill/save")
    public ResponseEntity<Billing> getBillingAmount(@RequestBody RequestBilling billingRequest) {
        try {

            String status;
            Billing billing = new Billing();
            int amount = billService.calculateBill(billingRequest.getMinutes(), billingRequest.getTableNo());
            TableInfo tableInfo = service.viewTableById(billingRequest.getTableNo());
            System.out.println(amount);
            CustomerInfo customer = service.getCustomer(billingRequest.getCustomerName());
            billing.setCustomerName(billingRequest.getCustomerName() + " new customer");
            if (customer != null) {
                System.out.println("Customer found");
                billing.setCustomerName(customer.getCustomerName());

            }

            billing.setDescription(tableInfo.getTableInfo() + " played " + billingRequest.getMinutes() + " min");
            billing.setBillAmount(amount);
            billing.setTotalTimePlayed(billingRequest.getMinutes());
            billing.setDate(new Date());
            billing.setBillingMode(billingRequest.getBillingMode());
            billing.setCustomerNumer(billingRequest.getCustomerName());
            billing.setBillingBy(billingRequest.getCashierUserId());

            if ((billingRequest.getBillingMode().equalsIgnoreCase("due")) && customer != null) {

               int customerTotaldues=billService.customerTotalDues(customer.getCustomerMobileNo());
               
                
                int customerCreditAllowance = customer.getMaxCreditLimit() - amount-customerTotaldues;
                System.out.println("Credit-"+customerCreditAllowance);
                if (customerCreditAllowance<=0) {
                    billing.setBillingMode("cash");
                    String status1 = billService.createBill(billing);
                    status = "Customer reached creadit limit bill is converted into cash";
                    
                
                } else {

                    Dues due = new Dues();
                    due.setCustomerName(customer.getCustomerName());
                    due.setCustomerNumber(billingRequest.getCustomerName());
                    due.setStatus(billingRequest.getBillingMode());
                    due.setAmount(amount);
                    due.setDescription(tableInfo.getTableInfo() + " played " + billingRequest.getMinutes() + " min");
                    due.setDate(new Date());
                    due.setBillingBy(billingRequest.getCashierUserId());
                    status = billService.createBillDue(due);
                }
            } else {

                billing.setBillingMode("cash");
                status = billService.createBill(billing);
            }

            if (customer == null && billingRequest.getBillingMode().equalsIgnoreCase("due")) {
                status = "Customer is not not eligiable for due";
            }

            billing.setStatus(status);
            return ResponseEntity.status(HttpStatus.OK).header("Billing-Status", status).body(billing);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/user/payDues")
    public String payBill(@RequestParam("customerNumber") String customerNumber,
            @RequestParam("userId") String userId,
            @RequestParam("amount") int amount,
            @RequestParam("paymentMode") String paymentMode) {
        try {
            String status = "";
            Dues due = new Dues();
            int finalAmount = -amount;
            CustomerInfo customer = service.getCustomer(customerNumber);
            due.setCustomerName(customer.getCustomerName());
            due.setDate(new Date());
            due.setDescription("Repayment");
            due.setStatus("Repayment");
            due.setBillingBy(userId);
            due.setAmount(finalAmount);
            due.setCustomerNumber(customerNumber);
            status = billService.createBillDue(due);
            System.out.println(status);
            Billing bill = new Billing();
            bill.setCustomerNumer(customerNumber);
            bill.setBillingBy(userId);
            bill.setDate(new Date());
            bill.setBillingMode(paymentMode);
            bill.setDescription("Repayment");
            bill.setTotalTimePlayed(0);
            bill.setCustomerName(customer.getCustomerName());
            bill.setBillAmount(amount);
            status = billService.createBill(bill);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to add bill";
        }
    }

    // Bill Reports
    //URL:http://localhost:8080/billing/getTodayCollection
    @GetMapping("/billing/getTodayCollection")
    public ResponseEntity<Map<String, Object>> getTodayCollection(@RequestParam("cashierId") String cashierId) {
        List<Billing> billingList = billService.getTodaysCollectionById(cashierId);
        List<Dues> dues = billService.getTodaysDuesByCashierId(cashierId);
        List<Dues> duesRepaymentList = billService.getRepaymentDetails(cashierId);
        System.out.println(duesRepaymentList);
        System.out.println("Cashier Id:" + cashierId);
        String status = "success";
        System.out.println(billingList.size());
        Integer upiCollection = 0;
        Integer cashCollection = 0;
        Integer dueCustomerBill = 0;
        Integer totalCollection = 0;
        for (Billing b : billingList) {
            String billMode = b.getBillingMode();
            if (billMode.equals("paytm"));
            {
                upiCollection = upiCollection + b.getBillAmount();
            }
            if (billMode.equals("cash")) {
                cashCollection = cashCollection + b.getBillAmount();
            }
        }
        for (Dues due : dues) {
            dueCustomerBill = dueCustomerBill + due.getAmount();
        }
        totalCollection = cashCollection + upiCollection;
        Map<String, Object> map = new HashMap<>();
        map.put("BillList", billingList);
        map.put("upiCollection", upiCollection);
        map.put("cashCollection", cashCollection);
        map.put("billingList", billingList);
        map.put("dueCustomerBill", dueCustomerBill);
        map.put("totalCollection", totalCollection);
        return ResponseEntity.status(HttpStatus.OK).header("Billing-Status", status).body(map);
    }

    //URL:http://localhost:8080/billing/getCollectionByUserWithDate
    @GetMapping("/billing/getCollectionByUserWithDate")
    public ResponseEntity<Map<String, Object>> getCollectionByUserWithDate(@RequestParam("cashierId") String cashierId,
            @RequestParam("startDate") String startDate1,
            @RequestParam("endDate") String endDate1) throws ParseException {

        System.out.println(startDate1);
        System.out.println(endDate1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(startDate1);
        Date endDate = dateFormat.parse(endDate1);
        List<Billing> billingList = billService.getBillByIdWithDateRange(cashierId, startDate, endDate);
        System.out.println("Cashier Id:" + cashierId);
        String status = "success";
        Integer upiCollection = 0;
        Integer cashCollection = 0;
        Integer totalCollection = 0;
        for (Billing b : billingList) {
            String billMode = b.getBillingMode();
            if (billMode.equals("paytm"));
            {
                upiCollection = upiCollection + b.getBillAmount();
            }
            if (billMode.equals("cash")) {
                cashCollection = cashCollection + b.getBillAmount();
            }
        }
        totalCollection = cashCollection + upiCollection;
        Map<String, Object> map = new HashMap<>();
        map.put("BillList", billingList);
        map.put("upiCollection", upiCollection);
        map.put("cashCollection", cashCollection);
        map.put("billingList", billingList);
        map.put("totalCollection", totalCollection);
        return ResponseEntity.status(HttpStatus.OK).header("Billing-Status", status).body(map);
    }

    //URL:http://localhost:8080/billing/getCollectionByCustomDate
    @GetMapping("/billing/getCollectionByCustomDate")
    public ResponseEntity<Map<String, Object>> getCollectionByCustomDate(@RequestParam("startDate") String startDate1,
            @RequestParam("endDate") String endDate1) throws ParseException {

        System.out.println(startDate1);
        System.out.println(endDate1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(startDate1);
        Date endDate = dateFormat.parse(endDate1);
        List<Billing> billingList = billService.findBillCustomDate(startDate, endDate);
        String status = "Success";
        Integer upiCollection = 0;
        Integer cashCollection = 0;
        Integer totalCollection = 0;
        for (Billing b : billingList) {
            String billMode = b.getBillingMode();
            if (billMode.equals("paytm"));
            {
                upiCollection = upiCollection + b.getBillAmount();
            }
            if (billMode.equals("cash")) {
                cashCollection = cashCollection + b.getBillAmount();
            }
        }
        totalCollection = cashCollection + upiCollection;
        Map<String, Object> map = new HashMap<>();
        map.put("BillList", billingList);
        map.put("upiCollection", upiCollection);
        map.put("cashCollection", cashCollection);
        map.put("billingList", billingList);
        map.put("totalCollection", totalCollection);
        return ResponseEntity.status(HttpStatus.OK).header("Billing-Status", status).body(map);
    }

    //URL:http://localhost:8080/billing/getTodayCollectionReport
    @GetMapping("/billing/getTodayCollectionReport")
    public ResponseEntity<Map<String, Object>> getTodayCollectionReport() throws ParseException {
        List<Billing> billingList = billService.getTodaysCollection();
        List<Dues> dues = billService.getTodaysDues();
        String status = "Success";
        Integer upiCollection = 0;
        Integer cashCollection = 0;
        Integer totalCollection = 0;
        Integer todaysDue = 0;
        Integer todaysRepayment = 0;
        for (Billing b : billingList) {
            String billMode = b.getBillingMode();
            if (billMode.equals("paytm"));
            {
                upiCollection = upiCollection + b.getBillAmount();
            }
            if (billMode.equals("cash")) {
                cashCollection = cashCollection + b.getBillAmount();
            }
        }
        for (Dues due : dues) {
            if (due.getStatus().equalsIgnoreCase("Repayment")) {
                todaysRepayment = todaysRepayment + due.getAmount();
            } else {
                todaysDue = todaysDue + due.getAmount();
            }
        }
        todaysRepayment = todaysRepayment * -1;
        totalCollection = cashCollection + upiCollection;
        System.out.println(billingList.size());
        System.out.println("Total collection-" + totalCollection);
        Map<String, Object> map = new HashMap<>();
        map.put("BillList", billingList);
        map.put("upiCollection", upiCollection);
        map.put("cashCollection", cashCollection);
        map.put("billingList", billingList);
        map.put("todaysDue", todaysDue);
        map.put("todaysRepayment", todaysRepayment);
        map.put("totalCollection", totalCollection);
        return ResponseEntity.status(HttpStatus.OK).header("Billing-Status", status).body(map);
    }

    // Bill Collection
    // URL:http://localhost:8080/billing/getTodayBillAmount
    @GetMapping("/billing/getTodayBillAmount")
    public Map<String, Integer> getTodayBillAmount(@RequestParam("cashierId") String cashierId) {
        List<Billing> billingList = billService.getTodaysCollectionById(cashierId);
        Map<String, Integer> map = new HashMap<>();
        List<Dues> dues = billService.getTodaysDuesByCashierId(cashierId);
        List<Dues> dueRepaymentList = billService.getRepaymentDetails(cashierId);
        int todayCashCollection = 0;
        int todayPaytmCollection = 0;
        int totalCollection = 0;
        int dueCustomerBill = 0;
        int dueRepayment = 0;
        if (billingList.size() > 0) {
            for (Billing bill : billingList) {
                String billMode = bill.getBillingMode();
                if (billMode.equalsIgnoreCase("paytm")) {
                    todayPaytmCollection = todayPaytmCollection + bill.getBillAmount();
                } else {
                    todayCashCollection = todayCashCollection + bill.getBillAmount();
                }
            }
            for (Dues due : dues) {
                dueCustomerBill = dueCustomerBill + due.getAmount();
            }
            for (Dues due : dueRepaymentList) {
                dueRepayment = dueRepayment + due.getAmount();
            }
            dueRepayment = dueRepayment * -1;
            totalCollection = todayCashCollection + todayPaytmCollection;
            map.put("todayCashCollection", todayCashCollection);
            map.put("todayPaytmCollection", todayPaytmCollection);
            map.put("dueCustomerBill", dueCustomerBill);
            map.put("dueRepayment", dueRepayment);
            map.put("totalCollection", totalCollection);
        }
        return map;
    }

    // URL:http://localhost:8080/user/getTodaysDues 
    @GetMapping("/user/getTodaysDues")
    public ResponseEntity<List<Dues>> getTodaysDuesByCashierId() {
        String status;
        try {
            List<Dues> customerDuesList = billService.getTotalDues();
            System.out.println("Todays");
            System.out.println(customerDuesList);
            List<String> uniqueCustomerNumbers = customerDuesList.stream().map(Dues::getCustomerNumber).distinct().collect(Collectors.toList());
            List<Dues> finalDuesCustomerList = new ArrayList<>();
            System.out.println(uniqueCustomerNumbers.size());
            for (int i = 0; i < uniqueCustomerNumbers.size(); i++) {
                String customerNumber = uniqueCustomerNumbers.get(i);
                int totalDues = 0;
                Dues due = new Dues();
                CustomerInfo customer = service.getCustomer(customerNumber);
                due.setCustomerName(customer.getCustomerName());
                due.setCustomerNumber(customerNumber);
                due.setDescription("Multiple Bills");
                due.setDate(new Date());
                due.setStatus("due");
                for (Dues duesList : customerDuesList) {
                    if (duesList.getCustomerNumber().equals(customerNumber)) {
                        totalDues = totalDues + duesList.getAmount();
                        System.out.println(duesList);
                    }
                }
                due.setAmount(totalDues);
                if (totalDues > 0) {
                    finalDuesCustomerList.add(due);
                }
            }
            status = "customer list succesfully fetched";
            return ResponseEntity.status(HttpStatus.OK).header("Status", status).body(finalDuesCustomerList);
        } catch (Exception e) {
            e.printStackTrace();
            status = "Unable to fetch customer try again after some time";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Status", status).body(null);
        }
    }

    // URL:http://localhost:8080/user/getTodaysCustomerDuesDetails 
    @GetMapping("/user/getTodaysCustomerDuesDetails")
    public ResponseEntity<List<Dues>> getTodaysCustomerDuesDetails(@RequestParam("customerNumber") String customerNumber) {
        String status;
        try {
            List<Dues> customerDuesList = billService.getCustomerTotalDues(customerNumber);
            status = "customer list succesfully fetched";
            System.out.println(customerDuesList.size());
            Dues dues = customerDuesList.get(0);
            System.out.println(dues.getDescription());
            return ResponseEntity.status(HttpStatus.OK).header("Status", status).body(customerDuesList);
        } catch (Exception e) {
            e.printStackTrace();
            status = "Unable to fetch customer try again after some time";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Status", status).body(null);
        }
    }
    //URL: http://localhost:8080/user/getAllUserDetail
    @GetMapping("/user/getAllUserDetail")
   public List<User> getAllUserDetail() {
       List<User> user = service.getAllUser();
       if (user != null && user.size() > 0) {
           return user;
       } else {
           return null;
       }

   }
   
  //URL:http://localhost:8080/user/payBillManual 
    @PostMapping("/user/payBillManual")
    public String payBillManual(@RequestParam("customerNumber") String customerNumber,
            @RequestParam("userId") String userId,
            @RequestParam("description") String description,
            @RequestParam("amount") int amount,
            @RequestParam("paymentMode") String paymentMode) {

        System.out.println("Billing");

        String status = "NA";

        CustomerInfo customer = service.getCustomer(customerNumber);

 
        
        if ((paymentMode.equalsIgnoreCase("cash")) || (paymentMode.equalsIgnoreCase("paytm"))) {
            Billing bill = new Billing();

            if (customer != null) {
                bill.setCustomerName(customer.getCustomerName());

            } else {
                bill.setCustomerName("New customer");
            }
            
           
            bill.setCustomerNumer(customerNumber);
            bill.setBillingBy(userId);
            bill.setDate(new Date());
            bill.setBillingMode(paymentMode);
            bill.setDescription(description);
            bill.setBillType("manual");
            bill.setBillAmount(amount);
            status = billService.createBill(bill);
        }

        if (paymentMode.equalsIgnoreCase("due")) {

            if (customer == null) {
                return "Unable to create due customer not found";
            }

            int customerTotaldue = billService.customerTotalDues(customerNumber);
            int avaiableCredit = customer.getMaxCreditLimit() - customerTotaldue - amount;
            System.out.println("avail cred limit"+avaiableCredit);

            if (avaiableCredit <= 0) {
                return "Customer reached credit limit unable to create due";
            }
            Dues due = new Dues();
            due.setCustomerName(customer.getCustomerName());
            due.setDate(new Date());
            due.setDescription(description);
            due.setStatus("");
            due.setBillingBy(userId);
            due.setAmount(amount);
            due.setCustomerNumber(customerNumber);
            status = billService.createBillDue(due);

        }

        return status;

    }
    // URL:http://localhost:8080/user/getRecentBillsInfo 
    @GetMapping("/user/getRecentBillsInfo")
    public List<Billing> getRecentBillsInfo()
    {
      List<Billing> billing=billService.getRecentBills();
        System.out.println(billing.size());
       return billing;
       
    }

}