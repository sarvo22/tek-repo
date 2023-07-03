package com.tekfilo.admin.dashboard;

import com.tekfilo.admin.dashboard.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/dashboard")
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    @GetMapping("/getsalestitle/{currency}")
    public ResponseEntity<List<Map<String, Object>>> getSalesSummary(@PathVariable("currency") String currency) {
        return new ResponseEntity<List<Map<String, Object>>>(dashboardService.getSalesSummary(currency), HttpStatus.OK);
    }

    @GetMapping("/getpurchasetitle/{currency}")
    public ResponseEntity<List<Map<String, Object>>> getPurchaseSummary(@PathVariable("currency") String currency) {
        return new ResponseEntity<List<Map<String, Object>>>(dashboardService.getPurchaseSummary(currency), HttpStatus.OK);
    }

    @GetMapping("/getpayablesummary/{currency}")
    public ResponseEntity<List<Map<String, Object>>> getPayableSummary(@PathVariable("currency") String currency) {
        return new ResponseEntity<List<Map<String, Object>>>(dashboardService.getPayableSummary(currency), HttpStatus.OK);
    }

    @GetMapping("/getreceivablesummary/{currency}")
    public ResponseEntity<List<Map<String, Object>>> getReceivableSummary(@PathVariable("currency") String currency) {
        return new ResponseEntity<List<Map<String, Object>>>(dashboardService.getReceivabeSummary(currency), HttpStatus.OK);
    }

    @GetMapping("/getweeklysales/{currency}")
    public ResponseEntity<List<Map<String, Object>>> getWeeklySales(@PathVariable("currency") String currency) {
        return new ResponseEntity<List<Map<String, Object>>>(dashboardService.getWeeklySales(currency), HttpStatus.OK);
    }

    @GetMapping("/getmonthlysales/{currency}")
    public ResponseEntity<List<Map<String, Object>>> getMonthlySales(@PathVariable("currency") String currency) {
        return new ResponseEntity<List<Map<String, Object>>>(dashboardService.getMonthlySales(currency), HttpStatus.OK);
    }

    @GetMapping("/getinventorytitle/{currency}")
    public ResponseEntity<List<Map<String, Object>>> getInventorySummary(@PathVariable("currency") String currency) {
        return new ResponseEntity<List<Map<String, Object>>>(dashboardService.getInventorySummary(currency), HttpStatus.OK);
    }

    @GetMapping("/getcommoditygroupinventory")
    public ResponseEntity<List<Map<String, Object>>> getCommodityGroupInventory() {
        return new ResponseEntity<List<Map<String, Object>>>(dashboardService.getCommodityGroupInventory(), HttpStatus.OK);
    }

    @GetMapping("/getshapegroupinventory")
    public ResponseEntity<List<Map<String, Object>>> getShapeInventory() {
        return new ResponseEntity<List<Map<String, Object>>>(dashboardService.getShapeGroupInventory(), HttpStatus.OK);
    }

    @GetMapping("/getweeklypaymentreceived")
    public ResponseEntity<List<Map<String, Object>>> getWeeklyPaymentReceived() {
        return new ResponseEntity<List<Map<String, Object>>>(dashboardService.getWeeklyPaymentReceived(), HttpStatus.OK);
    }

    @GetMapping("/getbillsreceivableslist")
    public ResponseEntity<List<Map<String, Object>>> getBillsReceivablesList() {
        return new ResponseEntity<List<Map<String, Object>>>(dashboardService.getBillsReceivablesList(), HttpStatus.OK);
    }

    @GetMapping("/getbillspayablelist")
    public ResponseEntity<List<Map<String, Object>>> getBillsPayablesList() {
        return new ResponseEntity<List<Map<String, Object>>>(dashboardService.getBillsPayablesList(), HttpStatus.OK);
    }
}
