package com.tekfilo.account.accmaster;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Data
public class AccMasterDto {
    private Integer id;
    private String accountCode;
    private String accountName;
    private String accountNo;
    private String currencyCode;
    private Integer isMasterAccount;
    private Integer subgroupId;
    private Integer ifrsSubgroupId;
    private String accountNature;
    private String debitCreditType;
    private String description;
    private String accountCategory;
    private Integer costCenterApplicable;

    private String bankName;

    private String bankAccountNo;

    private String bankIfscCode;

    private String bankSwiftCode;

    private String bankPayeeName;

    private String bankAccountType;
    private String bankAddress;
    private String partyType;
    private Integer partyId;
    private Integer sequence;
    private Integer isLocked;
}
