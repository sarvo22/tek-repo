package com.tekfilo.account.partyaccount;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class PartyAccountDto implements Serializable {

    private Integer partyId;
    private String partyType;
    private String partyCode;
    private String partyName;
    private String currencyCode;
}
