package com.tekfilo.sso.apiconfig;

import com.tekfilo.sso.base.SSOResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/sso/api")
public class ApiController {

    @Autowired
    ApiService apiService;

    @GetMapping("/getapilist")
    public ResponseEntity<List<ApiEntity>> getApiList() {
        return new ResponseEntity<List<ApiEntity>>(apiService.getApiList(), HttpStatus.OK);
    }


    @PostMapping("/configapi")
    public ResponseEntity<SSOResponse> saveApiConfig(@RequestBody ApiConfigDto apiConfigDto) {
        SSOResponse ssoResponse = new SSOResponse();
        try {
            apiService.saveApiConfig(apiConfigDto);
            ssoResponse.setStatus(100);
            ssoResponse.setLangStatus("lang_100");
            ssoResponse.setMessage("Saved Successfully");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ssoResponse.setStatus(101);
            ssoResponse.setLangStatus("lang_101");
            ssoResponse.setMessage(Optional.ofNullable(ex.getCause()).isPresent() ? ex.getCause().getMessage() : ex.getMessage());
        }
        return new ResponseEntity<SSOResponse>(ssoResponse, HttpStatus.OK);
    }

    @GetMapping("/getapiconfigbyid/{apiid}/{companyid}")
    public ResponseEntity<ApiConfigEntity> findApiConfigById(
            @PathVariable("apiid") Integer apiId,
            @PathVariable("companyid") Integer companyId) {
        return new ResponseEntity<ApiConfigEntity>(this.apiService.getApiConfigById(apiId, companyId), HttpStatus.OK);
    }


}
