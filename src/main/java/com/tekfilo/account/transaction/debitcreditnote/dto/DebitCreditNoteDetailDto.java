package com.tekfilo.account.transaction.debitcreditnote.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
public class DebitCreditNoteDetailDto {

    private Integer id;
    private Integer invId;
    private Integer accountId;
    private String description;
    private Double taxPct;
    private BigDecimal grossAmount;
    private BigDecimal taxAmount;
    private BigDecimal netAmount;
}
