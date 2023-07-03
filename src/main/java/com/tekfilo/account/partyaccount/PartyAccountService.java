package com.tekfilo.account.partyaccount;

import com.tekfilo.account.accmaster.AccMasterDto;
import com.tekfilo.account.accmaster.AccMasterEntity;
import com.tekfilo.account.accmaster.AccMasterService;
import com.tekfilo.account.util.AccountConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PartyAccountService {

    @Autowired
    AccMasterService accMasterService;

    public void createPartyAccounts(PartyAccountDto partyAccountDto) throws Exception{
        List<AccMasterDto> accMasterDtoList = new ArrayList<>();
        AccountConstants.accountMappingList.stream().forEach(e->{
            AccMasterDto accMasterDto = new AccMasterDto();
            accMasterDto.setId(null);
            accMasterDto.setAccountCode(partyAccountDto.getPartyCode());
            accMasterDto.setAccountName(
                        partyAccountDto.getPartyName()
                        .concat(getPurchaseSales(e,partyAccountDto.getPartyType()))
                        .concat(" A/C"));
            accMasterDto.setAccountNo(partyAccountDto.getPartyName().concat("-").concat(partyAccountDto.getPartyCode()));
            accMasterDto.setCurrencyCode(partyAccountDto.getCurrencyCode());
            accMasterDto.setIsMasterAccount(0);
            //private Integer subgroupId;
            //private Integer ifrsSubgroupId;
            accMasterDto.setAccountNature(AccountConstants.BSA);
            accMasterDto.setDebitCreditType(AccountConstants.CREDIT_TYPE);
            accMasterDto.setDescription("created through Party master");
            accMasterDto.setAccountCategory(partyAccountDto.getPartyType().toUpperCase().concat("_".concat(e)));
            accMasterDto.setCostCenterApplicable(0);
            accMasterDtoList.add(accMasterDto);
        });

        accMasterDtoList.stream().forEach(acc->{
            try {
                accMasterService.save(acc);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String getPurchaseSales(String accountType, String partyType) {
        return "";
    }
}
