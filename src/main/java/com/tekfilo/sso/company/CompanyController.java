package com.tekfilo.sso.company;

import com.tekfilo.sso.base.SSOResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sso/company")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @PutMapping("/modify")
    public ResponseEntity<SSOResponse> save(@RequestBody CompanyDto companyDto) {
        SSOResponse ssoResponse = new SSOResponse();
        if (companyDto.getId() == null) {
            ssoResponse.setStatus(101);
            ssoResponse.setLangStatus("save_101");
            ssoResponse.setMessage("Company UID should be passed to update the Company information");
            return new ResponseEntity<SSOResponse>(ssoResponse, HttpStatus.OK);
        }
        try {
            companyService.saveCompany(companyDto);
        } catch (Exception e) {
            ssoResponse.setStatus(101);
            ssoResponse.setLangStatus("save_101");
            ssoResponse.setMessage(e.getCause().getMessage());
            log.error(e.getCause().getMessage());
            return new ResponseEntity<SSOResponse>(ssoResponse, HttpStatus.OK);
        }

        ssoResponse.setStatus(100);
        ssoResponse.setLangStatus("save_100");
        ssoResponse.setMessage("Company Profile Updated Successfully");
        return new ResponseEntity<SSOResponse>(ssoResponse, HttpStatus.OK);
    }
}
