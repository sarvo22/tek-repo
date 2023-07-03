package com.tekfilo.inventory.settlement.paymentpaid;

import com.tekfilo.inventory.settlement.paymentpaid.dto.PaymentPaidMainDto;
import com.tekfilo.inventory.settlement.paymentpaid.dto.PendingPaidInvoiceDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class PaymentPaidRequestPayload {

    private PaymentPaidMainDto main;
    private List<PendingPaidInvoiceDto> detail;
}
