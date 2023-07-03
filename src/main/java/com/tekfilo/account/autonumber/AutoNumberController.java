package com.tekfilo.account.autonumber;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<String> nextNumber(@PathVariable("invoicetype") String invoiceType) {
        return ResponseEntity.ok(autoNumberGeneratorService.getNextDisplayNumber(invoiceType));
    }
}
