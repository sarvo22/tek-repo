package com.tekfilo.account.transaction.bankpaymentreceipt.dto;

import com.tekfilo.account.transaction.bankpaymentreceipt.dto.BankPaymentReceiptDetailDto;
import com.tekfilo.account.transaction.bankpaymentreceipt.dto.BankPaymentReceiptMainDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class BankRequestPayload {

    private BankPaymentReceiptMainDto main;
    private List<BankPaymentReceiptDetailDto> detail;
}
