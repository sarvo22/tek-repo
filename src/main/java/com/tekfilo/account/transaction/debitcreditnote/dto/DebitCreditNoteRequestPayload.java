package com.tekfilo.account.transaction.debitcreditnote.dto;

import com.tekfilo.account.transaction.debitcreditnote.dto.DebitCreditNoteDetailDto;
import com.tekfilo.account.transaction.debitcreditnote.dto.DebitCreditNoteMainDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class DebitCreditNoteRequestPayload {

    private DebitCreditNoteMainDto main;
    private List<DebitCreditNoteDetailDto> detail;
}
