package com.tekfilo.inventory.settlement.paymentreceived;

import com.tekfilo.inventory.settlement.paymentreceived.dto.PaymentMainDto;
import com.tekfilo.inventory.settlement.paymentreceived.dto.PendingInvoiceDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class PaymentReceivedRequestPayload {

    private PaymentMainDto main;
    private List<PendingInvoiceDto> detail;
}
