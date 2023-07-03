package com.tekfilo.account.transaction.cashpaymentreceipt.dto;

import com.tekfilo.account.transaction.cashpaymentreceipt.dto.CashPaymentReceiptDetailDto;
import com.tekfilo.account.transaction.cashpaymentreceipt.dto.CashPaymentReceiptMainDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class CashRequestPayload {

    private CashPaymentReceiptMainDto main;
    private List<CashPaymentReceiptDetailDto> detail;
}
