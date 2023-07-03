package com.tekfilo.inventory.autonumber;

import com.tekfilo.inventory.util.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/inventory/autonumber")
public class AutoNumberController {

    @Autowired
    AutoNumberGeneratorService autoNumberGeneratorService;

    @GetMapping("/nextnumber/{invoicetype}")
    public ResponseEntity<InventoryResponse> nextNumber(@PathVariable("invoicetype") String invoiceType) {
        InventoryResponse inventoryResponse = new InventoryResponse();
        try{
            String number = autoNumberGeneratorService.getNextDisplayNumber(invoiceType);
            inventoryResponse.setVoucherNo(number);
            inventoryResponse.setStatus(100);
            inventoryResponse.setLangStatus("get_100");
            inventoryResponse.setMessage("Number generated");
        }catch (Exception exception){
            inventoryResponse.setStatus(101);
            inventoryResponse.setLangStatus("get_101");
            inventoryResponse.setMessage(exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(inventoryResponse, HttpStatus.OK);
    }
}
