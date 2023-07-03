package com.tekfilo.admin.currency;

import com.tekfilo.admin.exchangerate.ExchangeRateEntity;
import com.tekfilo.admin.exchangerate.ExchangeRateService;
import com.tekfilo.admin.util.AdminResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/admin/currency")
public class CurrencyController {


    @Autowired
    CurrencyService currencyService;

    @Autowired
    CurrencyCompanyMapService currencyCompanyMapService;

    @Autowired
    ExchangeRateService exchangeRateService;

    @GetMapping("/search")
    public ResponseEntity<List<CurrencyEntity>> findAll() {
        return new ResponseEntity<List<CurrencyEntity>>(currencyService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/findcurrencybycompany/{companyid}")
    public ResponseEntity<List<CurrencyCompanyMapEntity>> findAllCurrenciesByCompanyId(@PathVariable("companyid") Integer companyId) {
        List<CurrencyCompanyMapEntity> entities = currencyCompanyMapService.findAllCurrenciesByCompanyId(companyId);
        setCurrencies(entities);
        return new ResponseEntity<List<CurrencyCompanyMapEntity>>(entities, HttpStatus.OK);
    }

    private void setCurrencies(List<CurrencyCompanyMapEntity> entities) {
        entities.stream().forEachOrdered(e -> {
            e.setCurrency(currencyService.findByCode(e.getCurrencyCode()));
            ExchangeRateEntity entity = exchangeRateService.getLastExchange(e.getCurrencyCode());
            e.setLastExchangeRate(entity.getExchangeRate());
            e.setLastExchangeRateDate(entity.getExchangeRateDate());
        });
    }

    @PostMapping("/currencycompanysave")
    public ResponseEntity<AdminResponse> saveCompanyCurrency(@RequestBody Map<String, Object> input) {
        final String currencyCode = (String) input.get("currencyCode");
        final Integer companyId = Integer.parseInt(input.get("companyId").toString());
        final String operateBy = (String) input
                .get("operateBy");

        AdminResponse adminResponse = new AdminResponse();
        try {
            currencyCompanyMapService.save(companyId, currencyCode, operateBy);
            adminResponse.setMessage("Saved Successfully");
            adminResponse.setStatus(100);
            adminResponse.setLangStatus("save_100");
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            adminResponse.setStatus(101);
            adminResponse.setLangStatus("save_101");
            adminResponse.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
        }
        return new ResponseEntity<AdminResponse>(adminResponse, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<AdminResponse> updateCurrency(@RequestBody CurrencyDto currencyDto) {
        AdminResponse adminResponse = new AdminResponse();
        try {
            currencyService.updateCurrency(currencyDto);
            adminResponse.setMessage("Saved Successfully");
            adminResponse.setStatus(100);
            adminResponse.setLangStatus("save_100");
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            adminResponse.setStatus(101);
            adminResponse.setLangStatus("save_101");
            adminResponse.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
        }
        return new ResponseEntity<AdminResponse>(adminResponse, HttpStatus.OK);
    }

    @PutMapping("/removecompanymap/{mapid}")
    public ResponseEntity<AdminResponse> removecompanymap(@PathVariable("mapid") Integer mapId) {
        AdminResponse adminResponse = new AdminResponse();
        try {
            currencyCompanyMapService.deleteCompanyMap(mapId);
            adminResponse.setMessage("Removed Successfully");
            adminResponse.setStatus(100);
            adminResponse.setLangStatus("save_100");
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            adminResponse.setStatus(101);
            adminResponse.setLangStatus("save_101");
            adminResponse.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
        }
        return new ResponseEntity<AdminResponse>(adminResponse, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<AdminResponse> saveCurrency(@RequestBody CurrencyDto currencyDto) {
        AdminResponse adminResponse = new AdminResponse();
        try {
            currencyService.createCurrency(currencyDto);
            adminResponse.setMessage("Saved Successfully");
            adminResponse.setStatus(100);
            adminResponse.setLangStatus("save_100");
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            adminResponse.setStatus(101);
            adminResponse.setLangStatus("save_101");
            adminResponse.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
        }
        return new ResponseEntity<AdminResponse>(adminResponse, HttpStatus.OK);
    }

}
