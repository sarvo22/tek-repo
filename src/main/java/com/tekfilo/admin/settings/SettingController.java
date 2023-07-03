package com.tekfilo.admin.settings;

import com.tekfilo.admin.settings.dto.DefaultChargesDto;
import com.tekfilo.admin.settings.entity.DefaultChargesEntity;
import com.tekfilo.admin.settings.service.SettingService;
import com.tekfilo.admin.util.AdminResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/settings")
public class SettingController {

    @Autowired
    SettingService settingService;

    @GetMapping("/getdefaultcharges/{invoicetype}")
    public ResponseEntity<List<DefaultChargesEntity>> getDefaultCharges(@PathVariable("invoicetype") String invoiceType) {
        return new ResponseEntity<List<DefaultChargesEntity>>(settingService.getDefaultCharges(invoiceType), HttpStatus.OK);
    }

    @PostMapping("/savedefaultcharges")
    public ResponseEntity<AdminResponse> savedefaultcharges(@RequestBody DefaultChargesDto defaultChargesDto) {
        AdminResponse response = new AdminResponse();
        try {
            DefaultChargesEntity entity = settingService.save(defaultChargesDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Saved Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);

    }
}
