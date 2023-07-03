package com.tekfilo.admin.exchangerate;

import com.tekfilo.admin.util.AdminResponse;
import com.tekfilo.admin.util.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/exchangerate")
public class ExchangeRateController {

    @Autowired
    ExchangeRateService exchangeRateService;

    @GetMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<List<ExchangeRateEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection) {
        return new ResponseEntity<List<ExchangeRateEntity>>(exchangeRateService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection), HttpStatus.OK);
    }

    @GetMapping("/findbycode/{currency}")
    public ResponseEntity<List<ExchangeRateEntity>> findByCurrencyCode(@PathVariable("currency") String currency) {
        return new ResponseEntity<List<ExchangeRateEntity>>(exchangeRateService.findByCode(currency), HttpStatus.OK);

    }

    @GetMapping("/findbydateandcurrency/{currency}/{date}")
    public ResponseEntity<ExchangeRateEntity> findbydateandcurrency(@PathVariable("currency") String currency,
                                                                    @PathVariable("date") Date date) {
        return new ResponseEntity<ExchangeRateEntity>(exchangeRateService.findByCodeAndDate(currency, date), HttpStatus.OK);

    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<ExchangeRateEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<ExchangeRateEntity>(exchangeRateService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<AdminResponse> save(@RequestBody ExchangeRateDto exchangeRateDto) {
        AdminResponse response = new AdminResponse();
        try {
            ExchangeRateEntity entity = exchangeRateService.save(exchangeRateDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<AdminResponse> update(@RequestBody ExchangeRateDto exchangeRateDto) {
        AdminResponse response = new AdminResponse();
        try {
            ExchangeRateEntity entity = exchangeRateService.save(exchangeRateDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}/{operateby}")
    public ResponseEntity<AdminResponse> remove(@PathVariable("id") Integer id,
                                                @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            ExchangeRateEntity entity = exchangeRateService.findById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            exchangeRateService.remove(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying   {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }
}
