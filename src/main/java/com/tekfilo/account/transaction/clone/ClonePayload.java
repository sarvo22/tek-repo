package com.tekfilo.account.transaction.clone;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClonePayload {
    private String moduleName;
    private String invoiceType;
    private List<Integer> ids;
    private String cloneType;
    private String attachment;
}
