package com.tekfilo.account.partyaccount;

import com.tekfilo.account.accmaster.AccMasterEntity;
import com.tekfilo.account.util.AccountResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/account/partyaccount")
public class PartyAccountController {

    @Autowired
    PartyAccountService partyAccountService;

    @RequestMapping("/create")
    public ResponseEntity<AccountResponse> create(@RequestBody PartyAccountDto partyAccountDto){
        AccountResponse response = new AccountResponse();
        try {
            partyAccountService.createPartyAccounts(partyAccountDto);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Accounts are created Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while creating account {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

}
